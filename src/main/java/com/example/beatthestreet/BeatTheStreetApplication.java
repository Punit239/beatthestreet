package com.example.beatthestreet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
public class BeatTheStreetApplication {

	public static final Logger logger = LoggerFactory.getLogger(BeatTheStreetApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(BeatTheStreetApplication.class, args);
	}
}
