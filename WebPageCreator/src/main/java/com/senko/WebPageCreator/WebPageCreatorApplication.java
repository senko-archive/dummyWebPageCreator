package com.senko.WebPageCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebPageCreatorApplication implements CommandLineRunner {

	@Autowired
	Runner runner;
	
	public static void main(String[] args) {
		SpringApplication.run(WebPageCreatorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Start your program here
		
		runner.start();
		
	}

}
