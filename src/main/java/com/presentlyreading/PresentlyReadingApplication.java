package com.presentlyreading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Presently Reading Spring Boot REST API.
 *
 * <p>Runs on port 8082 (see application.properties).
 * SQLite database file: {@code presentlyreading.db} (created automatically on first run).
 */
@SpringBootApplication
public class PresentlyReadingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PresentlyReadingApplication.class, args);
    }
}
