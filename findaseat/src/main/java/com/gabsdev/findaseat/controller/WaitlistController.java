package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.response.WaitlistResponse;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default server URL")})
public interface WaitlistController {

    @PostMapping("/confirmwaitlist/{id}")
    ResponseEntity<WaitlistResponse> confirmWaitlit(@RequestParam(name = "id") Long id, @RequestBody ReservationStatus status);
}
