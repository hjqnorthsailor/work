package com.tmooc.work.dao;


import com.tmooc.work.WorkApplicationTests;
import com.tmooc.work.entity.Role;
import com.tmooc.work.entity.Teacher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TeacherDaoTest extends WorkApplicationTests{
    @Autowired
    private TeacherDao teacherDao;
    @Test
    public void testFindAll(){
        Iterable<Teacher> all = teacherDao.findAll();
        all.forEach(a-> System.out.println(a));
    }
    @Test
    public void testInsert(){
        Teacher teacher=new Teacher();;
        teacher.setEmail("huangjq@tedu.cn");
        teacher.setLoginPwd("123456");
        Role role=new Role();
        role.setRoleName("小组长");
        role.setRoleDescription("负责本小组的组长");
        List<Role> roleList=new ArrayList<>();
        roleList.add(role);
        teacher.setRoleList(roleList);
        teacherDao.save(teacher);
    }

}