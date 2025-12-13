package com.boscostay.searchservice.messaging;

import com.boscostay.searchservice.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SearchListener {

    private static final Logger logger = LoggerFactory.getLogger(SearchListener.class);

    // Listener for /test-message
    @RabbitListener(queues = RabbitMQConfig.SEARCH_TEST_QUEUE)
    public void handleTestMessage(String message) {
        logger.info("[TEST] Received message from test queue: {}", message);
    }

    // Listener for /available search requests
    @RabbitListener(queues = RabbitMQConfig.SEARCH_REQUEST_QUEUE)
    public void handleSearchRequest(String message) {
        logger.info("[SEARCH] Received search request message: {}", message);
    }
}
