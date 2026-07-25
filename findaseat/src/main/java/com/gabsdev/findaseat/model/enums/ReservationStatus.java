package com.gabsdev.findaseat.model.enums;

public enum ReservationStatus {
    PENDING(1L, "pending"),
    CONFIRMED(2L, "confirmed"),
    CANCELLED(3L, "cancelled"),
    NOT_CONFIRMED(4L, "not_confirmed");

    ReservationStatus(Long id, String statusName) {
    }
}
