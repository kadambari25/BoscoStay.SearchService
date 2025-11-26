package com.boscostay.searchservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // Queue for simple string test messages
    public static final String SEARCH_QUEUE = "search.queue";

    // NEW: Queue for actual search request payloads
    public static final String SEARCH_REQUEST_QUEUE = "search.request.queue";

    @Bean
    public Queue searchQueue() {
        return new Queue(SEARCH_QUEUE, true);
    }

    @Bean
    public Queue searchRequestQueue() {
        return new Queue(SEARCH_REQUEST_QUEUE, true);
    }
}
