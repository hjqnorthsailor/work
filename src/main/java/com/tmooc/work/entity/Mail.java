package com.tmooc.work.entity;

import lombok.Data;
import org.springframework.mail.SimpleMailMessage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Mail extends SimpleMailMessage {
    /**
     * 主键ID自动生成策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    private String user;
    private String filePath;
}
