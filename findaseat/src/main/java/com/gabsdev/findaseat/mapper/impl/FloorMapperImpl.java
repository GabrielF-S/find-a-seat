package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.mapper.FloorMapper;
import com.gabsdev.findaseat.model.Floor;
import org.springframework.stereotype.Component;

@Component
public class FloorMapperImpl implements FloorMapper {


    @Override
    public Floor toFloor(FloorRequest request) {
        return new Floor(request.floorName());
    }
}
