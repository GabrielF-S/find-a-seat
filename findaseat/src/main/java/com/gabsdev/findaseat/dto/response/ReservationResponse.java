package com.gabsdev.findaseat.dto.response;

import com.gabsdev.findaseat.model.entity.ReservationPeriod;

import java.util.UUID;

public record ReservationResponse(UUID id, String seatName, String employeeName, ReservationPeriod reservationPeriod, boolean activate) {
}
