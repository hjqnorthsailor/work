package com.tmooc.work.service.impl;

import com.google.common.collect.Lists;
import com.tmooc.work.DTO.DataTablesRequest;
import com.tmooc.work.DTO.DataTablesResponse;
import com.tmooc.work.dao.StudentDao;
import com.tmooc.work.entity.Student;
import com.tmooc.work.entity.User;
import com.tmooc.work.enums.StudentMark;
import com.tmooc.work.enums.StudentStage;
import com.tmooc.work.service.StudentService;
import com.tmooc.work.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;

    @Override
    public Page<Student> findAll(Example<Student> studentExample, PageRequest request) {
        return studentDao.findAll(studentExample, request);
    }

    @Override
    public Page<Student> findAll(PageRequest request) {
        return studentDao.findAll(request);
    }

    @Override
    @Transactional
    public Student changeStage(Integer id) {
        Student student = studentDao.findOne(id);
        student.setStage(StudentStage.FOLLOWUP.getStage());
//        student.setUser(user.getUsername());https://www.jianshu.com/p/14cb69646195
        return studentDao.saveAndFlush(student);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        studentDao.delete(id);

    }

    @Override
    public Student changeMark(Integer id, Integer mark) {
        Student student = studentDao.findOne(id);
        student.setMark(mark);
        System.out.println(mark);
//        student.setUser(user.getUsername());https://www.jianshu.com/p/14cb69646195
        return studentDao.saveAndFlush(student);
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
    public Student changeRemark(Integer id, String remark) {
        Student student = studentDao.findOne(id);
        student.setRemark(remark);
        return studentDao.saveAndFlush(student);
    }

    @Override//https://www.cnblogs.com/happyday56/p/4661839.html
    public Page<Student> findAll(DataTablesRequest request, Pageable pageable) {
        Page<Student> studentList = studentDao.findAll((root, criteriaQuery, criteriaBuilder) ->
        {
            List<Predicate> predicateList = Lists.newArrayList();
            final Path<String> user = root.get("user");
            if (StringUtils.isNotEmpty(request.getUser())) {
                Predicate userLike = criteriaBuilder.like(user, '%' + request.getUser() + '%');
                predicateList.add(userLike);
            }
            final Path<String> studentQQ = root.get("studentQQ");
            if (StringUtils.isNotEmpty(request.getStudentQQ())) {
                Predicate qqLike = criteriaBuilder.like(studentQQ, '%' + request.getStudentQQ() + '%');
                predicateList.add(qqLike);
            }
            final Path<String> qunName = root.get("qunName");
            if (StringUtils.isNotEmpty(request.getQunName())) {
                Predicate qunNameLike = criteriaBuilder.like(qunName, '%' + request.getQunName() + '%');
                predicateList.add(qunNameLike);
            }
            final Path<String> qunNum = root.get("qunNum");
            if (StringUtils.isNotEmpty(request.getQunNum())) {
                predicateList.add(criteriaBuilder.like(qunNum, '%' + request.getQunNum() + '%'));
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
            Predicate[] predicates = new Predicate[predicateList.size()];
            CriteriaQuery<?> query = criteriaQuery.where(predicateList.toArray(predicates));
            return query.getRestriction();

        }, pageable);
        return studentList;
    }


    public DataTablesResponse<Student> findAll(DataTablesRequest dataTablesRequest) throws ParseException {
        int size = dataTablesRequest.getLength();
        int s = dataTablesRequest.getStart();
        int page = s / size;
        String startTime = dataTablesRequest.getStartTime();
        System.out.println(startTime);
        String endTime = dataTablesRequest.getEndTime();
        Student student = new Student();
        copyProperties(dataTablesRequest, student);//拷贝参数
        PageRequest pageRequest = new PageRequest(page, size);//分页请求
        /**
         * 查询匹配器
         */
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("user", m -> m.contains())//模糊匹配
                .withMatcher("studentQQ", m -> m.contains())//精确匹配
                .withMatcher("qunName", m -> m.contains())
                .withMatcher("qunNum", m -> m.exact())
                .withMatcher("stage", m -> m.exact())
                .withMatcher("mark", m -> m.exact());
        Example<Student> studentExample = Example.of(student, matcher);//JPA Example查询
        final Page<Student> studentList = findAll(studentExample, pageRequest);
        List<Student> students;
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = simpleDateFormat.parse(startTime.trim() + " 00:00:00");
            Date end = simpleDateFormat.parse(endTime.trim() + " 23:59:59");
            students = studentList.getContent().stream()
                    .filter(st -> DateUtils.isBetweenTime(st.getUpdateDateTime(), start, end))
                    .collect(Collectors.toList());

        } else {
            students = studentList.getContent();
        }
        long total = studentList.getTotalElements();
        return new DataTablesResponse(dataTablesRequest.getDraw(), total, total, "", students);
    }

    /**
     * 拷贝参数(使用BeanUtils会拷贝空值过来，造成查询错误）
     *
     * @param request
     * @param student
     * @return
     */
    private Student copyProperties(DataTablesRequest request, Student student) {
        if (StringUtils.isNotEmpty(request.getUser())) {
            student.setUser(request.getUser());
        }
        if (StringUtils.isNotEmpty(request.getStudentQQ())) {
            student.setStudentQQ(request.getStudentQQ());
        }
        if (StringUtils.isNotEmpty(request.getQunName())) {
            student.setQunName(request.getQunName());
        }
        if (StringUtils.isNotEmpty(request.getQunNum())) {
            student.setQunNum(request.getQunNum());
        }
        if (StringUtils.isNotEmpty(request.getStage())) {
            student.setStage(Integer.parseInt(request.getStage()));
        }
        if (StringUtils.isNotEmpty(request.getMark())) {
            student.setMark(Integer.parseInt(request.getMark()));
        }

        return student;
    }

}
