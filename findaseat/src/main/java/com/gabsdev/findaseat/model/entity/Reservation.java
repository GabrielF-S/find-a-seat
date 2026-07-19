package com.gabsdev.findaseat.model.entity;

import com.gabsdev.findaseat.model.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "tb_reservation")
@Data
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Embedded
    private ReservationPeriod reservationPeriod;
    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
    @ManyToOne
    @JoinColumn(name = "tb_employees_id")
    private Employee employees;

    private boolean active;
    private ReservationStatus status;

    public Reservation() {    }

    public Reservation(UUID id, ReservationPeriod reservationPeriod, Seat seat, Employee employees) {
        this.id = id;
        this.reservationPeriod = reservationPeriod;
        this.seat = seat;
        this.employees = employees;
        this.status = ReservationStatus.PENDING;
    }

    public Reservation(UUID id, ReservationPeriod reservationPeriod, Seat seat, Employee employees, boolean active) {
        this.id = id;
        this.reservationPeriod = reservationPeriod;
        this.seat = seat;
        this.employees = employees;
        this.active = active;
        this.status = ReservationStatus.PENDING;
    }
}
