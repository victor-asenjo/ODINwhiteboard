package edu.upc.essi.dtim.odin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OdinApplication {

	public static void main(String[] args) {
		SpringApplication.run(OdinApplication.class, args);
		
		System.out.println("Hello World!");
	}

}
