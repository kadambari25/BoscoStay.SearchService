package com.boscostay.searchservice.controller;

import com.boscostay.searchservice.config.RabbitMQConfig;
import com.boscostay.searchservice.model.Apartment;
import com.boscostay.searchservice.service.AvailabilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.format.annotation.DateTimeFormat;
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

        /**
         * 1) RabbitMQ TEST endpoint
         * This is ONLY to prove RabbitMQ is working.
         * Swagger will ALWAYS show a response.
         */
        @GetMapping("/test-message")
        public ResponseEntity<String> sendTestMessage() {
                String message = "hello from search service";

                rabbitTemplate.convertAndSend(
                                RabbitMQConfig.SEARCH_BOOKING_QUEUE,
                                message);

                logger.info("Sent test message to RabbitMQ: {}", message);

                return ResponseEntity.ok(
                                "RabbitMQ test successful. Message sent: " + message);
        }

        /**
         * 2) Search available apartments
         */
        @GetMapping("/available")
        public ResponseEntity<List<Apartment>> searchAvailableApartments(
                        @RequestParam("city") String address,
                        @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                        @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

                if (checkIn.isAfter(checkOut)) {
                        return ResponseEntity.badRequest().build();
                }

                List<Apartment> available = availabilityService.searchAvailableApartments(
                                address, checkIn, checkOut);

                return ResponseEntity.ok(available);
        }
}
