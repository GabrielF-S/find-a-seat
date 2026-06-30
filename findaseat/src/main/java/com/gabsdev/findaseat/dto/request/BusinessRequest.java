package com.gabsdev.findaseat.dto.request;

import com.gabsdev.findaseat.model.entity.Location;
import com.gabsdev.findaseat.model.enums.BusinessType;

public record BusinessRequest(String businessName, Location location, BusinessType businessType) {
}
