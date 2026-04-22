package com.sky;

import org.springframework.boot.SpringApplication;

public class TestSkyPocApplication {

	public static void main(String[] args) {
		SpringApplication.from(SkyPocApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
