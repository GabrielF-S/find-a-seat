package com.gabsdev.findaseat.controller.impl;

import com.gabsdev.findaseat.controller.SeatController;
import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.dto.response.SeatResponse;
import com.gabsdev.findaseat.model.Seat;
import com.gabsdev.findaseat.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seat")
public class SeatControllerImpl implements SeatController {

    private final SeatService seatService;

    public SeatControllerImpl(SeatService seatService) {
        this.seatService = seatService;
    }


    @Override
    public ResponseEntity<Seat> createSeat(SeatRequest seatRequest) {
        Seat seatCreated = seatService.createSeat(seatRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(seatCreated.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<Seat> getSeatById(UUID businessUuid , Long id) {
        return ResponseEntity.ok(seatService.getSeatById(businessUuid,id));
    }

    @Override
    public ResponseEntity<List<SeatResponse>> getAllSeat(UUID businessUuid) {
        return ResponseEntity.ok(seatService.getAllBusinessSeat(businessUuid));
    }

    @Override
    public ResponseEntity<List<SeatResponse>> getAllSeatByFloor(UUID floorUuid) {
        return ResponseEntity.ok(seatService.getAllSeatSByFloor(floorUuid));
    }

    @Override
    public ResponseEntity<Seat> updateSeat(Seat seat) {

        return ResponseEntity.ok(seatService.updateSeat(seat));
    }

    @Override
    public ResponseEntity<Void> deleteSeat(UUID businessUuid ,Long id) {
        seatService.deleteByBusinessIuudAndSeatId(businessUuid,id);
        return ResponseEntity.noContent().build();
    }
}
