package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.RequestBusiness;
import com.gabsdev.findaseat.dto.response.ResponseBusiness;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default server URL")})
public interface BusinessController {


    @PostMapping(value = "/create")
    ResponseEntity<ResponseBusiness> createBusiness(@RequestBody RequestBusiness business);

    @GetMapping(value = "/get/{uuid}")
    ResponseEntity<ResponseBusiness> getBusinessById(@PathVariable UUID uuid);

    @GetMapping(value = "/getAll")
    ResponseEntity<List<ResponseBusiness>> getAllBusiness();

    @PutMapping(value = "/update")
    ResponseEntity<ResponseBusiness> updateBusiness(@RequestBody RequestBusiness business);


    @DeleteMapping(value = "/delete")
    ResponseEntity<Void> deleteBusiness(@PathVariable UUID uuid);


}
