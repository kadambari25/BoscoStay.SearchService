package com.boscostay.searchservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SEARCH_BOOKING_QUEUE = "booking.search.queue";
    public static final String SEARCH_REQUEST_QUEUE = "search.apartment.change";

    @Bean
    public Queue searchBookingQueue() {
        return new Queue(SEARCH_BOOKING_QUEUE, true, false, false);
    }

    @Bean
    public Queue searchRequestQueue() {
        return new Queue(SEARCH_REQUEST_QUEUE, false, false, true);
    }
}
