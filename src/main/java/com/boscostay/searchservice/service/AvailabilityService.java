package com.boscostay.searchservice.service;

import com.boscostay.searchservice.model.Apartment;
import com.boscostay.searchservice.model.Booking;
import com.boscostay.searchservice.repository.ApartmentRepository;
import com.boscostay.searchservice.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final ApartmentRepository apartmentRepository;
    private final BookingRepository bookingRepository;

    public AvailabilityService(ApartmentRepository apartmentRepository,
                               BookingRepository bookingRepository) {
        this.apartmentRepository = apartmentRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Apartment> searchAvailableApartments(String city,
                                                     LocalDate checkIn,
                                                     LocalDate checkOut) {

        // 1) apartments in city
        List<Apartment> apartments = apartmentRepository.findByCityIgnoreCase(city);
        if (apartments.isEmpty()) {
            return Collections.emptyList();
        }

        // 2) bookings for those apartments
        List<Long> apartmentIds = apartments.stream()
                .map(Apartment::getId)
                .collect(Collectors.toList());

        List<Booking> bookings =
                bookingRepository.findByApartmentIdIn(apartmentIds);

        Map<Long, List<Booking>> bookingsByApartment = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getApartmentId));

        // 3) filter by overlapping bookings
        return apartments.stream()
                .filter(a -> isApartmentAvailable(a.getId(), checkIn, checkOut, bookingsByApartment))
                .collect(Collectors.toList());
    }

    private boolean isApartmentAvailable(Long apartmentId,
                                         LocalDate reqStart,
                                         LocalDate reqEnd,
                                         Map<Long, List<Booking>> bookingsByApartment) {

        List<Booking> existingBookings =
                bookingsByApartment.getOrDefault(apartmentId, Collections.emptyList());

        for (Booking booking : existingBookings) {
            if (datesOverlap(reqStart, reqEnd,
                    booking.getCheckInDate(), booking.getCheckOutDate())) {
                return false;
            }
        }
        return true;
    }

    private boolean datesOverlap(LocalDate reqStart,
                                 LocalDate reqEnd,
                                 LocalDate existingStart,
                                 LocalDate existingEnd) {
        return !(reqEnd.isBefore(existingStart) || reqStart.isAfter(existingEnd));
    }
}
