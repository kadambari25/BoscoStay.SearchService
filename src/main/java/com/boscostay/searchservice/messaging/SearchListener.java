package com.boscostay.searchservice.messaging;

import com.boscostay.searchservice.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SearchListener {

    private static final Logger log = LoggerFactory.getLogger(SearchListener.class);

    public SearchListener() {
        log.info("🔧 SearchListener CONSTRUCTED");
    }

    // Listener for simple string test messages (from /test-message)
    @RabbitListener(queues = RabbitMQConfig.SEARCH_QUEUE)
    public void handleTestMessage(String message) {
        log.info("📩 [TEST] Received message from search queue: {}", message);
    }

    // Listener for real search requests (from the POST /api/search)
    @RabbitListener(queues = RabbitMQConfig.SEARCH_REQUEST_QUEUE)
    public void handleSearchRequest(SearchRequestMessage request) {
        log.info("📩 [SEARCH] Received search request: from={} to={}",
                 request.getFromDate(), request.getToDate());
    }
}
