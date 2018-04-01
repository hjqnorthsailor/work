package com.tmooc.work.config;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.*;
@Configuration
@Slf4j
public class DruidConfig {
   @Autowired
    private DataSourceProperties dataSourceProperties;
    @Bean
    @Primary
    /**
     * 配置心得：不但要设置库、表的编码，还要设置字段的编码
     */
    public DataSource dataSource(){
        DruidDataSource druidDataSource=new DruidDataSource();
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        //设置utf8mb4
        String connectionInitSqls = "SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci";
        StringTokenizer tokenizer = new StringTokenizer(connectionInitSqls, ";");
        druidDataSource.setConnectionInitSqls(Collections.list(tokenizer));//重点设置该参数
        BeanUtils.copyProperties(dataSourceProperties,druidDataSource);
        log.info(druidDataSource.getConnectionInitSqls().toString());
        return druidDataSource;
    }
}
