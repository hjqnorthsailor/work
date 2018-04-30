package com.tmooc.work.controller;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

import com.tmooc.work.batch.HuoYueTask;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.dao.HuoYueDao;
import com.tmooc.work.entity.HuoYue;
import com.tmooc.work.entity.Reach;
import com.tmooc.work.service.ReachService;
import com.tmooc.work.util.FastDFSClientWrapper;
import com.tmooc.work.util.MyExcelUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
    private HuoYueDao huoYueDao;
    @RequestMapping("/upload")
    @ResponseBody
    public TmoocResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        final String filePath = fastDFSClientWrapper.uploadFile(file);
        System.out.println(filePath);
        return TmoocResult.ok(filePath);
    }

    @RequestMapping("/importHuoYue")
    @ResponseBody
    public TmoocResult importHuoYue(@RequestParam("remoteFilePath") String remoteFilePath) throws Exception{
        String localFilePath = "/Users/northsailor/Downloads/";//本地目录
        List<HuoYue> huoYueList = init(remoteFilePath, localFilePath);
        System.out.println(huoYueList.size());
        ForkJoinPool pool=new ForkJoinPool();
        HuoYueTask task=new HuoYueTask(huoYueList,huoYueDao);
        pool.submit(task);
        return TmoocResult.ok();
    }
    @RequestMapping("/export")
    public void export(HttpServletResponse response,Integer month,Integer week) {
        //需要导出的数据
        List<Reach> reachList = reachService.queryReachRate(month, week);
        //导出操作
        MyExcelUtils.exportExcel(reachList, ""+month+"月"+week+"周达到率", ""+month+"月"+week+"周达到率", Reach.class, month+"月"+week+"周达到率.xls", response);
    }
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