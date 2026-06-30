package com.gabsdev.findaseat.dto.request;

import com.gabsdev.findaseat.model.enums.Type;

import java.util.UUID;

public record SeatRequest(Type type, Long number,String nick, boolean isExclusive, Integer numberOfSeats, UUID floorId) {
}
