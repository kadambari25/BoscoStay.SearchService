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
public class SearchController {

        private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

        private final AvailabilityService availabilityService;

        public SearchController(AvailabilityService availabilityService) {
                this.availabilityService = availabilityService;
        }

        @GetMapping("/")
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
