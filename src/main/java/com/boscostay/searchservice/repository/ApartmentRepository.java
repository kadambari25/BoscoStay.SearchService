package com.boscostay.searchservice.repository;

import com.boscostay.searchservice.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    List<Apartment> findByCityIgnoreCase(String city);
}
