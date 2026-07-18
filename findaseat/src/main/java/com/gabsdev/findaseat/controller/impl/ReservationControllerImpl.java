package com.gabsdev.findaseat.controller.impl;

import com.gabsdev.findaseat.controller.ReservationController;
import com.gabsdev.findaseat.dto.request.QuickReservationRequest;
import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.model.entity.Reservation;
import com.gabsdev.findaseat.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/reservation")
public class ReservationControllerImpl implements ReservationController {

    private final ReservationService service;

    public ReservationControllerImpl(ReservationService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ReservationResponse> makeReservation(
            ReservationRequest reservation, LocalTime start, LocalTime end
    ) {
        ReservationResponse reservationCreated = service.createReservation(reservation, start, end);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(reservationCreated.id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<ReservationResponse> quickReservation(QuickReservationRequest reservation, LocalTime startTime, LocalTime endTime) {
        ReservationResponse reservationCreated = service.CreateQuickReservation(reservation, startTime, endTime);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(reservationCreated.id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<List<ReservationResponse>> getReservation(UUID reservationId, String employeeName, LocalDate date) {
        return  ResponseEntity.ok(service.getReservation(reservationId, employeeName, date));
    }

    @Override
    public ResponseEntity<List<ReservationResponse>> getReservationSeat(UUID seatId, LocalDate date) {
        return ResponseEntity.ok(service.getBySeatAndData(seatId, date));
    }

    @Override
    public ResponseEntity<List<ReservationResponse>> getReservationByDay(LocalDate localDate) {
        return ResponseEntity.ok(service.getByDay(localDate));
    }

    @Override
    public ResponseEntity<ReservationResponse> updateReservation(Reservation reservation) {
        return ResponseEntity.ok(service.updateReservation(reservation));
    }

    @Override
    public ResponseEntity<ReservationResponse> closeReservation(UUID uuid) {
        return ResponseEntity.ok(service.close(uuid));
    }

    @Override
    public ResponseEntity<Void> deleteReservation(UUID uuid) {
        service.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }
}
