package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.model.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ReservationService {

    ReservationResponse createReservation(ReservationRequest reservation, LocalTime start, LocalTime end);

    ReservationResponse getReservation(UUID reservationId, String employeeName, LocalDate date);

    ReservationResponse updateReservation(Reservation reservation);

    void deleteById(UUID uuid);

    List<ReservationResponse> getBySeatAndData(UUID seatId, LocalDate date);

    List<ReservationResponse> getByDay(LocalDate localDate);

    ReservationResponse close(UUID uuid);
}
