package com.boscostay.searchservice.service;

import com.boscostay.searchservice.model.Apartment;
import com.boscostay.searchservice.model.Booking;
import com.boscostay.searchservice.repository.ApartmentRepository;
import com.boscostay.searchservice.repository.BookingRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final ApartmentRepository apartmentRepository;
    private final BookingRepository bookingRepository;
    private static final Logger logger = LoggerFactory.getLogger(AvailabilityService.class);

    public AvailabilityService(ApartmentRepository apartmentRepository,
            BookingRepository bookingRepository) {
        this.apartmentRepository = apartmentRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Apartment> searchAvailableApartments(String address,
            LocalDate checkIn,
            LocalDate checkOut) {
        List<Apartment> apartments = apartmentRepository.findByAddressContainsText(address.toUpperCase());
        if (apartments.isEmpty()) {
            return Collections.emptyList();
        }

        logger.info("Found {} apartments for address {}", apartments.size(), address);
        apartments.stream().forEach(a -> logger.info("Fetched apartment id={}, name={}, address={}",
                a.getId(), a.getName(), a.getAddress()));

        List<UUID> apartmentIds = apartments.stream()
                .map(Apartment::getId)
                .collect(Collectors.toList());

        List<Booking> bookings = bookingRepository.findByApartmentIdIn(apartmentIds);

        logger.info("Fetched {} bookings", bookings.size());
        bookings.forEach(b -> logger.info("Fetched booking id={}, apartmentId={}, checkIn={}, checkOut={}",
                b.getBookingId(), b.getApartmentId(), b.getCheckInDate(), b.getCheckOutDate()));

        Map<UUID, List<Booking>> bookingsByApartment = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getApartmentId));

        return apartments.stream()
                .filter(a -> isApartmentAvailable(a.getId(), checkIn, checkOut, bookingsByApartment))
                .collect(Collectors.toList());
    }

    private boolean isApartmentAvailable(UUID apartmentId,
            LocalDate reqStart,
            LocalDate reqEnd,
            Map<UUID, List<Booking>> bookingsByApartment) {
        List<Booking> existingBookings = bookingsByApartment.getOrDefault(apartmentId, Collections.emptyList());

        for (Booking booking : existingBookings) {
            if (datesOverlap(reqStart, reqEnd,
                    booking.getCheckInDate(), booking.getCheckOutDate())) {
                logger.info("Apartment {} is not available due to existing booking id={}, checkIn={}, checkOut={}",
                        apartmentId, booking.getBookingId(), booking.getCheckInDate(), booking.getCheckOutDate());
                return false;
            }
        }
        logger.info("Apartment {} is available for dates {} to {}", apartmentId, reqStart, reqEnd);
        return true;
    }
    
    private boolean datesOverlap(LocalDate reqStart,
            LocalDate reqEnd,
            LocalDate existingStart,
            LocalDate existingEnd) {
        return reqStart.isBefore(existingEnd) && reqEnd.isAfter(existingStart);
    }
}
