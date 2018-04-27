package com.tmooc.work.service;

import com.tmooc.work.entity.Student;
import com.tmooc.work.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface StudentService {
    /** 分页查询所有学生**/
    Page<Student> findAll(PageRequest request);
    Page<Student> findAll(Example<Student> studentExample,PageRequest request);
    Long getTotal(Example<Student> studentExample);
    Student changeStage(String studentQQ, User user);
    void delete(Integer id, User user);

    Student changeMark(String studentQQ,Integer mark, User user);
}
