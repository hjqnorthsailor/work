package com.tmooc.work.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tmooc.work.DTO.DataTablesRequest;
import com.tmooc.work.DTO.DataTablesResponse;
import com.tmooc.work.VO.MarkVO;
import com.tmooc.work.VO.StudentChartVO;
import com.tmooc.work.dao.StudentDao;
import com.tmooc.work.entity.Student;
import com.tmooc.work.enums.StudentMark;
import com.tmooc.work.enums.StudentStage;
import com.tmooc.work.service.StudentService;
import com.tmooc.work.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author northsailor
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Student> findAll(Example<Student> studentExample, PageRequest request) {
        return studentDao.findAll(studentExample, request);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
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


    @Override
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
                //模糊匹配
                .withMatcher("user", m -> m.contains())
                .withMatcher("studentQQ", m -> m.contains())
                //精确匹配
                .withMatcher("qunName", m -> m.contains())
                .withMatcher("qunNum", m -> m.exact())
                .withMatcher("stage", m -> m.exact())
                .withMatcher("mark", m -> m.exact());
        //JPA Example查询
        Example<Student> studentExample = Example.of(student, matcher);
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

    @Override
    public List<Map<String, Object>> findStudentsInfo() {
        String sql="SELECT s.`qun_name` AS qunName,COUNT(s.`studentqq`) AS countNum" +
                " FROM student s" +
                " WHERE s.`stage`=1" +
                " GROUP BY s.`qun_name`";
        Query query = entityManager.createNativeQuery(sql);
        final List<StudentChartVO> list = query.unwrap(SQLQuery.class)
                // 这里是设置字段的数据类型，有几点注意，首先这里的字段名要和目标实体的字段名相同，然后 sql 语句中的名称（别名）得与实体的相同
                .addScalar("qunName", StandardBasicTypes.STRING)
                .addScalar("countNum", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(StudentChartVO.class)).list();
        List<Map<String, Object>> resultList=Lists.newArrayList();
        list.forEach(q->{
            Map<String,Object> qunMap=Maps.newHashMap();
            qunMap.put("qunName",q.getQunName());
            qunMap.put("countNum",q.getCountNum());
           resultList.add(qunMap);
        });
        return resultList;
    }

    @Override
    public Integer countAllByStage(Integer stage) {
        return studentDao.countAllByStage(stage);
    }

    @Override
    public List<Map<String, Object>> findStudentsByMark() {
        String sql="SELECT COUNT(s.mark) as value,"+
        " case s.mark"+
        " WHEN 0 THEN '未跟进'"+
        " WHEN 1 THEN  '正常'"+
        " WHEN 2 THEN '未联系到'"+
        " WHEN 3 THEN '长时间未登录'"+
        " WHEN 4 THEN '已过期'"+
        " WHEN 5 THEN '转脱产'"+
        " WHEN 6 THEN '查询不到'"+
        " WHEN 7 THEN '其他(试听/休学)'"+
        " END AS mark"+
        " FROM student AS s"+
        " group by s.mark";
        Query query = entityManager.createNativeQuery(sql);
        final List<MarkVO> list = query.unwrap(SQLQuery.class)
                // 这里是设置字段的数据类型，有几点注意，首先这里的字段名要和目标实体的字段名相同，然后 sql 语句中的名称（别名）得与实体的相同
                .addScalar("mark", StandardBasicTypes.STRING)
                .addScalar("value", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(MarkVO.class)).list();
        List<Map<String, Object>> resultList=Lists.newArrayList();
        list.forEach(q->{
            Map<String,Object> markMap=Maps.newHashMap();
            markMap.put("mark",q.getMark());
            markMap.put("value",q.getValue());
            resultList.add(markMap);
        });
        return resultList;
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
