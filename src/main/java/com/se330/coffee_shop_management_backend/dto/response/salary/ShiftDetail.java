package com.se330.coffee_shop_management_backend.dto.response.salary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class ShiftDetail {
    private String shiftId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String daysOfWeek;
    private BigDecimal shiftSalary;
    private int totalShiftCheckins;
    private BigDecimal totalShiftSalary;
}
