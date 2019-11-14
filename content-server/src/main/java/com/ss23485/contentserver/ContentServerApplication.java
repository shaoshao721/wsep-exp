package com.ss23485.contentserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContentServerApplication {

    public static void main(String[] args) {
        EnvProperties.initialize();
        SpringApplication.run(ContentServerApplication.class, args);
    }

}
