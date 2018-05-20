package com.tmooc.work.test;

import com.tmooc.work.dao.StudentDao;
import com.tmooc.work.entity.Student;
import com.tmooc.work.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.sun.tools.corba.se.idl.constExpr.Expression.one;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentTest {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentService studentService;;
    @Test
    public void test1(){
        Student one = studentDao.findOne(1415);
        System.out.println(one.getReachRates().get(0).getPercent());

    }
    @Test
    public void test2(){
        List<Map<String, Object>> studentsInfo = studentService.findStudentsInfo();
        System.out.println(Arrays.toString(studentsInfo.toArray()));
    }
}
