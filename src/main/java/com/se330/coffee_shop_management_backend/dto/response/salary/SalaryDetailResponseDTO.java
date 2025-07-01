package com.se330.coffee_shop_management_backend.dto.response.salary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class SalaryDetailResponseDTO {
    private String salaryId;
    private String employeeId;
    private String employeeName;
    private String monthAndYear;
    private String role;
    private int totalCheckins;
    private int totalSubCheckins;
    private BigDecimal totalSalary;

    List<ShiftDetail> shiftDetails;
    List<SubShiftDetail> subShiftDetails;
}

