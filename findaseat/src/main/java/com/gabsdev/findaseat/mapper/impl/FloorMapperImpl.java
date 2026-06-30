package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.BusinessResponse;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.mapper.FloorMapper;
import com.gabsdev.findaseat.model.entity.Floor;
import org.springframework.stereotype.Component;

@Component
public class FloorMapperImpl implements FloorMapper {


    @Override
    public Floor toFloor(FloorRequest request) {
        return Floor.builder()
                .floorName(request.floorNumber() + "° andar")
                .towerName(request.towerName())
                .build();
    }

    @Override
    public FloorResponse toFloorResponse(Floor floor) {
        return new FloorResponse(floor.getId(), floor.getFloorName(), floor.getSlug(),
                new BusinessResponse(
                        floor.getBusiness().getUuid(), floor.getBusiness().getBusinessName(), floor.getBusiness().getLocation())

        );
    }
}
