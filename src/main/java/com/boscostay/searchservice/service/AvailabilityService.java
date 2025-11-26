package com.boscostay.searchservice.service;

import com.boscostay.searchservice.model.Apartment;
import com.boscostay.searchservice.model.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    // Demo apartments – later you can replace with real data from the apartments service
    private final List<Apartment> apartments = List.of(
            new Apartment(1L, "Cozy Studio", "Rome"),
            new Apartment(2L, "Sea View Apartment", "Naples"),
            new Apartment(3L, "City Center Loft", "Milan")
    );

    // Map: apartmentId -> list of bookings
    private final Map<Long, List<Booking>> bookingsByApartment = new ConcurrentHashMap<>();

    public void addBooking(Booking booking) {
        bookingsByApartment
                .computeIfAbsent(booking.getApartmentId(), id -> new ArrayList<>())
                .add(booking);
    }

    /**
     * Original method: finds available apartments between two dates.
     */
    public List<Apartment> findAvailable(LocalDate from, LocalDate to) {
        return apartments.stream()
                .filter(apartment -> isAvailable(apartment.getId(), from, to))
                .collect(Collectors.toList());
    }

    /**
     * New method used by SearchController.
     * It simply delegates to findAvailable(...) to keep the logic in one place.
     */
    public List<Apartment> findAvailableApartments(LocalDate fromDate, LocalDate toDate) {
        return findAvailable(fromDate, toDate);
    }

    private boolean isAvailable(Long apartmentId, LocalDate from, LocalDate to) {
        List<Booking> bookings = bookingsByApartment
                .getOrDefault(apartmentId, Collections.emptyList());

        // Two date ranges overlap if: start1 < end2 AND start2 < end1
        for (Booking b : bookings) {
            if (b.getStartDate().isBefore(to) && from.isBefore(b.getEndDate())) {
                // overlap → not available
                return false;
            }
        }
        return true;
    }
}
