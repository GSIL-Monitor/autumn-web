package org.autumn.commons.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AutumnWebSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutumnWebSpringBootApplication.class, args);
    }
}
