package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class ShiftAdminResponse {
    // Mã định danh của ca làm việc
    private String shiftId;
    // Thời gian bắt đầu của ca làm việc
    private LocalTime shiftStartTime;
    // Thời gian kết thúc của ca làm việc
    private LocalTime shiftEndTime;
    // Mã định danh của nhân viên, khóa ngoại tham chiếu đến bảng nhân viên
    private String employeeId;
    // Ngày trong tuần của ca làm việc, bao gồm
    // MONDAY: THỨ HAI
    // TUESDAY: THỨ BA
    // WEDNESDAY: THỨ TƯ
    // THURSDAY: THỨ NĂM
    // FRIDAY: THỨ SÁU
    // SATURDAY: THỨ BẢY
    // SUNDAY: CHỦ NHẬT
    private Constants.DayOfWeekEnum dayOfWeek;
    // Tháng của ca làm việc
    private int month;
    // Năm của ca làm việc
    private int year;
    // Lương của ca làm việc
    private BigDecimal shiftSalary;

    public static ShiftAdminResponse convert(com.se330.coffee_shop_management_backend.entity.Shift shift) {
        return ShiftAdminResponse.builder()
                .shiftId(shift.getId().toString())
                .shiftStartTime(shift.getShiftStartTime())
                .shiftEndTime(shift.getShiftEndTime())
                .employeeId(shift.getEmployee() != null ? shift.getEmployee().getId().toString() : null)
                .dayOfWeek(shift.getDayOfWeek())
                .month(shift.getMonth())
                .year(shift.getYear())
                .shiftSalary(shift.getShiftSalary())
                .build();
    }

    public static java.util.List<ShiftAdminResponse> convert(java.util.List<com.se330.coffee_shop_management_backend.entity.Shift> shifts) {
        if (shifts == null || shifts.isEmpty()) {
            return java.util.List.of();
        }
        return shifts.stream()
                .map(ShiftAdminResponse::convert)
                .toList();
    }
}
