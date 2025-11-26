package com.boscostay.searchservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class SearchserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchserviceApplication.class, args);
    }
}
