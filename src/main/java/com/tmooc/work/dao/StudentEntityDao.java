package com.tmooc.work.dao;

import com.tmooc.work.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface StudentEntityDao extends JpaRepository<StudentEntity,Integer> {
}
