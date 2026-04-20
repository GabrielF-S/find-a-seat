package com.gabsdev.findaseat.dto.request;

import com.gabsdev.findaseat.model.Type;

import java.util.UUID;

public record SeatRequest(Type type, Long number,String nick, boolean isExclusive, UUID floorId) {
}
