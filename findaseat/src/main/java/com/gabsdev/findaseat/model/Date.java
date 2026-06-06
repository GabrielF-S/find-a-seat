package com.gabsdev.findaseat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.LocalTime;
@Embeddable
public class Date {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate reservationDay;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTimeLocation;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTimeLocation;

    public Date() {
    }

    public Date(LocalDate date, LocalTime startTimeLocation, LocalTime endTimeLocation) {
        this.reservationDay = date;
        this.startTimeLocation = startTimeLocation;
        this.endTimeLocation = endTimeLocation;
    }

    public LocalDate getReservationDay() {
        return reservationDay;
    }

    public void setReservationDay(LocalDate reservationDay) {
        this.reservationDay = reservationDay;
    }

    public LocalTime getStartTimeLocation() {
        return startTimeLocation;
    }

    public void setStartTimeLocation(LocalTime startTimeLocation) {
        this.startTimeLocation = startTimeLocation;
    }

    public LocalTime getEndTimeLocation() {
        return endTimeLocation;
    }

    public void setEndTimeLocation(LocalTime endTimeLocation) {
        this.endTimeLocation = endTimeLocation;
    }


}
