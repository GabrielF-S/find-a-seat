package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.model.Reservation;

import java.time.LocalDate;
import java.util.UUID;

public interface ReservationService {

    ReservationResponse createReservation(ReservationRequest reservation);

    ReservationResponse getReservation(UUID reservationId, String employeeName, LocalDate date);

    ReservationResponse updateReservation(Reservation reservation);

    void deleteById(UUID uuid);
}
