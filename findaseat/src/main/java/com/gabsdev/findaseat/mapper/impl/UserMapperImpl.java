package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import com.gabsdev.findaseat.mapper.UserMapper;
import com.gabsdev.findaseat.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toUser(RegisterUserRequest userToRegister) {

        User user = User.builder()
                .email(userToRegister.email())
                .password(userToRegister.password())
                .build();

        return user;
    }

    @Override
    public RegisterUserResponse toUserResponse(User saved) {
        return new RegisterUserResponse(saved.getEmployees().getEmployeeName(), saved.getEmail());
    }
}
