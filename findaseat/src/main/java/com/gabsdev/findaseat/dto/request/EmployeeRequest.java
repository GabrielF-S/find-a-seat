package com.gabsdev.findaseat.dto.request;

import java.util.UUID;

public record EmployeeRequest(String employeeName,
                              String department,
                              String document) {
}
