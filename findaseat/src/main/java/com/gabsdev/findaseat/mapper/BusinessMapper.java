package com.gabsdev.findaseat.mapper;

import com.gabsdev.findaseat.dto.request.RequestBusiness;
import com.gabsdev.findaseat.dto.response.ResponseBusiness;
import com.gabsdev.findaseat.model.Business;

public interface BusinessMapper {


    Business toBusiness(RequestBusiness business);

    ResponseBusiness toBusinessResponse(Business businessSaved);
}
