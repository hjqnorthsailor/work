package com.tmooc.work.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.ServletWebRequest;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Service
public class ThymeleafService {
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private ApplicationContext applicationContext;
    public String process(ServletWebRequest request, Model model,String key) {
        SpringWebContext ctx = new SpringWebContext(request.getRequest(), request.getResponse(),
                request.getRequest().getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        //手动渲染
        System.out.println(ctx.getApplicationContext());
        return thymeleafViewResolver.getTemplateEngine().process(key, ctx);
    }
}
