package com.tmooc.work.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

import com.tmooc.work.batch.HuoYueTask;
import com.tmooc.work.batch.StudentTask;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.config.MyProperties;
import com.tmooc.work.entity.HuoYue;
import com.tmooc.work.entity.Reach;
import com.tmooc.work.entity.Student;
import com.tmooc.work.service.HuoYueService;
import com.tmooc.work.service.ReachService;
import com.tmooc.work.service.StudentService;
import com.tmooc.work.util.FastDFSClientWrapper;
import com.tmooc.work.util.MyExcelUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.http.fileupload.FileUpload;
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
    private ReachService reachService;
    @Autowired
    private HuoYueService huoYueService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private MyProperties myProperties;//自定义属性

    /**
     * 上传excel
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    @ResponseBody
    public TmoocResult upload(@RequestParam("file") MultipartFile file) throws IOException {
//        final String filePath = fastDFSClientWrapper.uploadFile(file);
        String filePath=myProperties.getLocalFilePath()+file.getOriginalFilename();
        file.transferTo(new File(myProperties.getLocalFilePath()+file.getOriginalFilename()));
//        System.out.println(filePath);
        return TmoocResult.ok(filePath);
    }

    /**
     * 导入达到率数据到数据库
     * @param remoteFilePath
     * @return
     * @throws Exception
     */
    @RequestMapping("/importHuoYue")
    @ResponseBody
    public TmoocResult importHuoYue(@RequestParam("remoteFilePath") String remoteFilePath) {
        List<HuoYue> huoYueList =MyExcelUtils.importExcel(remoteFilePath,0,1,HuoYue.class);
        ForkJoinPool pool=new ForkJoinPool();
        HuoYueTask task=new HuoYueTask(huoYueList,huoYueService);
        pool.submit(task);
        return TmoocResult.ok();
    }

    /**
     * 导入学生数据到数据库
     * @param remoteFilePath
     * @return
     */
    @RequestMapping("/importStudent")
    @ResponseBody
    public TmoocResult importStudent(@RequestParam("remoteFilePath") String remoteFilePath) {
        List<Student> studentList = MyExcelUtils.importExcel(remoteFilePath, 0, 1, Student.class);
        MyExcelUtils.importExcel(remoteFilePath, 0, 1, Student.class);
        ForkJoinPool pool=new ForkJoinPool();
        StudentTask task=new StudentTask(studentList,studentService);
        pool.submit(task);
        return TmoocResult.ok();
    }

    /**
     * 导出达到率
     * @param response
     * @param month
     * @param week
     */
    @RequestMapping("/export")
    public void export(HttpServletResponse response,Integer month,Integer week) {
        //需要导出的数据
        List<Reach> reachList = reachService.queryReachRate(month, week);
        //导出操作
        MyExcelUtils.exportExcel(reachList, ""+month+"月"+week+"周达到率", ""+month+"月"+week+"周达到率", Reach.class, month+"月"+week+"周达到率.xls", response);
    }

    /**
     * 上传到fastdfs并在本地存备份
     * @param remoteFilePath
     * @param localFilePath
     * @return
     * @throws IOException
     */
    public List<HuoYue> init(String remoteFilePath, String localFilePath) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String filePath = localFilePath + remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);//本地文件名
        boolean isDownload = fastDFSClientWrapper.executeDownloadFile(httpClient, remoteFilePath, filePath, "UTF-8", true);
        if (isDownload) {
            return MyExcelUtils.importExcel(filePath, 0, 1, HuoYue.class);
        }
        return  null;
    }
}