package com.tmooc.work.service.impl;

import com.tmooc.work.DTO.Mail;
import com.tmooc.work.entity.Note;
import com.tmooc.work.service.MailService;
import com.tmooc.work.service.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.ServletWebRequest;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private ThymeleafService thymeleafService;
    @Override
    public void sendSimpleMail(Mail mail) {
        sender.send(mail);
    }

    @Override
    public void sendTemplateMail(Mail mail, ServletWebRequest request, Model model,String templateName) {
        final String html = thymeleafService.process(request, model, templateName);
        MimeMessage mimeMessage=null;
        try {
            mimeMessage=sender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject());
            helper.setText(html,true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        sender.send(mimeMessage);
    }
}
