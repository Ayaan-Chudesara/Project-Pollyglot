package com.pollyglot.pollyglot_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PollyglotBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollyglotBackendApplication.class, args);
	}


	// Bean for RestTemplate to make HTTP requests
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// Bean for ObjectMapper to handle JSON serialization/deserialization
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
