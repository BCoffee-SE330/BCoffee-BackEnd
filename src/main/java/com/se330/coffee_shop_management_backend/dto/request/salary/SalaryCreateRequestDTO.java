package com.se330.coffee_shop_management_backend.dto.request.salary;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class SalaryCreateRequestDTO {
    private UUID employeeId;
    private int month;
    private int year;
    private BigDecimal monthSalary;
}