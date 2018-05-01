package com.tmooc.work.controller;

import com.tmooc.work.entity.User;
import lombok.extern.java.Log;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
        String encryPwd=encry(username,password);//shiro有自身的加密的机制，这里实验几次没有成功，因此自己进行加密
        System.out.println(encryPwd);
        UsernamePasswordToken token=new UsernamePasswordToken(username,encryPwd);
        try {
            subject.login(token);
        } catch (Exception e) {
        log.info("验证失败");
        return "redirect:/login";
        }
        return "redirect:/index";
    }
    @GetMapping("/logout")
    public void loginOut() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
    private static String encry(String username,String password) {
        ByteSource salt = ByteSource.Util.bytes(username);
        String simpleHash=new SimpleHash("md5",password,salt,2).toHex();
        System.out.println(simpleHash);
        return simpleHash;
    }

    public static void main(String[] args) {
        System.out.println(encry("jintao","123456"));
        System.out.println(encry("huangjq","123456"));
        System.out.println(encry("mengbo","123456"));
        System.out.println(encry("baojq","123456"));
        System.out.println(encry("liuxy","123456"));
    }
}

