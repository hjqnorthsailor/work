package com.tmooc.work.dao;

import com.tmooc.work.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface StudentDao extends JpaRepository<Student,Integer> {
}
