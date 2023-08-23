package com.ncookie.imad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ImadServerApplication {

	public static void main(String[] args) {
		// TODO: 모든 Entity의 필드를 래퍼 클래스 타입으로 변환해야 함 (int, long -> Integer, Long)
		// null 값을 할당할 수 있게 해야 함
		SpringApplication.run(ImadServerApplication.class, args);
	}
}
