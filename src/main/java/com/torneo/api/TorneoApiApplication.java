package com.torneo.api;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class TorneoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TorneoApiApplication.class, args);
		System.out.println("✅ Aplicación Spring Boot iniciada y corriendo...");
	}

	@Bean
	public ApplicationRunner runner(ApplicationContext ctx) {
		return args -> {
			System.out.println("==== BEANS REGISTRADOS ====");
			Arrays.stream(ctx.getBeanDefinitionNames()).forEach(System.out::println);
		};
	}

}

