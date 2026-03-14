package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.request.RequestBusiness;
import com.gabsdev.findaseat.dto.response.ResponseBusiness;
import com.gabsdev.findaseat.mapper.BusinessMapper;
import com.gabsdev.findaseat.model.Business;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapperImpl implements BusinessMapper {
    @Override
    public Business toBusiness(RequestBusiness business) {
        Business newBusiness = new Business(business.businessName(), business.location());
        return newBusiness;
    }

    @Override
    public ResponseBusiness toBusinessResponse(Business businessSaved) {

        ResponseBusiness business = new ResponseBusiness(businessSaved.getUuid(),
                businessSaved.getBusinessName(),
                businessSaved.getLocation());

        return business;
    }
}
