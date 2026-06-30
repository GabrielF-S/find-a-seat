package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import com.gabsdev.findaseat.mapper.UserMapper;
import com.gabsdev.findaseat.model.entity.User;

public class UserMapperImpl implements UserMapper {
    @Override
    public User toUser(RegisterUserRequest userToRegister) {

        User user = new User(userToRegister.email(), userToRegister.password(), userToRegister.roles());

        return user;
    }

    @Override
    public RegisterUserResponse toUserResponse(User saved) {
        return new RegisterUserResponse(saved.getEmployees().getEmployeeName(), saved.getEmail());
    }
}
