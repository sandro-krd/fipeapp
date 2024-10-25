package com.example.fipeapp;

import com.example.fipeapp.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FipeappApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FipeappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal();
		principal.exibeMenu();

	}
}
