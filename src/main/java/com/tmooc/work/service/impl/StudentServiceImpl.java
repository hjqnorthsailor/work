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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    public Student changeStage(Integer id,User user) {
        Student student = studentDao.findOne(id);
        student.setStage(1);
        student.setUser(user.getUsername());
        return  studentDao.saveAndFlush(student);
    }

    @Override
    @Transactional
    public void delete(Integer id, User user) {
        studentDao.delete(id);

    }

    @Override
    @Transactional
    public void delete(String studentQQ) {
        studentDao.deleteAllByStudentQQ(studentQQ);
    }

    @Override
    public Student changeMark(Integer id,Integer mark, User user) {
        Student student = studentDao.findOne(id);
        student.setMark(mark);
        student.setUser(user.getUsername());
        return  studentDao.saveAndFlush(student);
    }

    @Override
    @Transactional
    public void save(List<Student> list) {
        studentDao.save(list);
    }

}
