package com.tmooc.work.service.impl;

import com.tmooc.work.dao.StudentDao;
import com.tmooc.work.entity.Student;
import com.tmooc.work.entity.User;
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
    public Student changeStage(String studentQQ,User user) {
        Student student = studentDao.findByStudentQQ(studentQQ);
        student.setStage(1);
        student.setUser(user.getUsername());
        return  studentDao.saveAndFlush(student);
    }

    @Override
    public void delete(Integer id, User user) {
        studentDao.delete(id);
        saveUser(id,user);

    }

    @Override
    public Student changeMark(String studentQQ,Integer mark, User user) {
        Student student = studentDao.findByStudentQQ(studentQQ);
        student.setMark(mark);
        student.setUser(user.getUsername());
        return  studentDao.saveAndFlush(student);
    }

    private void saveUser(Integer id, User user){
        Student student = studentDao.getOne(id);
        student.setUser(user.getUsername());
        studentDao.saveAndFlush(student);
    }
}
