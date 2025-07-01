package com.se330.coffee_shop_management_backend.service.employeeservices;

import com.se330.coffee_shop_management_backend.dto.request.auth.RegisterRequest;
import com.se330.coffee_shop_management_backend.dto.request.employee.EmployeeUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindException;

import java.util.UUID;

public interface IEmployeeService {
    Employee findByIdEmployee(UUID id);
    Page<Employee> findAllEmployees(Pageable pageable);
    Page<Employee> findAllEmployeesByBranchId(Pageable pageable);
    Employee createEmployee(RegisterRequest request) throws BindException;
    Employee createBranchManager(RegisterRequest request, UUID branchId) throws BindException;
    Employee updateEmployee(EmployeeUpdateRequestDTO employeeUpdateRequestDTO);
    void deleteEmployee(UUID id);
}