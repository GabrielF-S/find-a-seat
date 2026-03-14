package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.RequestBusiness;
import com.gabsdev.findaseat.dto.response.ResponseBusiness;
import com.gabsdev.findaseat.exception.BusinessNotFoundException;
import com.gabsdev.findaseat.mapper.BusinessMapper;
import com.gabsdev.findaseat.model.Business;
import com.gabsdev.findaseat.repository.BusinessRepository;
import com.gabsdev.findaseat.service.BusinessService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository repository;
    private final BusinessMapper mapper;

    public BusinessServiceImpl(BusinessRepository repository, BusinessMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ResponseBusiness createBusiness(RequestBusiness business) {
        Business businessToSave = mapper.toBusiness(business);
        Business businessSaved = repository.save(businessToSave);
        return mapper.toBusinessResponse(businessSaved) ;
    }

    @Override
    public ResponseBusiness getBusinessById(UUID uuid) {
        Business business = repository.findById(uuid).orElseThrow(() -> new BusinessNotFoundException("Business Not Found"));
        return mapper.toBusinessResponse(business);
    }

    @Override
    public List<ResponseBusiness> getAllBusiness() {
        return  repository.findAll().stream().map(mapper::toBusinessResponse).toList();
    }
}
