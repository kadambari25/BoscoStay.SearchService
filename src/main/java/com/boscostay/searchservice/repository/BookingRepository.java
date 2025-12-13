package com.boscostay.searchservice.repository;

import com.boscostay.searchservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByApartmentIdIn(List<Long> apartmentIds);
}
