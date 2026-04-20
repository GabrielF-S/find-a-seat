package com.gabsdev.findaseat.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatName;
    private String nick;
    private String slug;
    private Status status;
    private boolean exclusive;
    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "tb_floors_id")
    private Floor floor;

    public Seat(Long id, String seatName, String slug,
                Status status, boolean exclusive, LocalDateTime createdAt,
                LocalDateTime updatedAt, Floor floors) {
        this.id = id;
        this.seatName = seatName;
        this.slug = slug;
        this.status = status;
        this.exclusive = exclusive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.floor = floors;
    }

    public Seat() {
    }

    public Seat(String name, String nick, boolean exclusive) {
        this.seatName = name;
        this.exclusive = exclusive;
        this.status = Status.AVALIABLE;

    }

    public Seat(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public Seat(String nick, Status status, boolean exclusive, Floor floor) {
        this.nick = nick;
        this.status = status;
        this.exclusive = exclusive;
        this.floor = floor;
    }

    public Floor getFloors() {
        return floor;
    }

    public void setFloors(Floor floors) {
        this.floor = floors;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
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


