package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.model.Floor;

import java.util.List;
import java.util.UUID;

public interface FloorService {
    Floor creteFloor(FloorRequest request);

    Floor getById(UUID uuid);

    Floor updateFloor(Floor floor);

    List<FloorResponse> getAll(UUID businessUuid);

    void deleteById(UUID uuid);
}
