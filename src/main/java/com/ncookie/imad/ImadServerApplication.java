package com.ncookie.imad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ImadServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImadServerApplication.class, args);
	}
}
