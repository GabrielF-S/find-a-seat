package com.gabsdev.findaseat.model;


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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Seat() {
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


