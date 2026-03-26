package com.gabsdev.findaseat.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatName;
    private String slug;
    private Status status;
    private boolean exclusive;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "tb_floors_id")
    private Floors floors;

    public Seat(Long id, String seatName, String slug,
                Status status, boolean exclusive, LocalDateTime createdAt,
                LocalDateTime updatedAt, Floors floors) {
        this.id = id;
        this.seatName = seatName;
        this.slug = slug;
        this.status = status;
        this.exclusive = exclusive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.floors = floors;
    }

    public Seat() {
    }

    public Floors getFloors() {
        return floors;
    }

    public void setFloors(Floors floors) {
        this.floors = floors;
    }

    public Seat(String seatName, String slug, Status status, boolean exclusive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.seatName = seatName;
        this.slug = slug;
        this.status = status;
        this.exclusive = exclusive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}


