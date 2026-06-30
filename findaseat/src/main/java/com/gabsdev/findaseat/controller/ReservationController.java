package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.model.entity.Reservation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default server URL")})
public interface ReservationController {

    @PostMapping
    ResponseEntity<ReservationResponse> makeReservation(
            @RequestBody ReservationRequest reservation,
            @RequestParam(name = "start", required = false) LocalTime startTime,
            @RequestParam(name = "end", required = true)  LocalTime endTime
            );

    @GetMapping(value = "/getReservation")
    ResponseEntity<ReservationResponse> getReservation(
            @RequestParam(value = "id", required = false) UUID reservationId,
            @RequestParam(value = "name", required = false, defaultValue = "") String employeeName,
            @RequestParam(value ="dia", required = false)LocalDate date
            );

    @GetMapping(value = "/getReservationBySeat")
    ResponseEntity<List<ReservationResponse>> getReservationSeat(
            @RequestParam(value = "seatId") UUID seatId,
            @RequestParam(value ="dia", required = false)LocalDate date
    );

    @GetMapping("/byDay")
    ResponseEntity<List<ReservationResponse>> getReservationByDay(@RequestParam(value = "day") LocalDate localDate);

    @PutMapping(value = "/update")
    ResponseEntity<ReservationResponse> updateReservation(@RequestBody Reservation reservation);

    @PatchMapping("/closeReservation")
    ResponseEntity<ReservationResponse> closeReservation( @PathVariable UUID uuid);

    @DeleteMapping( value = "/delete/{uuid}")
    ResponseEntity<Void> deleteReservation(@PathVariable UUID uuid);
}
