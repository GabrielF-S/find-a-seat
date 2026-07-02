package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.mapper.ReservationMapper;
import com.gabsdev.findaseat.model.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapperImpl implements ReservationMapper {
    @Override
    public ReservationResponse toReservationResponse(Reservation reservation) {

        return new ReservationResponse(reservation.getId(),reservation.getSeat().getSeatName(),reservation.getEmployees().getEmployeeName(), reservation.getReservationPeriod());
    }
}
