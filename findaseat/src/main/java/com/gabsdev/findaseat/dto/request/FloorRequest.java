package com.gabsdev.findaseat.dto.request;

import java.util.UUID;

public record FloorRequest(UUID businessId, String floorName) {
}
