package com.gabsdev.findaseat.mapper;

import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.dto.response.SeatResponse;
import com.gabsdev.findaseat.model.Floor;
import com.gabsdev.findaseat.model.Seat;

public interface SeatMapper {

    Seat toSeat(SeatRequest seatRequest, Floor floor);

    SeatResponse toSeatResponse(Seat seat);
}
