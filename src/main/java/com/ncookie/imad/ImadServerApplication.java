package com.ncookie.imad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ImadServerApplication {

	public static void main(String[] args) {
		// TODO: 자식 테이블들에 @OnDelete 옵션 추가해야 함
		SpringApplication.run(ImadServerApplication.class, args);
	}
}
