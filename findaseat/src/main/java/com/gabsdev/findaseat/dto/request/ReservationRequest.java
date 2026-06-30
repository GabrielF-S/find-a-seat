package com.gabsdev.findaseat.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationRequest(LocalDate date, UUID seatId, Long employeId) {
}
