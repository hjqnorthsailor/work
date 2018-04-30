package com.tmooc.work.controller;


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

    @GetMapping("/login")
    public String toLogin() {
        System.out.println("login");
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
        System.out.println("欢迎登陆" + user.getUsername());
        String html;
//        if (redisService.exists(TabKey.tabListKey, index)) {
//            html = redisService.get(TabKey.tabListKey, index, new TypeReference<String>() {
//            });
//            log.info("使用了缓存"+index);
//            return html;
//        }
        final List<Tab> tabList = tabService.findAll();
        if (!StringUtils.isEmpty(index)) {
            if (index.equals("index")) {
                index = "home";
            }
            model.addAttribute("title", index);
        }
        model.addAttribute("tabList", tabList);
        html = thymeleafService.process(request, model, "index");
        redisService.set(TabKey.tabListKey, index, html);
//        log.info("更新了缓存"+index);
        return html;
    }

    /**
     * 查询所有标签
     * @return
     */
    @RequestMapping(value = "/tab/findAll")
    @ResponseBody
    public TmoocResult findAllTab() {

        final List<Tab> tabList = tabService.findAll();
        System.out.println(tabList.size());

        return TmoocResult.ok(tabList);
    }
}
