package com.tmooc.work.config;

import com.tmooc.work.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
@Configuration
public class UserAuditorAware implements AuditorAware<String> {
    @Override
    public String getCurrentAuditor() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user.getUsername();
    }
}
