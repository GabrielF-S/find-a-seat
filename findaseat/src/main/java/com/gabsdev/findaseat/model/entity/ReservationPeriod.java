package com.gabsdev.findaseat.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Embeddable
@Data
@Builder
public class ReservationPeriod implements Comparable<ReservationPeriod> {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate reservationDay;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTimeLocation;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTimeLocation;

    public ReservationPeriod() {
    }

    public ReservationPeriod(LocalDate date, LocalTime startTimeLocation, LocalTime endTimeLocation) {
        this.reservationDay = date;
        this.startTimeLocation = startTimeLocation;
        this.endTimeLocation = endTimeLocation;
    }

    @Override
    public int compareTo(ReservationPeriod other) {
        // Null safety check
        if (other == null) {
            throw new NullPointerException("Cannot compare with null");
        }
        int comparation = this.getReservationDay().compareTo(other.getReservationDay());
        if (comparation != 0){
            return comparation;
        }
        return this.getStartTimeLocation().compareTo(other.getStartTimeLocation());
    }
}
