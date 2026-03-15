package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.BusinessRequest;
import com.gabsdev.findaseat.dto.response.BusinessResponse;
import com.gabsdev.findaseat.exception.BusinessNotFoundException;
import com.gabsdev.findaseat.mapper.BusinessMapper;
import com.gabsdev.findaseat.model.Business;
import com.gabsdev.findaseat.repository.BusinessRepository;
import com.gabsdev.findaseat.service.BusinessService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository repository;
    private final BusinessMapper mapper;

    public BusinessServiceImpl(BusinessRepository repository, BusinessMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public BusinessResponse createBusiness(BusinessRequest business) {
        Business businessToSave = mapper.toBusiness(business);
        Business businessSaved = repository.save(businessToSave);
        return mapper.toBusinessResponse(businessSaved) ;
    }

    @Override
    public BusinessResponse getBusinessById(UUID uuid) {
        Business business = repository.findById(uuid).orElseThrow(() -> new BusinessNotFoundException("Business Not Found"));
        return mapper.toBusinessResponse(business);
    }

    @Override
    public List<BusinessResponse> getAllBusiness() {
        return  repository.findAll().stream().map(mapper::toBusinessResponse).toList();
    }

    @Override
    public Business updateBusiness(Business business) {
        if (!repository.existsById(business.getUuid())){
            throw new BusinessNotFoundException("Business Not Found");
        }
        return repository.save(business);
    }

    @Override
    public void deleteBusiness(UUID uuid) {
        if (!repository.existsById(uuid)){
            throw new BusinessNotFoundException("Business Not Found");
        }
        repository.deleteById(uuid);

    }
}
