package com.boscostay.searchservice.controller;

import com.boscostay.searchservice.config.RabbitMQConfig;
import com.boscostay.searchservice.model.Apartment;
import com.boscostay.searchservice.service.AvailabilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    private final RabbitTemplate rabbitTemplate;
    private final AvailabilityService availabilityService;

    public SearchController(RabbitTemplate rabbitTemplate,
                            AvailabilityService availabilityService) {
        this.rabbitTemplate = rabbitTemplate;
        this.availabilityService = availabilityService;
    }

    // 1) RabbitMQ test endpoint
    @GetMapping("/test-message")
    public ResponseEntity<String> sendTestMessage() {
        String message = "hello from search service";

        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.SEARCH_TEST_QUEUE, message);
            logger.info("Sent test message to RabbitMQ: {}", message);
            return ResponseEntity.ok("Message sent: " + message);
        } catch (AmqpException ex) {
            logger.error("Failed to send test message to RabbitMQ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send message to RabbitMQ: " + ex.getMessage());
        }
    }

    // 2) Search available apartments
    @GetMapping("/available")
    public ResponseEntity<List<Apartment>> searchAvailableApartments(
            @RequestParam("city") String city,
            @RequestParam("checkIn")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam("checkOut")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        if (checkIn.isAfter(checkOut)) {
            return ResponseEntity.badRequest().build();
        }

        List<Apartment> available =
                availabilityService.searchAvailableApartments(city, checkIn, checkOut);

        // Fire-and-forget info message to RabbitMQ
        try {
            String mqMessage = String.format(
                    "search city=%s from=%s to=%s", city, checkIn, checkOut);
            rabbitTemplate.convertAndSend(RabbitMQConfig.SEARCH_REQUEST_QUEUE, mqMessage);
            logger.info("Sent search request message to RabbitMQ: {}", mqMessage);
        } catch (AmqpException ex) {
            logger.error("Failed to send search request message to RabbitMQ", ex);
        }

        return ResponseEntity.ok(available);
    }
}
