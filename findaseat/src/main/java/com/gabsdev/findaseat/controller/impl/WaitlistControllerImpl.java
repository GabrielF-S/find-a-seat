package com.gabsdev.findaseat.controller.impl;

import com.gabsdev.findaseat.controller.WaitlistController;
import com.gabsdev.findaseat.dto.response.WaitlistResponse;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import com.gabsdev.findaseat.service.WaitlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/waitlist")
public class WaitlistControllerImpl implements WaitlistController {

    private final WaitlistService service;

    public WaitlistControllerImpl(WaitlistService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<WaitlistResponse> confirmWaitlit(Long waitlistId ,ReservationStatus status) {
        return ResponseEntity.ok(service.updateStatus(waitlistId ,status));
    }
}
