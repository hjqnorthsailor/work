package com.tmooc.work.controller;


import com.tmooc.work.DTO.DataTablesRequest;
import com.tmooc.work.DTO.DataTablesResponse;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.Student;
import com.tmooc.work.entity.User;
import com.tmooc.work.service.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 多条件分页查询
     * @param dataTablesRequest
     * @return
     */
    @RequestMapping("/findAll")
    public DataTablesResponse<Student> studentList(@RequestBody final DataTablesRequest dataTablesRequest){
        int size=dataTablesRequest.getLength();
        int s=dataTablesRequest.getStart();
        int page=s/size;
        Student student=new Student();
        copyProperties(dataTablesRequest,student);//拷贝参数
        PageRequest pageRequest = new PageRequest(page, size);//分页请求
        /**
         * 查询匹配器
         */
        ExampleMatcher matcher=ExampleMatcher.matching()
                .withMatcher("user",m->m.contains())//模糊匹配
                .withMatcher("studentQQ",m->m.contains())//精确匹配
                .withMatcher("qunName",m->m.contains())
                .withMatcher("qunNum",m->m.exact())
                .withMatcher("stage",m->m.exact())
                .withMatcher("mark",m->m.exact());
        Example<Student> studentExample=Example.of(student,matcher);//JPA Example查询
        final Page<Student> studentList = studentService.findAll(studentExample,pageRequest);
        System.out.println(studentList.getTotalElements());
        long total=studentService.getTotal(studentExample);
        return new DataTablesResponse(dataTablesRequest.getDraw(),total,total,"",studentList.getContent());
    }

    /**
     * 修改学员跟进状态
     * @param
     * @return
     */
    @PostMapping("/changeStage")
    public TmoocResult changeStage(Integer id, User user){
        Student student = studentService.changeStage(id,user);
        return  TmoocResult.ok(student);
    }
    /**
     * 修改学员标签
     * @param
     * @return
     */
    @PostMapping("/changeMark")
    public TmoocResult changeMark(Integer id,Integer mark, User user){
        Student student = studentService.changeMark(id,mark,user);
        return  TmoocResult.ok(student);
    }
    @PostMapping("/delete")
    public TmoocResult deleteStudent(@RequestParam("id")Integer id){
        studentService.delete(id);
        return  TmoocResult.ok();
    }
    @PostMapping("/reset")
    public TmoocResult reset(@RequestParam("id") Integer id,User user){
        Student student=studentService.resetMark(id,user);
        if (student==null){return TmoocResult.error();}
        return TmoocResult.ok();
    }
    /**
     * 拷贝参数(使用BeanUtils会拷贝空值过来，造成查询错误）
     * @param request
     * @param student
     * @return
     */
    private Student copyProperties(DataTablesRequest request,Student student){
        if (StringUtils.isNotEmpty(request.getUser())){
            student.setUser(request.getUser());
        }
        if (StringUtils.isNotEmpty(request.getStudentQQ())){
            student.setStudentQQ(request.getStudentQQ());
        }
        if (StringUtils.isNotEmpty(request.getQunName())){
            student.setQunName(request.getQunName());
        }
        if (StringUtils.isNotEmpty(request.getQunNum())){
            student.setQunNum(request.getQunNum());
        }
        if (StringUtils.isNotEmpty(request.getStage())){
            student.setStage(Integer.parseInt(request.getStage()));
        }
        if (StringUtils.isNotEmpty(request.getMark())){
            student.setMark(Integer.parseInt(request.getMark()));
        }

        return student;
    }

}
