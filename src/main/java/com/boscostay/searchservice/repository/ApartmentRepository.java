package com.boscostay.searchservice.repository;

import com.boscostay.searchservice.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ApartmentRepository extends JpaRepository<Apartment, UUID> {

    @Query("SELECT a FROM Apartment a WHERE upper(a.address) LIKE %?1%")
    List<Apartment> findByAddressContainsText(String address);

    // Use JpaRepository.save(...) from your service; no need to redeclare save
    // here.

    // Use the standard deleteById signature that matches the repository ID type
    // (Long).
    void deleteById(UUID id);

    // Example of a correct JPQL update method. Note: parameters are simple names
    // (no dots).
    @Modifying
    @Transactional
    @Query("UPDATE Apartment a SET a.name = ?1, a.description = ?2, a.address = ?3, a.floor = ?4, a.noiseLevel = ?5, a.distanceToCenterInKm = ?6, a.pricePerDay = ?7, a.isFurnished = ?8, a.areaInSquareMeters = ?9 WHERE a.id = ?10")
    int updateApartment(
            @Param("id") UUID id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("address") String address,
            @Param("floor") Integer floor,
            @Param("noiseLevel") Integer noiseLevel,
            @Param("distanceToCenterInKm") Double distanceToCenterInKm,
            @Param("pricePerDay") Double pricePerDay,
            @Param("isFurnished") Boolean isFurnished,
            @Param("areaInSquareMeters") Double areaInSquareMeters);
}