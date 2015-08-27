package com.calamp.connect.messageprocessor.web.config;

import javax.jms.ConnectionFactory;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.calamp.connect.messageprocessor.Constants;

@SpringBootApplication
@EnableScheduling
@EnableJms
@ComponentScan(basePackages = { Constants.rootPackageName })
public class Application extends SpringBootServletInitializer {

    private static final Logger log = Logger.getLogger(Application.class.getName());

    @Bean
    JmsListenerContainerFactory<?> JmSqsContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setSessionTransacted(false); // SQS Does not Support Transacted.
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

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