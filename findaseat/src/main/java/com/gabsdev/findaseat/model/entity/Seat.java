package com.gabsdev.findaseat.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gabsdev.findaseat.model.enums.Status;
import com.gabsdev.findaseat.model.enums.Type;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_seats")
@Builder
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String seatName;
    private String nick;
    private String slug;
    @Enumerated(EnumType.STRING)
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
    @Enumerated(EnumType.STRING)
    private Type type;
    private Integer seatsQuantity;

    public Seat(UUID id, String seatName, String slug,
                Status status, boolean exclusive, LocalDateTime createdAt,
                LocalDateTime updatedAt, Floor floors, Type type) {
        this.id = id;
        this.seatName = seatName;
        this.slug = slug;
        this.status = status;
        this.exclusive = exclusive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.floor = floors;
        this.type = type;
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


    public Seat(String seatName, String slug, Status status, boolean exclusive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.seatName = seatName;
        this.slug = slug;
        this.status = status;
        this.exclusive = exclusive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Seat(UUID id, String seatName, String nick, String slug, Status status, boolean exclusive, LocalDateTime createdAt, LocalDateTime updatedAt, Floor floor, Type type, Integer seatsQuantity) {
        this.id = id;
        this.seatName = seatName;
        this.nick = nick;
        this.slug = slug;
        this.status = status;
        this.exclusive = exclusive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.floor = floor;
        this.type = type;
        this.seatsQuantity = seatsQuantity;
    }
}


