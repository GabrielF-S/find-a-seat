package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.RequestBusiness;
import com.gabsdev.findaseat.dto.response.ResponseBusiness;

import java.util.List;
import java.util.UUID;


public interface BusinessService {


    ResponseBusiness createBusiness(RequestBusiness business);


    ResponseBusiness getBusinessById(UUID uuid);

    List<ResponseBusiness> getAllBusiness();
}
