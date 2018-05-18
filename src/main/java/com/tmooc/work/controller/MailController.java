package com.tmooc.work.controller;

import com.tmooc.work.DTO.NoteDTO;
import com.tmooc.work.config.MyProperties;
import com.tmooc.work.entity.Mail;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.User;
import com.tmooc.work.service.MailService;
import com.tmooc.work.util.FastDFSClientWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;
    @Autowired
    private MyProperties myProperties;
    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;

    @RequestMapping("/send")
    public TmoocResult sendEmail(NoteDTO note, ServletWebRequest request, Model model, User user) {
        Mail mail = new Mail();
        mail.setUser(user.getName());
        if (StringUtils.isEmpty(user.getEmail())) {
            return TmoocResult.error("你还没设置邮箱呢，赶快去设置邮箱");
        }
        mail.setFrom(user.getEmail());
        mail.setTo("baojq@tedu.cn");
        mail.setCc(new String[]{"jintao@tedu.cn", "tanxue@tedu.cn", "xuzj@tedu.cn", "hjqsmu@163.com"});
        mail.setReplyTo(user.getEmail());
        mail.setSentDate(new Date());
        mail.setSubject("TMOOC教研部-" + user.getName() + "—本周工作总结及下周工作计划");
        model.addAttribute("note", note);
        String fileName = user.getUsername() + "_" + note.getMonth() + "_" + note.getWeek() + ".xlsx";
        //设置临时文件存储在/用户文件夹下
        String userNotePath = myProperties.getExcelTemplatePath() + user.getUsername();
        String filePath = userNotePath + "/" + fileName;
        mail.setFilePath(filePath);
        FileInputStream fileInputStream;
        String s;
        try {
            fileInputStream = new FileInputStream(new File(filePath));
            s = fastDFSClientWrapper.uploadFile(new MockMultipartFile(fileName, fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileInputStream));

        } catch (FileNotFoundException e) {
            return TmoocResult.error("附件不存在,先下载确认无误后再发送");
        } catch (IOException e) {
            return TmoocResult.error("上传错误");
        }
        model.addAttribute("filePath", s);
        model.addAttribute("user", user);
        String templateName = "noteMail";
        try {
            mailService.sendTemplateMail(mail, request, model, templateName, user, note.getEmailPwd());
        } catch (MessagingException e) {
            return TmoocResult.error("邮件发送失败");
        }
        return TmoocResult.ok();
    }
}
