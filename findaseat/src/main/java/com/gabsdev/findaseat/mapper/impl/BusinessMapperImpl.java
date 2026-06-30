package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.request.BusinessRequest;
import com.gabsdev.findaseat.dto.response.BusinessResponse;
import com.gabsdev.findaseat.mapper.BusinessMapper;
import com.gabsdev.findaseat.model.entity.Business;
import com.gabsdev.findaseat.model.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapperImpl implements BusinessMapper {
    @Override
    public Business toBusiness(BusinessRequest business) {
               return Business.builder()
                .businessName(business.businessName())
                .location(business.location())
                .businessType(business.businessType())
                .build();
    }

    @Override
    public BusinessResponse toBusinessResponse(Business businessSaved) {

        BusinessResponse business = new BusinessResponse(businessSaved.getUuid(),
                businessSaved.getBusinessName(),
                businessSaved.getLocation(), businessSaved.getBusinessType());

        return business;
    }
}
