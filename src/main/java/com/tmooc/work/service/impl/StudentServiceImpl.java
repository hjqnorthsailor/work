package com.tmooc.work.service.impl;

import com.tmooc.work.dao.StudentDao;
import com.tmooc.work.entity.Student;
import com.tmooc.work.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;
    @Override
    public Page<Student> findAll(Example<Student> studentExample,PageRequest request) {
        return studentDao.findAll(studentExample,request);
    }

    @Override
    public Page<Student> findAll(PageRequest request) {
        return studentDao.findAll(request);
    }

    @Override
    public Long getTotal(Example<Student> studentExample){

        return studentDao.count(studentExample);
    }

    @Override
    public Student changeStage(String studentQQ) {
        Student student = studentDao.findByStudentQQ(studentQQ);
        student.setStage(1);
        return  studentDao.saveAndFlush(student);
    }

    @Override
    public void delete(Integer id) {
        studentDao.delete(id);
    }
}
