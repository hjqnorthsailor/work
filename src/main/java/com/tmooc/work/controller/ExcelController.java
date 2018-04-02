package com.tmooc.work.controller;

import java.io.IOException;
import java.util.*;

import com.alibaba.druid.util.HttpClientUtils;
import com.tmooc.work.dao.StudentEntityDao;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.StudentEntity;
import com.tmooc.work.util.FastDFSClientWrapper;
import com.tmooc.work.util.MyExcelUtils;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private StudentEntityDao studentEntityDao;
    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;

    @RequestMapping("/upload")
    @ResponseBody
    public TmoocResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        final String filePath = fastDFSClientWrapper.uploadFile(file);
        System.out.println(filePath);
        return TmoocResult.ok(filePath);
    }

    @RequestMapping("/export")
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

    @RequestMapping("/import")
    @ResponseBody
    public TmoocResult importExcel(@RequestParam("remotFilePath") String remotFilePath) throws IOException {
//        String remotFilePath = "http://140.143.0.246:8888/group1/M00/00/00/rBUADFrB0UaAanoiAACE4Uhh1WY40.xlsx";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String localFilePath="D:\\test\\";//本地目录
        String filePath=localFilePath+remotFilePath.substring(remotFilePath.lastIndexOf("/")+1);//本地文件名
        boolean isDowload=fastDFSClientWrapper.executeDownloadFile(httpClient,remotFilePath,filePath,"UTF-8",true);
        if (isDowload) {
            List<StudentEntity> list = MyExcelUtils.importExcel(filePath, 1, 1, StudentEntity.class);
            List<StudentEntity> list1 = studentEntityDao.save(list);
            return TmoocResult.ok(list1);
        }
        return TmoocResult.build(500,"error");
    }

}