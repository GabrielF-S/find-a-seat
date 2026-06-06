package com.gabsdev.findaseat.controller.impl;

import com.gabsdev.findaseat.controller.ReservationController;
import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.model.Reservation;
import com.gabsdev.findaseat.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("api/reservation")
public class ReservationControllerImpl implements ReservationController {

    private final ReservationService service;

    public ReservationControllerImpl(ReservationService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ReservationResponse> makeReservation(ReservationRequest reservation) {
        ReservationResponse reservationCreated = service.createReservation(reservation);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}").buildAndExpand(reservationCreated.id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<ReservationResponse> getReservation(UUID reservationId, String employeeName, LocalDate date) {

        return  ResponseEntity.ok(service.getReservation(reservationId, employeeName, date));
    }

    @Override
    public ResponseEntity<ReservationResponse> getReservationSeat(UUID seatId, LocalDate date) {
        return null;
    }

    @Override
    public ResponseEntity<ReservationResponse> updateReservation(Reservation reservation) {
        return ResponseEntity.ok(service.updateReservation(reservation));
    }

    @Override
    public ResponseEntity<Void> deleteReservation(UUID uuid) {
        service.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }
}
