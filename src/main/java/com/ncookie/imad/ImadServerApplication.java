package com.ncookie.imad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.ncookie.imad.domain")
@SpringBootApplication
public class ImadServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ImadServerApplication.class, args);
	}
}
