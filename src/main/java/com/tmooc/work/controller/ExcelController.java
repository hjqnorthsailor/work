package com.tmooc.work.controller;

import java.io.IOException;
import java.util.*;

import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.Reach;
import com.tmooc.work.service.ReachService;
import com.tmooc.work.util.FastDFSClientWrapper;
import com.tmooc.work.util.MyExcelUtils;
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
    @Autowired
    private ReachService reachService;
    @RequestMapping("/upload")
    @ResponseBody
    public TmoocResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        final String filePath = fastDFSClientWrapper.uploadFile(file);
        System.out.println(filePath);
        return TmoocResult.ok(filePath);
    }


    @RequestMapping("/importStudent")
    @ResponseBody
    public TmoocResult importStudent(@RequestParam("remoteFilePath") String remoteFilePath) throws IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobExecution jobExecution = getJobExecution(remoteFilePath,importStudent);
        return TmoocResult.ok(jobExecution.getExecutionContext().size());
    }

    @RequestMapping("/importHuoYue")
    @ResponseBody
    public TmoocResult importHuoYue(@RequestParam("remoteFilePath") String remoteFilePath) throws IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
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
    public void export(HttpServletResponse response,Integer month,Integer week) {
        //需要导出的数据
        List<Reach> reachList = reachService.queryReachRate(month, week);
        //导出操作
        MyExcelUtils.exportExcel(reachList, ""+month+"月"+week+"周达到率", ""+month+"月"+week+"周达到率", Reach.class, month+"月"+week+"周达到率.xls", response);
    }
}