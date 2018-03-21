package com.tmooc.work.controller;

import com.alibaba.druid.sql.PagerUtils;
import com.tmooc.work.DTO.PageDTO;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.Student;
import com.tmooc.work.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.context.request.ServletWebRequest;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    public TmoocResult studentList(PageDTO pageDTO){
        Sort sort=new Sort(Sort.Direction.fromString(pageDTO.getDirection()), pageDTO.getProperties());
        PageRequest pageRequest = new PageRequest(pageDTO.getPageNo()-1, pageDTO.getSize(), sort);
        final Page<Student> studentList = studentService.findAll(pageRequest);
        return  TmoocResult.ok(studentList);
    }
}
