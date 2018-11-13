package com.leadiq.imguruploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ImguruploaderWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImguruploaderWebApplication.class, args);
	}
}
