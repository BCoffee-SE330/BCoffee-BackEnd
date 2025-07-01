package com.se330.coffee_shop_management_backend.dto.request.shift;

import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ShiftUpdateRequestDTO {
    private UUID shiftId;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private Constants.DayOfWeekEnum dayOfWeek;
    private int month;
    private int year;
    private BigDecimal shiftSalary;
    private UUID employeeId;
}
