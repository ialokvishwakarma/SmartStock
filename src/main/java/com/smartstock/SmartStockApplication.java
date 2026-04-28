package com.smartstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartStockApplication {


	public static void main(String[] args) {
		SpringApplication.run(SmartStockApplication.class, args);
		System.out.println("SmartStock Application started successfully.");
	}

}
