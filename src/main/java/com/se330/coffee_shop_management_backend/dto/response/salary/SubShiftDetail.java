package com.se330.coffee_shop_management_backend.dto.response.salary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalTime;
@Data
@NoArgsConstructor
@SuperBuilder
public class SubShiftDetail {
    private String subShiftId;
    private String absentEmployeeId;
    private String absentEmployeeName;
    private String daysOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal subShiftSalary;
    private int totalSubShiftCheckins;
    private BigDecimal totalSubShiftSalary;
}
