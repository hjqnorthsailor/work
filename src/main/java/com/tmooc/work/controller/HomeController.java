package com.tmooc.work.controller;


import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.entity.Tab;
import com.tmooc.work.service.RedisService;
import com.tmooc.work.service.TabService;
import com.tmooc.work.service.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private TabService tabService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafService thymeleafService;
    @RequestMapping("/{root}/{index}")
    public String toPage(@PathVariable String root,@PathVariable String index){
        return "/"+root+"/"+index;
    }
    @RequestMapping(value = "/to_starter",produces = "text/html")
    @ResponseBody
    public String showAllTab(ServletWebRequest request,Model model){
//        if (redisService.exists(TabKey.tabListKey,"")){
//            System.out.println("存在");
//            final String html = redisService.get(TabKey.tabListKey, "", new TypeReference<String>() {
//            });
//            if (!StringUtils.isEmpty(html)) {
//                return html;
//            }
//        }
        System.out.println("不存在");
        final List<Tab> tabList = tabService.findAll();
        model.addAttribute("tabList",tabList);
        final String html = thymeleafService.process(request, model, "starter");
//        redisService.set(TabKey.tabListKey,"",html);
        return html;
    }
    @RequestMapping(value = "/tab/findAll")
    @ResponseBody
    public TmoocResult findAllTab(){

        final List<Tab> tabList = tabService.findAll();
        System.out.println(tabList.size());

        return TmoocResult.ok(tabList);
    }
}
