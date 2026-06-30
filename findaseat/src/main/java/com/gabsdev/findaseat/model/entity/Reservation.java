package com.gabsdev.findaseat.model.entity;

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
    private Date date;
    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
    @ManyToOne
    @JoinColumn(name = "tb_employees_id")
    private Employee employees;

    private boolean active;



    public Reservation() {

    }

    public Reservation(UUID id, Date date, Seat seat, Employee employees) {
        this.id = id;
        this.date = date;
        this.seat = seat;
        this.employees = employees;
    }

    public Reservation(UUID id, Date date, Seat seat, Employee employees, boolean active) {
        this.id = id;
        this.date = date;
        this.seat = seat;
        this.employees = employees;
        this.active = active;
    }

    public boolean isActive() {
        return getDate().getEndTimeLocation().isAfter(LocalTime.now());
    }


}
