package com.tmooc.work.service;

import com.tmooc.work.DTO.TeacherDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TeacherService {
    List<TeacherDTO> findAll();

}
