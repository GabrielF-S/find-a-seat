package com.gabsdev.findaseat.dto.response;

import com.gabsdev.findaseat.model.enums.Status;

import java.util.UUID;

public record SeatResponse(UUID uuid, String name, String slug, String nick, Status status, Integer qtd , String floor) {
}
