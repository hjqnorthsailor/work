package com.tmooc.work.service.impl;

import com.google.common.collect.Lists;
import com.tmooc.work.DTO.DataTablesRequest;
import com.tmooc.work.dao.StudentDao;
import com.tmooc.work.entity.Student;
import com.tmooc.work.entity.User;
import com.tmooc.work.enums.StudentMark;
import com.tmooc.work.enums.StudentStage;
import com.tmooc.work.service.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
    public Student changeStage(Integer id) {
        Student student = studentDao.findOne(id);
        student.setStage(StudentStage.FOLLOWUP.getStage());
//        student.setUser(user.getUsername());https://www.jianshu.com/p/14cb69646195
        return  studentDao.saveAndFlush(student);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        studentDao.delete(id);

    }

    @Override
    @Transactional
    public void delete(String studentQQ) {
        studentDao.deleteAllByStudentQQ(studentQQ);
    }

    @Override
    public Student changeMark(Integer id,Integer mark) {
        Student student = studentDao.findOne(id);
        student.setMark(mark);
//        student.setUser(user.getUsername());https://www.jianshu.com/p/14cb69646195
        return  studentDao.saveAndFlush(student);
    }

    @Override
    @Transactional
    public void save(List<Student> list) {
        studentDao.save(list);
    }

    @Override
    public Student resetMark(Integer id) {
        Student student = studentDao.findOne(id);
        student.setStage(StudentStage.NOFOLLOWUP.getStage());
        student.setMark(StudentMark.NOMARK.getMark());
//        student.setUser(user.getUsername());//https://www.jianshu.com/p/14cb69646195
        return studentDao.saveAndFlush(student);
    }

    @Override
    public Student changeRemark(Integer id,String remark) {
        Student student = studentDao.findOne(id);
        student.setRemark(remark);
        return studentDao.saveAndFlush(student);
    }

    @Override
    public Page<Student> findAll(DataTablesRequest request, Pageable pageable) {
        Page<Student> studentList = studentDao.findAll((root, criteriaQuery, criteriaBuilder) ->
        {
            List<Predicate> predicateList=Lists.newArrayList();
            final Path<String> user = root.get("user");
            if (StringUtils.isNotEmpty(request.getUser())) {
                Predicate userLike = criteriaBuilder.like(user, '%'+request.getUser()+'%');
                predicateList.add(userLike);
            }
            final Path<String> studentQQ = root.get("studentQQ");
            if (StringUtils.isNotEmpty(request.getStudentQQ())) {
                Predicate qqLike = criteriaBuilder.like(studentQQ, '%'+request.getStudentQQ()+'%');
                predicateList.add(qqLike);
            }
            final Path<String> qunName = root.get("qunName");
            if (StringUtils.isNotEmpty(request.getQunName())) {
                Predicate qunNameLike = criteriaBuilder.like(qunName, '%'+request.getQunName()+'%');
                predicateList.add(qunNameLike);
            }
            final Path<String> qunNum = root.get("qunNum");
            if (StringUtils.isNotEmpty(request.getQunNum())) {
                predicateList.add(criteriaBuilder.like(qunNum, '%'+request.getQunNum()+'%'));
            }
            final Path<Integer> stage = root.get("stage");
            if (StringUtils.isNotEmpty(request.getStage())) {
                predicateList.add(criteriaBuilder.equal(stage, request.getStage()));
            }
            final Path<Integer> mark = root.get("mark");
            if (StringUtils.isNotEmpty(request.getMark())) {
                predicateList.add(criteriaBuilder.equal(mark, request.getMark()));
            }
            final Path<Date> updateTime = root.get("updateDateTime");
            String startTime = request.getStartTime();
            String endTime = request.getEndTime();
            if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date start = null;
                Date end = null;
                try {
                    start = simpleDateFormat.parse(startTime.trim() + " 00:00:00");
                    end = simpleDateFormat.parse(endTime.trim() + " 23:59:59");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                predicateList.add(criteriaBuilder.greaterThan(updateTime, start));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.lessThan(updateTime, end)));
            }
            Predicate[] predicates=new Predicate[predicateList.size()];
            CriteriaQuery<?> query = criteriaQuery.where(predicateList.toArray(predicates));
            return query.getRestriction();

        }, pageable);
        return studentList;
    }

}
