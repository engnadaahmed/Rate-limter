package com.example.RateLimter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RateLimterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimterApplication.class, args);
	}

}
