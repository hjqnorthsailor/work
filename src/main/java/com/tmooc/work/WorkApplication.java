package com.tmooc.work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
@EnableJpaRepositories(basePackages = "com.tmooc.work.dao")
@EntityScan(basePackages = "com.tmooc.work.entity")
public class WorkApplication {
	public static void main(String[] args) {
		SpringApplication.run(WorkApplication.class, args);
	}
}
