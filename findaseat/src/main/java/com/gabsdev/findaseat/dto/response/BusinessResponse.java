package com.gabsdev.findaseat.dto.response;

import com.gabsdev.findaseat.model.Location;

import java.util.UUID;

public record BusinessResponse(UUID id, String businessName, Location location) {
}
