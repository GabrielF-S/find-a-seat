package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.dto.response.SeatResponse;
import com.gabsdev.findaseat.model.entity.Seat;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.UUID;

public interface SeatService {
    Seat createSeat(SeatRequest seatRequest);

    void verifyNumberOfSeats(SeatRequest seatRequest);

    Seat getSeatById(UUID uuid, UUID id, LocalDate localDate);

    org.springframework.data.domain.Page<SeatResponse> getAllBusinessSeat(UUID businessUuid, LocalDate localDate, Integer page, Integer size);

    Page<SeatResponse> getAllSeatSByFloor(UUID floorUuid, LocalDate localDate, Integer page, Integer size);

    Seat updateSeat(Seat seat);

    void deleteByBusinessIuudAndSeatId(UUID businessUuid, UUID id);
}
