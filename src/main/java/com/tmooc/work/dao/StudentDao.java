package com.tmooc.work.dao;

import com.tmooc.work.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface StudentDao extends JpaRepository<Student,Integer>,JpaSpecificationExecutor<Student>{
    Student findByStudentQQ(String studentQQ);
    void deleteAllByStudentQQ(String studentQQ);
    Integer countAllByStage(Integer stage);
}
