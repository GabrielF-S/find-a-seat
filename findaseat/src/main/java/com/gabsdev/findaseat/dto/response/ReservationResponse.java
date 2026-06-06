package com.gabsdev.findaseat.dto.response;

import java.util.UUID;

public record ReservationResponse(UUID id, String seatName, String employeeName, com.gabsdev.findaseat.model.Date date) {
}
