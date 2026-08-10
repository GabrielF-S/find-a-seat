package com.gabsdev.findaseat.dto.response;

public record WaitlistResponse(Long id, String employeeName, java.time.LocalDate reservationDay,
                               java.time.Duration duration,
                               com.gabsdev.findaseat.model.enums.ReservationStatus reservationStatus) {
}
