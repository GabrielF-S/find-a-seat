package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.dto.response.SeatResponse;
import com.gabsdev.findaseat.mapper.SeatMapper;
import com.gabsdev.findaseat.model.entity.Floor;
import com.gabsdev.findaseat.model.entity.Seat;
import com.gabsdev.findaseat.model.enums.Status;
import org.springframework.stereotype.Component;

@Component
public class SeatMapperImpl implements SeatMapper {
    @Override
    public Seat toSeat(SeatRequest seatRequest, Floor floor) {
        Seat seat = Seat.builder().nick(seatRequest.nick())
                .status(Status.AVALIABLE)
                .exclusive(seatRequest.isExclusive())
                .floor(floor)
                .type(seatRequest.type())
                .seatsQuantity(seatRequest.numberOfSeats()).build();
        return seat;
    }

    @Override
    public SeatResponse toSeatResponse(Seat seat) {
        return new SeatResponse(seat.getId(),seat.getSeatName(), seat.getSlug(), seat.getNick(),seat.getStatus(),seat.getSeatsQuantity() , seat.getFloor().getFloorName());
    }


}
