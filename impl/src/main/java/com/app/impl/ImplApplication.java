package com.app.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

// TODO: Логирование

@SpringBootApplication
@EnableCaching
public class ImplApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImplApplication.class, args);
	}

}
