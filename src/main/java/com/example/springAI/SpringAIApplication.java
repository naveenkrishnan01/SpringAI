package com.example.springAI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class SpringAIApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAIApplication.class, args);
	}

}
