package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


public interface UserService extends UserDetailsService {

    RegisterUserResponse createUser(RegisterUserRequest userToRegister);
}
