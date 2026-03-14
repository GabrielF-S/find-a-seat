package com.gabsdev.findaseat.dto.response;

import com.gabsdev.findaseat.model.Location;

import java.util.UUID;

public record ResponseBusiness(UUID id, String businessName, Location location) {
}
