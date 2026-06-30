package com.gabsdev.findaseat.mapper.impl;

import com.gabsdev.findaseat.dto.request.EmployeeRequest;
import com.gabsdev.findaseat.dto.response.EmployeeResponse;
import com.gabsdev.findaseat.mapper.EmployeeMapper;
import com.gabsdev.findaseat.model.entity.Business;
import com.gabsdev.findaseat.model.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapperImpl implements EmployeeMapper {
    @Override
    public Employee toEmployee(EmployeeRequest employeeRequest, Business business) {
        return  Employee.builder()
                .employeeName(employeeRequest.employeeName())
                .department(employeeRequest.department())
                .document(employeeRequest.document())
                .business(business).build();
    }

    @Override
    public EmployeeResponse toEmployeeResponse(Employee saved) {
        return new EmployeeResponse(saved.getId(), saved.getEmployeeName(), saved.getDepartment(), saved.getBusiness().getBusinessName());
    }
}
