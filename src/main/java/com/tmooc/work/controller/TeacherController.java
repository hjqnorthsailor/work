package com.tmooc.work.controller;

import com.tmooc.work.DTO.TeacherDTO;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @RequestMapping("/teacher/findAll")
    @ResponseBody
    public TmoocResult findAll(){
        List<TeacherDTO> teacherDTOList = teacherService.findAll();
        teacherDTOList.forEach(teacherDTO -> System.out.println(teacherDTO.getEmail() ));
        return TmoocResult.ok(teacherDTOList);
    }


}
