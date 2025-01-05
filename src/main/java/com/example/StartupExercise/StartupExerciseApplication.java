package com.example.StartupExercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//@EnableCaching enable caching only in production , for testing keep this disabled
@SpringBootApplication
public class StartupExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartupExerciseApplication.class, args);
	}

}
