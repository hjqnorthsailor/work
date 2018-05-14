package com.tmooc.work.controller;


import com.google.common.collect.Lists;
import com.tmooc.work.common.TabKey;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.Tab;
import com.tmooc.work.entity.User;
import com.tmooc.work.service.RedisService;
import com.tmooc.work.service.TabService;
import com.tmooc.work.service.ThymeleafService;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private TabService tabService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafService thymeleafService;
    @GetMapping("/")
    public String home() {
        return "login";
    }
    @GetMapping("/login")
    public String toLogin() {
        return "login";
    }

    /**
     * 跳转页面
     * @param index
     * @param request
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/{index}", produces = "text/html")
    @ResponseBody
    public String showPage(@PathVariable String index, ServletWebRequest request, Model model, User user) {
        List<Tab> tabList;
        if (redisService.exists(TabKey.tabListKey,"")){
            tabList=redisService.get(TabKey.tabListKey, "", new TypeReference<List<Tab>>() {
            });
        }else {
            tabList = tabService.findAll();
            redisService.set(TabKey.tabListKey,"",tabList);
            log.info("对tab进行了缓存");
        }
        if (!StringUtils.isEmpty(index)) {
            if (index.equals("index")) {
                index = "home";
            }
            model.addAttribute("title", index);
        }
        model.addAttribute("tabList", tabList);
        model.addAttribute("username",user.getName());
        model.addAttribute("email",user.getEmail());
        String html = thymeleafService.process(request, model, "index");
        return html;
    }

    /**
     * 查询所有标签
     * @return
     */
    @RequestMapping(value = "/tab/findAll")
    @ResponseBody
    public TmoocResult findAllTab() {
        List<Tab> tabList;
        if (redisService.exists(TabKey.tabListKey,"")){
            tabList=redisService.get(TabKey.tabListKey, "", new TypeReference<List<Tab>>() {
            });
        }else {
            tabList = tabService.findAll();
            redisService.set(TabKey.tabListKey,"",tabList);
        }
        return TmoocResult.ok(tabList);
    }
}
