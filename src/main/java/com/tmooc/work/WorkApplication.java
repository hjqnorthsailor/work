package com.tmooc.work;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
@EnableJpaRepositories(basePackages = "com.tmooc.work.dao")
@EntityScan(basePackages = "com.tmooc.work.entity")
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@EnableAsync(proxyTargetClass = true)
public class WorkApplication {
	public static void main(String[] args) {
		SpringApplication.run(WorkApplication.class, args);
	}
}
