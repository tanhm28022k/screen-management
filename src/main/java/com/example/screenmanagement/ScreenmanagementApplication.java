package com.example.screenmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScreenmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmanagementApplication.class, args);
	}

}
