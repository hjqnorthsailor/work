package com.tmooc.work.dao;

import com.tmooc.work.entity.Teacher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherDao extends CrudRepository<Teacher,Integer> {

}
