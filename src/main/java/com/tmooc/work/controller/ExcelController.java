package com.tmooc.work.controller;

import java.io.IOException;
import java.util.*;

import com.alibaba.druid.util.HttpClientUtils;
import com.tmooc.work.dao.HuoYueDao;
import com.tmooc.work.dao.StudentDao;
import com.tmooc.work.dao.StudentEntityDao;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.HuoYue;
import com.tmooc.work.entity.Student;
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
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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
    private FastDFSClientWrapper fastDFSClientWrapper;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job importHuoYue;
    @Autowired
    private Job importStudent;

    @RequestMapping("/upload")
    @ResponseBody
    public TmoocResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        final String filePath = fastDFSClientWrapper.uploadFile(file);
        System.out.println(filePath);
        return TmoocResult.ok(filePath);
    }


    @RequestMapping("/importStudent")
    @ResponseBody
    public TmoocResult importExcel(@RequestParam("remoteFilePath") String remoteFilePath) throws IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobExecution jobExecution = getJobExecution(remoteFilePath,importStudent);
        return TmoocResult.ok(jobExecution.getExecutionContext().size());
    }

    @RequestMapping("/importHuoYue")
    @ResponseBody
    public TmoocResult importHuoYue(@RequestParam("remotFilePath") String remoteFilePath) throws IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobExecution jobExecution = getJobExecution(remoteFilePath,importHuoYue);
        return TmoocResult.ok(jobExecution.getExecutionContext().size());
    }

    private JobExecution getJobExecution(String remoteFilePath,Job job) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        String localFilePath = "E:\\test\\";//本地目录
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("remoteFilePath", remoteFilePath)
                .addString("localFilePath", localFilePath).toJobParameters();
        return jobLauncher.run(job, jobParameters);
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
}