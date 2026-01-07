package com.boscostay.searchservice.repository;

import com.boscostay.searchservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByApartmentIdIn(List<UUID> apartmentIds);

    // void deleteByBookingIdAndApartmentId(String bookingId, UUID apartmentId);

    Optional<Booking> findByBookingId(String bookingId);
}
