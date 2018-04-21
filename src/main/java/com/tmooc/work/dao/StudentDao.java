package com.tmooc.work.dao;

import com.tmooc.work.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentDao extends JpaRepository<Student,Integer> {
    Student findByStudentQQ(String studentQQ);

    void delete(Integer id);
}
