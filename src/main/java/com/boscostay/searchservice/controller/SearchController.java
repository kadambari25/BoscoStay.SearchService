package com.boscostay.searchservice.controller;

import com.boscostay.searchservice.config.RabbitMQConfig;
import com.boscostay.searchservice.model.Apartment;
import com.boscostay.searchservice.service.AvailabilityService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final RabbitTemplate rabbitTemplate;
    private final AvailabilityService availabilityService;

    public SearchController(RabbitTemplate rabbitTemplate,
                            AvailabilityService availabilityService) {
        this.rabbitTemplate = rabbitTemplate;
        this.availabilityService = availabilityService;
    }

    // 1) RabbitMQ test endpoint (what you used with "hello-from-swagger")
    @GetMapping("/test-message")
    public ResponseEntity<String> sendTestMessage(
            @RequestParam(defaultValue = "hello from search service") String message) {

        rabbitTemplate.convertAndSend(RabbitMQConfig.SEARCH_QUEUE, message);
        return ResponseEntity.ok("Message sent: " + message);
    }

    // 2) MAIN SEARCH ENDPOINT (returns list of apartments)
    @GetMapping
    public ResponseEntity<List<Apartment>> searchApartments(
            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam("to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

        // use your existing AvailabilityService logic
        List<Apartment> results = availabilityService.findAvailableApartments(from, to);

        return ResponseEntity.ok(results);
    }
}
