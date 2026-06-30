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


}
