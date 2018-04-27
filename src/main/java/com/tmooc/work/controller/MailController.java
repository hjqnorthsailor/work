package com.tmooc.work.controller;

import com.tmooc.work.entity.Mail;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.Note;
import com.tmooc.work.entity.User;
import com.tmooc.work.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;
    @RequestMapping("send")
    public TmoocResult sendEmail(ServletWebRequest request, Model model, Note note, String filePath, User user){
        Mail mail=new Mail();
        mail.setUser(user.getUsername());
        mail.setFrom("1045404308@qq.com");
        mail.setTo("hjqsmu@163.com");
        mail.setSubject("test");
        model.addAttribute("note",note);
        model.addAttribute("filePath",filePath);
        String templateName="noteMail";
        mailService.sendTemplateMail(mail,request,model,templateName);
        return TmoocResult.ok();
    }
}
