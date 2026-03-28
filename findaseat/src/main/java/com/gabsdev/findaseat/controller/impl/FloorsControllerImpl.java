package com.gabsdev.findaseat.controller.impl;

import com.gabsdev.findaseat.controller.FloorsController;
import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.model.Floor;
import com.gabsdev.findaseat.service.FloorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<Floor> createFloor(FloorRequest request) {
        Floor creted = service.creteFloor(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(creted.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<Floor> getFlorById(UUID uuid) {
        return ResponseEntity.ok(service.getById(uuid));
    }

    @Override
    public ResponseEntity<List<FloorResponse>> getAllFloors(UUID businessUuid) {
        return ResponseEntity.ok(service.getAll(businessUuid));
    }

    @Override
    public ResponseEntity<Floor> updateFloor(Floor floor) {

        return ResponseEntity.ok(service.updateFloor(floor));
    }

    @Override
    public ResponseEntity<Void> deleteFloor(UUID uuid) {
        service.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }
}
