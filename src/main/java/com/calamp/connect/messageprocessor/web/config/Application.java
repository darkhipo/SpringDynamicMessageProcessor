package com.calamp.connect.messageprocessor.web.config;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.calamp.connect.messageprocessor.Constants;

@SpringBootApplication
@EnableScheduling
@ComponentScan( basePackages = { Constants.rootPackageName } )
public class Application extends SpringBootServletInitializer {

    private static final Logger log = Logger.getLogger(Application.class.getName());

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder;
    }

    public static void main(String[] args) throws InterruptedException {
        log.info("Main screen turn on!");
        SpringApplication.run(Application.class, args);
        log.info("For great justice!");
    }
}