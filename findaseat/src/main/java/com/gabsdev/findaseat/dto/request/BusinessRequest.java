package com.gabsdev.findaseat.dto.request;

import com.gabsdev.findaseat.model.Location;

public record BusinessRequest(String businessName, Location location) {
}
