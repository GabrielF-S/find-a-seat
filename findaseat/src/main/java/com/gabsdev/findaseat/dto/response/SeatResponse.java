package com.gabsdev.findaseat.dto.response;

import com.gabsdev.findaseat.model.Status;

import java.time.LocalDateTime;

public record SeatResponse(String name, String slug, String nick, Status status, String floor) {
}
