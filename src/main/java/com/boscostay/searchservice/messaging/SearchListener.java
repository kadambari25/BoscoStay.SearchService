package com.boscostay.searchservice.messaging;

import com.boscostay.searchservice.config.RabbitMQConfig;
import com.boscostay.searchservice.model.Apartment;
import com.boscostay.searchservice.model.ApartmentChangeEnum;
import com.boscostay.searchservice.model.Booking;
import com.boscostay.searchservice.model.BookingChangeEnum;
import com.boscostay.searchservice.repository.ApartmentRepository;
import com.boscostay.searchservice.repository.BookingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

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

    @Autowired
    private BookingRepository _bookingRepo;

    // Listener for booking changes
    @RabbitListener(queues = RabbitMQConfig.SEARCH_BOOKING_QUEUE)
    public void handleBookingChangeEvent(String message) throws JsonMappingException, JsonProcessingException {
        logger.info("[BOOKING] Received booking event message: {}", message);
        BookingChangeEvent event = mapper.readValue(message, BookingChangeEvent.class);
        String bookingId = event.getBookingId();
        BookingChangeEnum eventEnum = event.getEventEnum();
        UUID apartmentId = event.getApartmentId();
        LocalDate startDate = event.getStartDate();
        LocalDate endDate = event.getEndDate();
        switch (eventEnum) {
            case BOOKING_CREATED:
            case BOOKING_UPDATED:
                HandleBookingInRepository(bookingId, apartmentId, startDate, endDate);
                break;
            case BOOKING_DELETED:
                deleteBookingInRepository(bookingId, apartmentId);
                break;
            default:
                break;
        }
    }

    private void HandleBookingInRepository(String bookingId, UUID apartmentId, LocalDate startDate, LocalDate endDate) {
        java.util.Optional<Booking> existingBooking = _bookingRepo.findByBookingId(bookingId);
        if (existingBooking.isPresent()) {
            logger.info("[BOOKING] Booking {} - Apartment {} Updating existing booking", bookingId, apartmentId);
            Booking booking = existingBooking.get();
            booking.setApartmentId(apartmentId);
            booking.setCheckInDate(startDate);
            booking.setCheckOutDate(endDate);
            _bookingRepo.save(booking);
        } else {
            logger.info("[BOOKING] Booking {} - Apartment {} Creating new booking", bookingId, apartmentId);
            Booking booking = new Booking();
            booking.setBookingId(bookingId);
            booking.setApartmentId(apartmentId);
            booking.setCheckInDate(startDate);
            booking.setCheckOutDate(endDate);
            _bookingRepo.save(booking);
        }
    }

    private void deleteBookingInRepository(String bookingId, UUID apartmentId) {
        java.util.Optional<Booking> existingBooking = _bookingRepo.findByBookingId(bookingId);
        if (existingBooking.isPresent()) {
            _bookingRepo.delete(existingBooking.get());
            return;
        }
        logger.info("[BOOKING] Booking {} - Apartment {} not found", bookingId, apartmentId);
    }

    // Listener for apartment changes
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

    @Data
    static class BookingChangeEvent {
        private String bookingId;
        private UUID apartmentId;
        private LocalDate startDate;
        private LocalDate endDate;
        private BookingChangeEnum event;

        public String getBookingId() {
            return bookingId;
        }

        public void setBookingId(String bookingId) {
            this.bookingId = bookingId;
        }

        public BookingChangeEnum getEventEnum() {
            return event;
        }
    }
}
