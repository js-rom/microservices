package es.edu.escuela_it.Miroservices.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {

	@Value("${spring.application.name}")
	private String name;
	
	private int year;
	
	private String edition;
	
	private String[] countries;
	
}


