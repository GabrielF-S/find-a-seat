package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.dto.response.LayoutResponse;
import com.gabsdev.findaseat.model.entity.Floor;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default server URL")})
public interface FloorsController {

    @PostMapping()
    ResponseEntity<FloorResponse> createFloor(@RequestBody FloorRequest request);

    @GetMapping(value = "/{uuid}")
    ResponseEntity<FloorResponse> getFloorById(@PathVariable UUID uuid);

    @GetMapping(value = "/{businessUuid}/getAll")
    ResponseEntity<Page<FloorResponse>> getAllFloors(@PathVariable UUID businessUuid,
                                                     @RequestParam(value="page", defaultValue = "0") Integer page,
                                                     @RequestParam(value="size", defaultValue = "10") Integer size);

    @GetMapping(value = "/getLayout/{uuid}")
    ResponseEntity<LayoutResponse> getLayoutByFloorUuid(@PathVariable UUID uuid);


    @PutMapping(value = "/update")
    ResponseEntity<FloorResponse> updateFloor(@RequestBody Floor floor);

    @PatchMapping(value = "insertLayout/{uuid}")
    ResponseEntity<FloorResponse> insertLayout(@PathVariable UUID uuid ,@RequestBody String layout);


    @DeleteMapping(value = "/delete/{uuid}")
    ResponseEntity<Void> deleteFloor(@PathVariable UUID uuid);




}
