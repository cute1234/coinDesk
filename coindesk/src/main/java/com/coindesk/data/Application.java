package com.coindesk.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	// http://localhost:8080/h2-console/
	/*
	 CREATE TABLE CURRENCY (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    CODE VARCHAR(255),
    CHINESE_NAME VARCHAR(255),
    SYMBOL VARCHAR(255),
    RATE VARCHAR(255),
    RATE_FLOAT VARCHAR(255),
    DESCRIPTION VARCHAR(255),
    UPDATE_TIME VARCHAR(255)
);

	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
