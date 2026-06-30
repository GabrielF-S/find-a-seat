package com.gabsdev.findaseat.mapper;

import com.gabsdev.findaseat.dto.request.EmployeeRequest;
import com.gabsdev.findaseat.dto.response.EmployeeResponse;
import com.gabsdev.findaseat.model.entity.Business;
import com.gabsdev.findaseat.model.entity.Employee;

public interface EmployeeMapper {
    Employee toEmployee(EmployeeRequest employeeRequest, Business business);

    EmployeeResponse toEmployeeResponse(Employee saved);
}
