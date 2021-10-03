package com.s1dmlgus.instagram02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Instagram02Application {

	public static void main(String[] args) {
		SpringApplication.run(Instagram02Application.class, args);
	}

}
