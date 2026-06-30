package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserController {

    @PostMapping("/register")
    ResponseEntity<RegisterUserResponse> registerUser(@RequestBody RegisterUserRequest userToRegister);


}
