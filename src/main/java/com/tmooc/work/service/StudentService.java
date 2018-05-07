package com.tmooc.work.service;

import com.tmooc.work.entity.Student;
import com.tmooc.work.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface StudentService {
    /** 分页查询所有学生**/
    Page<Student> findAll(PageRequest request);
    Page<Student> findAll(Example<Student> studentExample,PageRequest request);
    Long getTotal(Example<Student> studentExample);
    Student changeStage(Integer id, User user);
    void delete(Integer id);
    void delete(String studentQQ);
    Student changeMark(Integer id,Integer mark, User user);
    void save(List<Student> list);
    /** 重置学员状态及标记*/
    Student resetMark(Integer id, User user);
}
