package com.gabsdev.findaseat.dto.request;

import com.gabsdev.findaseat.model.enums.Type;

import java.time.LocalDate;

public record QuickReservationRequest(LocalDate date, Type type, Long employeId) {
}
