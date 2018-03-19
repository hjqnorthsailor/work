package com.tmooc.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/{index}")
    public String index(@PathVariable String index){
        return index;
    }
    @RequestMapping("/{root}/{index}")
    public String toPage(@PathVariable String root,@PathVariable String index){
        return "/"+root+"/"+index;
    }
}
