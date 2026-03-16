package com.gabsdev.findaseat.controller.impl;


import com.gabsdev.findaseat.controller.BusinessController;
import com.gabsdev.findaseat.dto.request.BusinessRequest;
import com.gabsdev.findaseat.dto.response.BusinessResponse;
import com.gabsdev.findaseat.model.Business;
import com.gabsdev.findaseat.service.BusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<BusinessResponse> createBusiness(BusinessRequest business) {
        BusinessResponse businessResponse = service.createBusiness(business);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(businessResponse.id()).toUri();
        return  ResponseEntity.created(uri).build();

    }

    @Override
    public ResponseEntity<BusinessResponse> getBusinessById(UUID uuid) {

        return ResponseEntity.ok(service.getBusinessById(uuid));
    }

    @Override
    public ResponseEntity<List<BusinessResponse>> getAllBusiness() {
        return ResponseEntity.ok(service.getAllBusiness());
    }

    @Override
    public ResponseEntity<Business> updateBusiness(Business business) {

        return ResponseEntity.ok(service.updateBusiness(business));
    }

    @Override
    public ResponseEntity<Void> deleteBusiness(UUID uuid) {
        service.deleteBusiness(uuid);
        return ResponseEntity.noContent().build();
    }
}
