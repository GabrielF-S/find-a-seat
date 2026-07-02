package com.gabsdev.findaseat.mapper;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.model.entity.Floor;

public interface FloorMapper {


    Floor toFloor(FloorRequest request, String stringType);

    FloorResponse toFloorResponse(Floor floor);
}
