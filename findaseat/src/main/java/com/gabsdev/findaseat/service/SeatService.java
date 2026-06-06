package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.dto.response.SeatResponse;
import com.gabsdev.findaseat.model.Seat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SeatService {
    Seat createSeat(SeatRequest seatRequest);

    Seat getSeatById(UUID uuid, UUID id, LocalDate localDate);

    List<SeatResponse> getAllBusinessSeat(UUID businessUuid, LocalDate localDate);

    List<SeatResponse> getAllSeatSByFloor(UUID floorUuid, LocalDate localDate);

    Seat updateSeat(Seat seat);

    void deleteByBusinessIuudAndSeatId(UUID businessUuid, UUID id);
}
