package com.gabsdev.findaseat.controller.impl;


import com.gabsdev.findaseat.controller.BusinessController;
import com.gabsdev.findaseat.dto.request.RequestBusiness;
import com.gabsdev.findaseat.dto.response.ResponseBusiness;
import com.gabsdev.findaseat.service.BusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/business")
public class BusinessControllerImpl implements BusinessController {

    private final BusinessService service;

    public BusinessControllerImpl(BusinessService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ResponseBusiness> createBusiness(RequestBusiness business) {
        return  new ResponseEntity<>(service.createBusiness(business), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseBusiness> getBusinessById(UUID uuid) {

        return ResponseEntity.ok(service.getBusinessById(uuid));
    }

    @Override
    public ResponseEntity<List<ResponseBusiness>> getAllBusiness() {
        return ResponseEntity.ok(service.getAllBusiness());
    }

    @Override
    public ResponseEntity<ResponseBusiness> updateBusiness(RequestBusiness business) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteBusiness(UUID uuid) {
        return null;
    }
}
