package com.gabsdev.findaseat.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_waitlist")
@Builder
@Data
public class Waitlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private LocalDate reservationDay;
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    private Duration duration;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyy HH:mm:ss")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @JsonFormat(pattern = "dd/MM/yyy HH:mm:ss")
    private LocalDateTime updatedAt;

    private UUID businessUuid;

    public Waitlist() {
    }


    public Waitlist(Long employeeId, LocalDate date, Duration duration, ReservationStatus reservationStatus, UUID businessUuid) {
        this.employeeId = employeeId;
        this.reservationDay = date;
        this.duration = duration;
        this.reservationStatus = reservationStatus;
        this.businessUuid = businessUuid;
    }

    public Waitlist(Long id, Long employeeId, LocalDate reservationDay, Duration duration, ReservationStatus reservationStatus, LocalDateTime createdAt, LocalDateTime updatedAt, UUID businessUuid) {
        this.id = id;
        this.employeeId = employeeId;
        this.reservationDay = reservationDay;
        this.duration = duration;
        this.reservationStatus = reservationStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.businessUuid = businessUuid;
    }
}
