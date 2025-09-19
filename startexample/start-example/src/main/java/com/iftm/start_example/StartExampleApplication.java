package com.iftm.start_example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class StartExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartExampleApplication.class, args);
	}

}
