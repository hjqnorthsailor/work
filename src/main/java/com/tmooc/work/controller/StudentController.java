package com.tmooc.work.controller;


import com.tmooc.work.DTO.DataTablesRequest;
import com.tmooc.work.DTO.DataTablesResponse;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.Student;
import com.tmooc.work.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;
    /**
     * 多条件分页查询
     * @param dataTablesRequest 自己构建的请求体
     * @return
     */
    @RequestMapping("/findAll")
    public DataTablesResponse<Student> studentList(@RequestBody final DataTablesRequest dataTablesRequest){
        int size=dataTablesRequest.getLength();
        int s=dataTablesRequest.getStart();
        int page=s/size;
        PageRequest pageRequest = new PageRequest(page, size);//分页请求
        final Page<Student> studentList = studentService.findAll(dataTablesRequest, pageRequest);
        long total=studentList.getTotalElements();
        log.info(""+total);
        return new DataTablesResponse(dataTablesRequest.getDraw(),total,total,"",studentList.getContent());
    }


    /**
     * 修改学员跟进状态
     * @param
     * @return
     */
    @PostMapping("/changeStage")
    public TmoocResult changeStage(Integer id){
        Student student = studentService.changeStage(id);
        return  TmoocResult.ok(student);
    }
    /**
     * 修改学员标签
     * @param
     * @return
     */
    @PostMapping("/changeMark")
    public TmoocResult changeMark(Integer id,Integer mark){
        Student student = studentService.changeMark(id,mark);
        return  TmoocResult.ok(student);
    }

    /**
     * 删除学员信息
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public TmoocResult deleteStudent(@RequestParam("id")Integer id){
        studentService.delete(id);
        return  TmoocResult.ok();
    }

    /**
     * 重置学员状态
     * @param id
     * @return
     */
    @PostMapping("/reset")
    public TmoocResult reset(@RequestParam("id") Integer id){
        Student student=studentService.resetMark(id);
        if (student==null){return TmoocResult.error();}
        return TmoocResult.ok();
    }

    /**
     * 添加学员备注
     * @param id
     * @param remark
     * @return
     */
    @PostMapping("/remark")
    public TmoocResult remark(@RequestParam("id") Integer id,
                              @RequestParam("remark")String remark){
        Student student=studentService.changeRemark(id,remark);
        if (student==null){return TmoocResult.error();}
        return TmoocResult.ok();
    }

}
