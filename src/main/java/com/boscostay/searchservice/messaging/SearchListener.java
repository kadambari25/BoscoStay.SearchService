package com.boscostay.searchservice.messaging;

import com.boscostay.searchservice.config.RabbitMQConfig;
import com.boscostay.searchservice.model.Apartment;
import com.boscostay.searchservice.model.ApartmentChangeEnum;
import com.boscostay.searchservice.repository.ApartmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchListener {

    private static final Logger logger = LoggerFactory.getLogger(SearchListener.class);
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

    @Autowired
    private ApartmentRepository _apartmentRepo;
    // // Listener for /test-message
    // @RabbitListener(queues = RabbitMQConfig.SEARCH_TEST_QUEUE)
    // public void handleTestMessage(String message) {
    // logger.info("[TEST] Received message from test queue: {}", message);
    // }

    // Listener for /available search requests
    @RabbitListener(queues = RabbitMQConfig.SEARCH_REQUEST_QUEUE)
    public void handleSearchRequest(String message) throws JsonMappingException, JsonProcessingException {
        logger.info("[SEARCH] Received search request message: {}", message);
        SearchRequest request = mapper.readValue(message, SearchRequest.class);
        Apartment apartment = request.getApartment();
        ApartmentChangeEnum apartmentChange = request.getApartmentChange();

        switch (apartmentChange) {
            case Create:
            case Update:
                _apartmentRepo.save(apartment);
                break;
            case Delete:
                _apartmentRepo.deleteById(apartment.getId());
                break;
        }
    }

    @Data
    static class SearchRequest {
        private Apartment apartment;
        private ApartmentChangeEnum apartmentChange;

        public Apartment getApartment() {
            return apartment;
        }

        public ApartmentChangeEnum getApartmentChange() {
            return apartmentChange;
        }
    }
}
