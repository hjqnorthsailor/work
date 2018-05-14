package com.tmooc.work.service.impl;

import com.tmooc.work.entity.Mail;
import com.tmooc.work.entity.User;
import com.tmooc.work.service.MailService;
import com.tmooc.work.service.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.ServletWebRequest;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {
    //    @Autowired
//    private JavaMailSender sender;
    @Autowired
    private ThymeleafService thymeleafService;

    @Override
    public void sendSimpleMail(Mail mail, User user, String emailPwd) {
        JavaMailSender sender = javaMailSender(user, emailPwd);

        sender.send(mail);
    }

    private JavaMailSender javaMailSender(User user, String emailPwd) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("mail.tedu.cn");
        sender.setUsername(user.getEmail());
        sender.setPassword(emailPwd);
        sender.setDefaultEncoding("UTF-8");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");
        sender.setJavaMailProperties(properties);
        return sender;
    }

    @Override
    public void sendTemplateMail(Mail mail, ServletWebRequest request, Model model, String templateName, User user, String emailPwd) throws MessagingException {
        JavaMailSender sender = javaMailSender(user, emailPwd);
        final String html = thymeleafService.process(request, model, templateName);
        MimeMessage mimeMessage=sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setCc(mail.getCc());
        helper.setSentDate(mail.getSentDate());
        helper.setReplyTo(mail.getReplyTo());
        FileSystemResource resource = new FileSystemResource(mail.getFilePath());
        helper.addAttachment("周报.xlsx", resource);
        helper.setText(html, true);

        sender.send(mimeMessage);
    }
}
