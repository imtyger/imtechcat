package com.imtyger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author imtygerx@gmail.com
 * @Date 2019/7/8 11:03
 */

@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan("com.imtyger.imtygerbed.mapper")
public class ImTygerBedApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImTygerBedApplication.class, args);
	}
}
