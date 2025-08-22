package com.example.DrumkitTagger;

import org.springframework.boot.SpringApplication;

public class TestDrumkitTaggerApplication {

	public static void main(String[] args) {
		SpringApplication.from(DrumkitTaggerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
