package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.BusinessRequest;
import com.gabsdev.findaseat.dto.response.BusinessResponse;
import com.gabsdev.findaseat.model.Business;

import java.util.List;
import java.util.UUID;


public interface BusinessService {


    BusinessResponse createBusiness(BusinessRequest business);


    BusinessResponse getBusinessById(UUID uuid);

    List<BusinessResponse> getAllBusiness();

    Business updateBusiness(Business business);

    void deleteBusiness(UUID uuid);
}
