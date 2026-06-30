package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.EmployeeRequest;
import com.gabsdev.findaseat.dto.response.EmployeeResponse;
import com.gabsdev.findaseat.exception.BusinessNotFoundException;
import com.gabsdev.findaseat.exception.EmployeeNotFoundException;
import com.gabsdev.findaseat.mapper.EmployeeMapper;
import com.gabsdev.findaseat.model.entity.Business;
import com.gabsdev.findaseat.model.entity.Employee;
import com.gabsdev.findaseat.repository.BusinessRepository;
import com.gabsdev.findaseat.repository.EmployeeRepository;
import com.gabsdev.findaseat.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final BusinessRepository businessRepository;
    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public EmployeeServiceImpl(BusinessRepository businessRepository,
                               EmployeeRepository repository,
                               EmployeeMapper mapper) {
        this.businessRepository = businessRepository;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        Business business = getBusiness(employeeRequest);
        Employee employeeToSave = mapper.toEmployee(employeeRequest, business);
        Employee saved = repository.save(employeeToSave);
        return mapper.toEmployeeResponse(saved);
    }

    private Business getBusiness(EmployeeRequest employeeRequest) {
        return businessRepository.findById(employeeRequest.businessId())
                .orElseThrow(
                        () -> new BusinessNotFoundException("Business" +
                                employeeRequest.businessId() + " Not Found"));
    }

    @Override
    public List<EmployeeResponse> getEmployees(String name) {

        List<Employee> employeeList = repository.findByEmployeeName("%" + name + "%").get();
        if (employeeList.isEmpty()){
            throw  new EmployeeNotFoundException("Não foi possivel localizar um funcionario com o nome"+ name);
        }
        return employeeList.stream().map(mapper::toEmployeeResponse).toList();
    }

    @Override
    public EmployeeResponse getEmployeeyId(UUID businessUuid, Long employeeId) {
        verifyBusinessByUuid(businessUuid);
        Employee employee = repository.findById(employeeId).orElseThrow(
                () -> new EmployeeNotFoundException("Não foi possivel localizar um funcionario com este id")
        );

        return mapper.toEmployeeResponse(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)){
            throw new EmployeeNotFoundException("Não foi possivel localizar um funcionario com este id");
        }
         repository.deleteById(id);
    }

    private void verifyBusinessByUuid(UUID businessUuid) {

        if (!businessRepository.existsById(businessUuid)){
            throw new  BusinessNotFoundException("Business" +
                    businessUuid + " Not Found");
        }
    }
}
