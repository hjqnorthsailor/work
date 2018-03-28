package com.tmooc.work.controller;

import com.tmooc.work.DTO.DataTablesRequest;
import com.tmooc.work.DTO.DataTablesResponse;
import com.tmooc.work.entity.Student;
import com.tmooc.work.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;
    @RequestMapping("/student/findAll")
    public DataTablesResponse<Student> studentList(@RequestBody final DataTablesRequest dataTablesRequest){
        System.out.println(dataTablesRequest.getDraw());
        int size=dataTablesRequest.getLength();
        int s=dataTablesRequest.getStart();
        int page=s/size;
        List<DataTablesRequest.Column> columns=dataTablesRequest.getColumns();

        List<Sort.Order> orders1=new ArrayList<>();
        Sort.Order order1=null;
        for (DataTablesRequest.Order order2 : dataTablesRequest.getOrders()){
            System.out.println(columns.get(order2.getColumn()).getData());
            order1=new Sort.Order(Sort.Direction.fromString(order2.getDir()),columns.get(order2.getColumn()).getData());
            orders1.add(order1);
        }

        Sort sort=new Sort(orders1);
        PageRequest pageRequest = new PageRequest(page, size, sort);
        final Page<Student> studentList = studentService.findAll(pageRequest);
        long total=studentService.getTotal();
        return new DataTablesResponse<Student>(dataTablesRequest.getDraw(),total,total,"",studentList.getContent());
    }
}
