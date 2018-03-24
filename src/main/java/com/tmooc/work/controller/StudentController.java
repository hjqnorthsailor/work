package com.tmooc.work.controller;

import com.tmooc.work.entity.Student;
import com.tmooc.work.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;
    @RequestMapping("/student/findAll")
    public Map<String,Object>  studentList(ServletWebRequest request,@RequestParam String draw,
                                           @RequestParam(name="start",defaultValue = "0") String start,
                                           @RequestParam(name="length",defaultValue = "10") String length,
                                           @RequestParam(name="search",defaultValue = "id") String search){
        int size=Integer.parseInt(length);
        int s=Integer.parseInt(start);
        int page=s/size;
        Sort sort=new Sort(Sort.Direction.ASC, search);
        PageRequest pageRequest = new PageRequest(page, size, sort);
        final Page<Student> studentList = studentService.findAll(pageRequest);
        Map<String,Object> map=new HashMap<>();
        long total=studentService.getTotal();
        map.put("draw",draw);
        map.put("data",studentList.getContent());
        map.put("recordsTotal",total);
        map.put("recordsFiltered",total);
        return map;
    }
}
