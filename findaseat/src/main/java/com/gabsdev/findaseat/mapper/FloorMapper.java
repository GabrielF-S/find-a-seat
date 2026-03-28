package com.gabsdev.findaseat.mapper;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.model.Floor;

public interface FloorMapper {


    Floor toFloor(FloorRequest request);

    FloorResponse toFloorResponse(Floor floor);
}
