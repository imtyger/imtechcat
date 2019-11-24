package com.imtyger.imtygerbed;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan("com.imtyger.imtygerbed.mapper")
public class TygerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TygerApplication.class, args);
		
	}


}
