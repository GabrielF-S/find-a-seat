package com.gabsdev.findaseat.dto.response;

import com.gabsdev.findaseat.model.entity.Date;

import java.util.UUID;

public record ReservationResponse(UUID id, String seatName, String employeeName, Date date) {
}
