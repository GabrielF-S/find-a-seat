package com.gabsdev.findaseat.controller.impl;

import com.gabsdev.findaseat.controller.AuthController;
import com.gabsdev.findaseat.service.AuthService;
import org.springframework.http.ResponseEntity;

public class AuthControllerImpl implements AuthController {
    private  final AuthService service;

    public AuthControllerImpl(AuthService service) {
        this.service = service;
    }

//    @Override
//    public ResponseEntity<LoginResponse> login(LoginRequest login) {
//
//        return ResponseEntity.ok(service.login(login));
//    }
}
