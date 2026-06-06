package com.gabsdev.findaseat.dto.response;

import com.gabsdev.findaseat.model.Status;

import java.util.UUID;

public record SeatResponse(UUID uuid, String name, String slug, String nick, Status status, String floor) {
}
