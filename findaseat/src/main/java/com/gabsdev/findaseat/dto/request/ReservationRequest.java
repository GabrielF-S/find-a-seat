package com.gabsdev.findaseat.dto.request;

import com.gabsdev.findaseat.model.Date;

import java.util.UUID;

public record ReservationRequest(Date data, UUID seatId, Long employeId) {
}
