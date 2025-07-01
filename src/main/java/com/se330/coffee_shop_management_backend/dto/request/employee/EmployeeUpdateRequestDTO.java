package com.se330.coffee_shop_management_backend.dto.request.employee;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EmployeeUpdateRequestDTO {
    private UUID employeeId;
    private LocalDateTime employeeHireDate;
}