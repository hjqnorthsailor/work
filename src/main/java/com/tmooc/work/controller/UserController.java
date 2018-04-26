package com.tmooc.work.controller;

import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.User;
import lombok.extern.java.Log;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log
@RequestMapping("/user")
public class UserController {
    /**
     * 登录方法
     * @param
     * @return
     */
    @PostMapping("/login")
    public String login(String username,String password) {
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        try {
            subject.login(token);
            System.out.println(token.getUsername());
        } catch (Exception e) {
        log.info("验证失败");
        return "login";
        }
        return "redirect:/index";
    }
}

