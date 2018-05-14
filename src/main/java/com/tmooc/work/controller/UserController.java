package com.tmooc.work.controller;

import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.dao.UserDao;
import com.tmooc.work.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDao userDao;
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
    @PostMapping("/update")
    @ResponseBody
    public TmoocResult updateUser(@RequestParam(value = "email",required =false)String email,
                                  @RequestParam(value = "name",required =false)String name,
                                  @RequestParam(value = "newPwd",required =false)String newPwd, User user){
        if (StringUtils.isNotEmpty(email.trim())){
            user.setEmail(email);
        }
        if (StringUtils.isNotEmpty(name.trim())){
            user.setName(name);
        }
        if (StringUtils.isNotEmpty(newPwd.trim())){
            user.setPassword(encry(user.getUsername(),newPwd));
        }
        final User u = userDao.saveAndFlush(user);
        if (u==null){
            return TmoocResult.error("用户信息修改失败");
        }
        return TmoocResult.ok();
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

