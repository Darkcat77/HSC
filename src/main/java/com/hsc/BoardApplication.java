package com.hsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		System.setProperty("org.apache.catalina.connector.RECYCLE_FACADES", "true");
		SpringApplication.run(BoardApplication.class, args);
	}

}
