package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.BusinessRequest;
import com.gabsdev.findaseat.dto.response.BusinessResponse;
import com.gabsdev.findaseat.model.Business;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default server URL")})
public interface BusinessController {


    @PostMapping(value = "/create")
    ResponseEntity<BusinessResponse> createBusiness(@RequestBody BusinessRequest business);

    @GetMapping(value = "/get/{uuid}")
    ResponseEntity<BusinessResponse> getBusinessById(@PathVariable UUID uuid);

    @GetMapping(value = "/getAll")
    ResponseEntity<List<BusinessResponse>> getAllBusiness();

    @PutMapping(value = "/update")
    ResponseEntity<Business> updateBusiness(@RequestBody Business business);


    @DeleteMapping(value = "/delete/{uuid}")
    ResponseEntity<Void> deleteBusiness(@PathVariable UUID uuid);


}
