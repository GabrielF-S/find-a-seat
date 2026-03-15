package com.gabsdev.findaseat.mapper;

import com.gabsdev.findaseat.dto.request.BusinessRequest;
import com.gabsdev.findaseat.dto.response.BusinessResponse;
import com.gabsdev.findaseat.model.Business;

public interface BusinessMapper {


    Business toBusiness(BusinessRequest business);

    BusinessResponse toBusinessResponse(Business businessSaved);
}
