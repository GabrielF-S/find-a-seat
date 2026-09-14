package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.config.TokenConfig;
import com.gabsdev.findaseat.dto.request.LoginRequest;
import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.LoginResponse;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import com.gabsdev.findaseat.exception.EmployeeNotFoundException;
import com.gabsdev.findaseat.mapper.UserMapper;
import com.gabsdev.findaseat.model.entity.Employee;
import com.gabsdev.findaseat.model.entity.User;
import com.gabsdev.findaseat.repository.EmployeeRepository;
import com.gabsdev.findaseat.repository.UserRepository;
import com.gabsdev.findaseat.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthServiceImpl(UserRepository repository,
                           UserMapper mapper,
                           EmployeeRepository employeeRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           TokenConfig tokenConfig) {
        this.repository = repository;
        this.mapper = mapper;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @Override
    public RegisterUserResponse createComumUser(RegisterUserRequest request) {
        Employee employee = employeeRepository.findById(request.employeeID())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        User user = createUser(employee, request.email(), request.password());
        user.setRoles(List.of("COMUM"));
        var savedUser = repository.save(user);
        return mapper.toUserResponse(savedUser);
    }

    @Override
    public RegisterUserResponse createSuperUser(RegisterUserRequest request) {
        Employee employee = employeeRepository.findById(request.employeeID())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        User user = createUser(employee, request.email(), request.password());
        user.setRoles(List.of("COMUM", "SUPER", "ADMIN"));
        var savedUser = repository.save(user);
        return mapper.toUserResponse(savedUser);
    }

    @Override
    public RegisterUserResponse createAdminUser(RegisterUserRequest request) {
        Employee employee = employeeRepository.findById(request.employeeID())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        User user = createUser(employee, request.email(), request.password());
        user.setRoles(List.of("COMUM", "SUPER", "ADMIN"));
        var savedUser = repository.save(user);
        return mapper.toUserResponse(savedUser);
    }

    @Override
    public User createUser(Employee employee, String email, String password) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .employees(employee).build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );
        Authentication authentication = authenticationManager.authenticate(userAndPass);
        User user = (User)authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return new LoginResponse(token);
    }
}
