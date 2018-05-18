package com.tmooc.work.test;

import com.tmooc.work.dao.StudentDao;
import com.tmooc.work.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentTest {
    @Autowired
    private StudentDao studentDao;
    @Test
    public void test1(){
        Student one = studentDao.findOne(1415);
        System.out.println(one.getReachRates().get(0).getPercent());

    }
}
