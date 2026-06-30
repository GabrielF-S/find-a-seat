package com.gabsdev.findaseat.dto.request;

import java.util.List;

public record RegisterUserRequest(String email, String password, List<String> roles, Long employeeID) {
}
