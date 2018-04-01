package com.tmooc.work.controller;

import java.util.*;

import com.tmooc.work.dao.StudentEntityDao;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.StudentEntity;
import com.tmooc.work.util.MyExcelUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ExcelController {
    @Autowired
    private StudentEntityDao studentEntityDao;
    public String upload(@RequestParam("file")MultipartFile file){

    }
    @RequestMapping("export")
    public void export(HttpServletResponse response) {
        //模拟从数据库获取需要导出的数据
        List<StudentEntity> studentEntities = new ArrayList<>();
        StudentEntity studentEntity0 = new StudentEntity("相依、诠释今朝", "昆明-杨聪（春", "553011904", "达内Java2017VIP群3", "462633904");
        StudentEntity studentEntity1 = new StudentEntity("相依、诠释今朝", "昆明-杨聪（春", "553011904", "达内Java2017VIP群3", "462633904");
        StudentEntity studentEntity2 = new StudentEntity("相依、诠释今朝", "昆明-杨聪（春", "553011904", "达内Java2017VIP群3", "462633904");
        StudentEntity studentEntity3 = new StudentEntity("相依、诠释今朝", "昆明-杨聪（春", "553011904", "达内Java2017VIP群3", "462633904");
        StudentEntity studentEntity4 = new StudentEntity("相依、诠释今朝", "昆明-杨聪（春", "553011904", "达内Java2017VIP群3", "462633904");
        StudentEntity studentEntity5 = new StudentEntity("相依、诠释今朝", "昆明-杨聪（春", "553011904", "达内Java2017VIP群3", "462633904");
        studentEntities.add(studentEntity0);
        studentEntities.add(studentEntity1);
        studentEntities.add(studentEntity2);
        studentEntities.add(studentEntity3);
        studentEntities.add(studentEntity4);
        studentEntities.add(studentEntity5);
        //导出操作
        MyExcelUtils.exportExcel(studentEntities, "", "", StudentEntity.class, "students.xls", response);
    }

    @RequestMapping("importExcel")
    @ResponseBody
    public TmoocResult importExcel() {
        String filePath = "C:\\Users\\hjqno\\Desktop\\students.xlsx"; //解析excel，
        List<StudentEntity> list = MyExcelUtils.importExcel(filePath, 1, 1, StudentEntity.class);
        List<StudentEntity> list1 = studentEntityDao.save(list);
        return TmoocResult.ok(list1);
    }
}