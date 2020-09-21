package com.github.fabiosoaza.salesimporter.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.github.fabiosoaza.salesimporter")
public class SalesImporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesImporterApplication.class, args);
	}

}
	