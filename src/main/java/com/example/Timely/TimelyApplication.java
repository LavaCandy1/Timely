package com.example.Timely;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TimelyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimelyApplication.class, args);
	}

}
