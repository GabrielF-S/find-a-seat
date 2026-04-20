package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.dto.response.SeatResponse;
import com.gabsdev.findaseat.mapper.SeatMapper;
import com.gabsdev.findaseat.model.Floor;
import com.gabsdev.findaseat.model.Seat;
import com.gabsdev.findaseat.model.Status;
import com.github.slugify.Slugify;
import org.springframework.stereotype.Component;

@Component
public class SeatMapperImpl implements SeatMapper {
    @Override
    public Seat toSeat(SeatRequest seatRequest, Floor floor) {
        Seat seat = new Seat(seatRequest.nick(), Status.AVALIABLE,seatRequest.isExclusive(), floor);
        return seat;
    }

    @Override
    public SeatResponse toSeatResponse(Seat seat) {
        return new SeatResponse(seat.getSeatName(), seat.getSlug(), seat.getNick(),seat.getStatus(), seat.getFloors().getFloorName());
    }


}
