package org.fsociety.identityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class ApplicationInitializer {

    public static void main(String[] arguments) {
        SpringApplication.run(ApplicationInitializer.class, arguments);
    }
}
