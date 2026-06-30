package com.gabsdev.findaseat.mapper;

import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import com.gabsdev.findaseat.model.entity.User;

public interface UserMapper {
    User toUser(RegisterUserRequest userToRegister);

    RegisterUserResponse toUserResponse(User saved);
}
