package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.dto.response.SeatResponse;
import com.gabsdev.findaseat.model.Seat;

import java.util.List;
import java.util.UUID;

public interface SeatService {
    Seat createSeat(SeatRequest seatRequest);

    Seat getSeatById(UUID uuid, Long id);

    List<SeatResponse> getAllBusinessSeat(UUID businessUuid);

    List<SeatResponse> getAllSeatSByFloor(UUID floorUuid);

    Seat updateSeat(Seat seat);

    void deleteByBusinessIuudAndSeatId(UUID businessUuid, Long id);
}
