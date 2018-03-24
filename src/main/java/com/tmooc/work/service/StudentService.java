package com.tmooc.work.service;

import com.tmooc.work.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface StudentService {
    /** 分页查询所有学生**/
    Page<Student> findAll(PageRequest request);
    Long getTotal();
}
