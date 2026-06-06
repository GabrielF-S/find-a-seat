package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.model.Reservation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default server URL")})
public interface ReservationController {

    @PostMapping
    ResponseEntity<ReservationResponse> makeReservation(@RequestBody ReservationRequest reservation);

    @GetMapping(value = "/getReservation")
    ResponseEntity<ReservationResponse> getReservation(
            @RequestParam(value = "id", required = false) UUID reservationId,
            @RequestParam(value = "name", required = false, defaultValue = "") String employeeName,
            @RequestParam(value ="dia", required = false)LocalDate date
            );

    @GetMapping(value = "/getReservationBySeat")
    ResponseEntity<ReservationResponse> getReservationSeat(
            @RequestParam(value = "seatId") UUID seatId,
            @RequestParam(value ="dia", required = false)LocalDate date
    );


    @PutMapping(value = "/update")
    ResponseEntity<ReservationResponse> updateReservation(@RequestBody Reservation reservation);

    @DeleteMapping( value = "/delete/{uuid}")
    ResponseEntity<Void> deleteReservation(@PathVariable UUID uuid);
}
