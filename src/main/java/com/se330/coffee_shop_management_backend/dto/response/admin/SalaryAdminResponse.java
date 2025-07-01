package com.se330.coffee_shop_management_backend.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class SalaryAdminResponse {
    // Mã định danh của bảng lương
    private String salaryId;
    // Mã định danh của nhân viên, khóa ngoại tham chiếu đến bảng nhân viên
    private String employeeId;
    // Tháng của bảng lương
    private int month;
    // Năm của bảng lương
    private int year;
    // Lương tháng của nhân viên
    private BigDecimal monthSalary;

    // Thời gian tạo bản ghi
    private LocalDateTime createdAt;
    // Thời gian cập nhật bản ghi gần nhất
    private LocalDateTime updatedAt;

    public static SalaryAdminResponse convert(com.se330.coffee_shop_management_backend.entity.Salary salary) {
        return SalaryAdminResponse.builder()
                .salaryId(salary.getId().toString())
                .employeeId(salary.getEmployee().getId().toString())
                .month(salary.getMonth())
                .year(salary.getYear())
                .monthSalary(salary.getMonthSalary())
                .createdAt(salary.getCreatedAt())
                .updatedAt(salary.getUpdatedAt())
                .build();
    }

    public static java.util.List<SalaryAdminResponse> convert(java.util.List<com.se330.coffee_shop_management_backend.entity.Salary> salaries) {
        if (salaries == null || salaries.isEmpty()) {
            return java.util.List.of();
        }
        return salaries.stream()
                .map(SalaryAdminResponse::convert)
                .toList();
    }
}
