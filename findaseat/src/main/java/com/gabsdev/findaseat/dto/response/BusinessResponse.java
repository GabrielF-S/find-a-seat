package com.gabsdev.findaseat.dto.response;

import com.gabsdev.findaseat.model.entity.Location;
import com.gabsdev.findaseat.model.enums.BusinessType;

import java.util.UUID;

public record BusinessResponse(UUID id, String businessName, Location location, BusinessType type) {
}
