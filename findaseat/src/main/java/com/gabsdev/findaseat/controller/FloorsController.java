package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.model.Business;
import com.gabsdev.findaseat.model.Floor;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default server URL")})
public interface FloorsController {

    @PostMapping()
    ResponseEntity<Floor> createFloor(@RequestBody FloorRequest request);

    @GetMapping(value = "/{uuid}")
    ResponseEntity<Floor> getFloorById(@PathVariable UUID uuid);

    @GetMapping(value = "/{businessUuid}/getAll")
    ResponseEntity<List<FloorResponse>> getAllFloors(@PathVariable UUID businessUuid);


    @PutMapping(value = "/update")
    ResponseEntity<Floor> updateFloor(@RequestBody Floor floor);


    @DeleteMapping(value = "/delete/{uuid}")
    ResponseEntity<Void> deleteFloor(@PathVariable UUID uuid);




}
