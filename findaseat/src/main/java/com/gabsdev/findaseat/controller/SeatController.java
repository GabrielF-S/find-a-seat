package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.dto.response.SeatResponse;
import com.gabsdev.findaseat.model.Seat;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default server URL")})
public interface SeatController {

    @PostMapping()
    ResponseEntity<Seat> createSeat(@RequestBody SeatRequest seatRequest);

    @GetMapping(value = "{businessUuid}/{id}")
    ResponseEntity<Seat> getSeatById(@PathVariable("businessUuid") UUID businessUuid, @PathVariable("id") UUID id,  @RequestParam(value = "date", required = false)LocalDate localDate);

    @GetMapping(value = "{businessUuid}/getAll")
    ResponseEntity<List<SeatResponse>> getAllSeat(@PathVariable("businessUuid") UUID businessUuid, @RequestParam(value = "date", required = false)LocalDate localDate);

    @GetMapping(value = "{floorUuid}/getAllByFloor")
    ResponseEntity<List<SeatResponse>> getAllSeatByFloor(@PathVariable("floorUuid") UUID floorUuid,  @RequestParam(value = "date", required = false)LocalDate localDate);

    @PutMapping(value = "/update")
    ResponseEntity<Seat> updateSeat(@RequestBody Seat seat);


    @DeleteMapping(value = "{businessUuid}/delete/{id}")
    ResponseEntity<Void> deleteSeat(@PathVariable("businessUuid") UUID businessUuid, @PathVariable("id") UUID id);
}
