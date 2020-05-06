package com.larbotech.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class ShowcaseApp {

    public static void main(String[] args) {
        SpringApplication.run(ShowcaseApp.class, args);
    }

    private static final Logger logger = LoggerFactory.getLogger(ShowcaseApp.class);

    @PostConstruct
    public void postInit() {
        logger.info("Application ShowcaseApp started!");
    }
}
