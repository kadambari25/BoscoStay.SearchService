package com.boscostay.searchservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SEARCH_TEST_QUEUE = "search.test.queue";
    public static final String SEARCH_REQUEST_QUEUE = "search.apartment.change";

    // @Bean
    // public Queue searchTestQueue() {
    //     return new Queue(SEARCH_TEST_QUEUE, false);
    // }

    @Bean
    public Queue searchRequestQueue() {
       // return new Queue(SEARCH_REQUEST_QUEUE, false);
        return new Queue(SEARCH_REQUEST_QUEUE, false, false, true);
    }
}
