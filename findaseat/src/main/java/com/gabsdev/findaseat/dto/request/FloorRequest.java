package com.gabsdev.findaseat.dto.request;

import java.util.UUID;

public record FloorRequest(UUID businessId, int floorNumber, String towerName) {
}
