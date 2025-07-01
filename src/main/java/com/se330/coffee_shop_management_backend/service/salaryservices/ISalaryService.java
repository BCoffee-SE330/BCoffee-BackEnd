package com.se330.coffee_shop_management_backend.service.salaryservices;

import com.se330.coffee_shop_management_backend.dto.request.salary.SalaryCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.salary.SalaryUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.salary.SalaryDetailResponseDTO;
import com.se330.coffee_shop_management_backend.entity.Salary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ISalaryService {
    Salary findById(UUID id);
    Page<Salary> findAll(Pageable pageable);
    Page<Salary> findAllByBranch(Pageable pageable);
    Page<Salary> findAllByBranchAndMonthAndYear(Pageable pageable, int month, int year);
    Salary create(SalaryCreateRequestDTO salaryCreateRequestDTO);
    Salary update(SalaryUpdateRequestDTO salaryUpdateRequestDTO);
    SalaryDetailResponseDTO findSalaryDetailById(UUID id);
    SalaryDetailResponseDTO findMySalaryDetailByMonthAndYear(int month, int year);
    void updateSalaryForAllEmployeesInBranchInMonthAndYear(int month, int year);
    void delete(UUID id);
}