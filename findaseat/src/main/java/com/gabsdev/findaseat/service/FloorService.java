package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.dto.response.LayoutResponse;
import com.gabsdev.findaseat.model.entity.Floor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface FloorService {
    Floor creteFloor(FloorRequest request);

    FloorResponse getById(UUID uuid);

    FloorResponse updateFloor(Floor floor);

    Page<FloorResponse> getAll(UUID businessUuid, Integer page, Integer size);

    void deleteById(UUID uuid);

    FloorResponse insertLayout(UUID uuid, String layout);

    LayoutResponse getLayoutByUuid(UUID uuid);
}
