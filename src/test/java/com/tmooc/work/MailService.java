package com.tmooc.work;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailService extends WorkApplicationTests {
    @Autowired
    private JavaMailSender javaMailSender;
    @Test
    public void send(){
        SimpleMailMessage mailMessage=new SimpleMailMessage();

        mailMessage.setFrom("1045404308@qq.com");
        mailMessage.setTo("hjqsmu@163.com");
        mailMessage.setSubject("test");
        mailMessage.setText("test");
        javaMailSender.send(mailMessage);
    }
}
