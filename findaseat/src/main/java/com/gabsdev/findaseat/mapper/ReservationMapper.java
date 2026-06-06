package com.gabsdev.findaseat.mapper;

import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.model.Reservation;

public interface ReservationMapper {
    ReservationResponse toReservationResponse(Reservation reservation);
}
