package com.tmooc.work.service;

import com.tmooc.work.DTO.DataTablesRequest;
import com.tmooc.work.DTO.DataTablesResponse;
import com.tmooc.work.entity.Student;
import com.tmooc.work.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.List;

public interface StudentService {
    /** 分页查询所有学生**/
    Page<Student> findAll(PageRequest request);
    Page<Student> findAll(Example<Student> studentExample,PageRequest request);
    /** 修改学员跟进状态*/
    Student changeStage(Integer id);
    void delete(Integer id);
    /** 修改学员标签*/
    Student changeMark(Integer id,Integer mark);
    void save(List<Student> list);
    /** 重置学员状态及标记*/
    Student resetMark(Integer id);

    Student changeRemark(Integer id,String remark);
    /** 使用criteriaQuery构建多条件分页查询**/
    Page<Student> findAll(DataTablesRequest request, Pageable pageable);
    /** 使用ExampleMtcher构建多条件分页查询**/
    DataTablesResponse<Student> findAll(DataTablesRequest dataTablesRequest) throws ParseException;
}
