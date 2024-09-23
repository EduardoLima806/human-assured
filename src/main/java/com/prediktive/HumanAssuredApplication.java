package com.prediktive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class HumanAssuredApplication {

	public static void main(String[] args) {
		SpringApplication.run(HumanAssuredApplication.class, args);
	}

}
