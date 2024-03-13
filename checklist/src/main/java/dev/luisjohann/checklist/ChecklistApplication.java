package dev.luisjohann.checklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ChecklistApplication {

	public static void main(String[] args) {
		log.info("*** START PROJECT ***");
		SpringApplication.run(ChecklistApplication.class, args);
	}
}
