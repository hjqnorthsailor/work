package com.tmooc.work.service;

import com.tmooc.work.entity.Mail;
import com.tmooc.work.entity.User;
import org.springframework.ui.Model;
import org.springframework.web.context.request.ServletWebRequest;

import javax.mail.MessagingException;

public interface MailService {

    void sendSimpleMail(Mail mail, User user, String emailPwd);

    void sendTemplateMail(Mail mail, ServletWebRequest request, Model model, String templateName, User user, String emailPwd) throws MessagingException;
}
