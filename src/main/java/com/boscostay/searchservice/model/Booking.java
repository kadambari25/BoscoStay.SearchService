package com.boscostay.searchservice.model;

import java.time.LocalDate;

public class Booking {
    private Long apartmentId;
    private LocalDate startDate;
    private LocalDate endDate;

    public Booking() {
    }

    public Booking(Long apartmentId, LocalDate startDate, LocalDate endDate) {
        this.apartmentId = apartmentId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
