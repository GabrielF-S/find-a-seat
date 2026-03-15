package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.request.BusinessRequest;
import com.gabsdev.findaseat.dto.response.BusinessResponse;
import com.gabsdev.findaseat.mapper.BusinessMapper;
import com.gabsdev.findaseat.model.Business;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapperImpl implements BusinessMapper {
    @Override
    public Business toBusiness(BusinessRequest business) {
        Business newBusiness = new Business(business.businessName(), business.location());
        return newBusiness;
    }

    @Override
    public BusinessResponse toBusinessResponse(Business businessSaved) {

        BusinessResponse business = new BusinessResponse(businessSaved.getUuid(),
                businessSaved.getBusinessName(),
                businessSaved.getLocation());

        return business;
    }
}
