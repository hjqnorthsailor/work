package com.tmooc.work.service.impl;

import com.tmooc.work.DTO.TeacherDTO;
import com.tmooc.work.dao.TeacherDao;
import com.tmooc.work.entity.Teacher;
import com.tmooc.work.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherDao teacherDao;


    @Override
    public List<TeacherDTO> findAll() {
        Iterable<Teacher> teachers= teacherDao.findAll();
        List<TeacherDTO> teacherDTOList=new ArrayList<>();
        teachers.forEach(teacher -> {
            TeacherDTO teacherDTO=TeacherDTO.builder().teacherId(teacher.getId())
                    .nikeName(teacher.getNikeName())
                    .email(teacher.getEmail())
                    .phone(teacher.getPhone())
                    .profession(teacher.getProfession())
                    .team(teacher.getTeam())
                    .roleList(teacher.getRoleList())
                    .teacherName(teacher.getTeacherName())
                    .createTime(teacher.getCreateDateTime())
                    .lastLoginTime(teacher.getLastLoginTime())
                    .entryTime(teacher.getEntryTime()).build();
            teacherDTOList.add(teacherDTO);
        });
        return teacherDTOList;
    }
}
