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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest, UUID businessUuid) {
        Business business = getBusiness(businessUuid);
        Employee employeeToSave = mapper.toEmployee(employeeRequest, business);
        Employee saved = repository.save(employeeToSave);
        return mapper.toEmployeeResponse(saved);
    }

    private Business getBusiness(UUID uuid) {
        return businessRepository.findById(uuid)
                .orElseThrow(
                        () -> new BusinessNotFoundException("Business" +
                                uuid + " Not Found"));
    }

    @Override
    public Page<EmployeeResponse> getEmployees(UUID businessUuid, String name, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "employee_name");
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        name = name.trim();
        name = name.replaceAll(" ", " & ");

        System.out.println(name);

        Page<Employee> employeeList = repository.findByEmployeeName(name, businessUuid, pageRequest);

        return employeeList.map(mapper::toEmployeeResponse);
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

    @Override
    public Page<EmployeeResponse> getAllEmployees(UUID businessId, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "employeeName");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Employee> employeePage = repository.findByBusiness_Uuid(businessId, pageRequest);
        return employeePage.map(mapper::toEmployeeResponse);
    }

    private void verifyBusinessByUuid(UUID businessUuid) {
        if (!businessRepository.existsById(businessUuid)){
            throw new  BusinessNotFoundException("Business" +
                    businessUuid + " Not Found");
        }
    }
}
