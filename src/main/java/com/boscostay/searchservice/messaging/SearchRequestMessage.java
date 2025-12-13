package com.boscostay.searchservice.messaging;

import java.io.Serializable;
import java.time.LocalDate;

public class SearchRequestMessage implements Serializable {

    private LocalDate fromDate;
    private LocalDate toDate;

    public SearchRequestMessage() {
    }

    public SearchRequestMessage(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "SearchRequestMessage{" +
                "fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }
}
