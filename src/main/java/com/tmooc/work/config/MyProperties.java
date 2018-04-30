package com.tmooc.work.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix ="my.properties" )
@Data
public class MyProperties {
    private String excelTemplatePath="/Users/northsailor/Downloads/work/src/main/resources/templates/";
    private String localFilePath="/Users/northsailor/Downloads/work/src/main/resources/templates/test/";;
}
