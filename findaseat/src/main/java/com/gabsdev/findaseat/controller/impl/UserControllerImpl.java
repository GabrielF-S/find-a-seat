package com.gabsdev.findaseat.controller.impl;

import com.gabsdev.findaseat.controller.UserController;
import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import com.gabsdev.findaseat.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerImpl implements UserController {
    private final UserService service;

    public UserControllerImpl(UserService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<RegisterUserResponse> registerUser(RegisterUserRequest userToRegister) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(userToRegister));
    }

}
