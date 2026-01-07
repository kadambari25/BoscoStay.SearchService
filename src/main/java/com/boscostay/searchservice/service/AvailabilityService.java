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

    public List<Apartment> searchAvailableApartments(String address,
            LocalDate checkIn,
            LocalDate checkOut) {
        List<Apartment> apartments = apartmentRepository.findByAddressIgnoreCase(address);
        if (apartments.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> apartmentIds = apartments.stream()
                .map(Apartment::getId)
                .collect(Collectors.toList());

        List<Booking> bookings = bookingRepository.findByApartmentIdIn(apartmentIds);

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
