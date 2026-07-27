package com.gabsdev.findaseat.dto.response;

import com.gabsdev.findaseat.model.entity.ReservationPeriod;
import com.gabsdev.findaseat.model.enums.ReservationStatus;

import java.util.UUID;

public record ReservationResponse(UUID id,
                                  String seatName,
                                  String employeeName,
                                  ReservationPeriod reservationPeriod,
                                  boolean activate,
                                  ReservationStatus reservationStatus ) {
}
