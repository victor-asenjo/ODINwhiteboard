package edu.upc.essi.dtim.odin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:credentials.properties")
public class OdinApplication {

	public static void main(String[] args) {
		SpringApplication.run(OdinApplication.class, args);
		
		System.out.println("READY PARA RECOGER API REQUESTS");
	}

}
