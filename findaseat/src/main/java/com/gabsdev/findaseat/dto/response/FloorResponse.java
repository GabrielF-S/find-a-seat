package com.gabsdev.findaseat.dto.response;

import java.util.UUID;

public record FloorResponse(UUID uuid, String floorName, String slug, BusinessResponse business) {
}
