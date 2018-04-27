package com.tmooc.work.service;

import com.tmooc.work.entity.Mail;
import org.springframework.ui.Model;
import org.springframework.web.context.request.ServletWebRequest;

public interface MailService {
    void sendSimpleMail(Mail mail);
    void sendTemplateMail(Mail mail, ServletWebRequest request, Model model, String templateName);
}
