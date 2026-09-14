package com.gabsdev.findaseat.controller.impl;

import com.gabsdev.findaseat.controller.AuthController;
import com.gabsdev.findaseat.dto.request.LoginRequest;
import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.LoginResponse;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import com.gabsdev.findaseat.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("auth")
@RestController
public class AuthControllerImpl implements AuthController {
    private  final AuthService service;

    public AuthControllerImpl(AuthService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @Override
    public ResponseEntity<RegisterUserResponse> registerUserResponseResponseEntity(RegisterUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createComumUser(request));
    }

    @Override
    public ResponseEntity<RegisterUserResponse> registerAdminUser(RegisterUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAdminUser(request));
    }

    @Override
    public ResponseEntity<RegisterUserResponse> registerSuperUser(RegisterUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSuperUser(request));
    }


}
