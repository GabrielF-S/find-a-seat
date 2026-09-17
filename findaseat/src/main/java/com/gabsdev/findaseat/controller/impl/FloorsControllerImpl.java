package com.gabsdev.findaseat.controller.impl;

import com.gabsdev.findaseat.controller.FloorsController;
import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.dto.response.LayoutResponse;
import com.gabsdev.findaseat.model.entity.Floor;
import com.gabsdev.findaseat.service.FloorService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/floor")
public class FloorsControllerImpl implements FloorsController {

    private final FloorService service;

    public FloorsControllerImpl(FloorService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<FloorResponse> createFloor(FloorRequest request) {
        Floor creted = service.creteFloor(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(creted.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<FloorResponse> getFloorById(UUID uuid) {
        return ResponseEntity.ok(service.getById(uuid));
    }

    @Override
    public ResponseEntity<Page<FloorResponse>> getAllFloors(UUID businessUuid,  Integer page,Integer size) {
        return ResponseEntity.ok(service.getAll(businessUuid, page, size));
    }

    @Override
    public ResponseEntity<LayoutResponse> getLayoutByFloorUuid(UUID uuid) {
        return ResponseEntity.ok(service.getLayoutByUuid(uuid));
    }

    @Override
    public ResponseEntity<FloorResponse> updateFloor(Floor floor) {

        return ResponseEntity.ok(service.updateFloor(floor));
    }

    @Override
    public ResponseEntity<FloorResponse> insertLayout(UUID uuid, String layout) {
        return ResponseEntity.ok(service.insertLayout(uuid, layout));
    }

    @Override
    public ResponseEntity<Void> deleteFloor(UUID uuid) {
        service.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }
}
