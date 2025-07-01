package com.se330.coffee_shop_management_backend.dto.response.salary;

import com.se330.coffee_shop_management_backend.entity.Salary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class SalaryResponseDTO {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of salary creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of salary update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String employeeId;
    private String employeeName;
    private String role;
    private int month;
    private int year;
    private BigDecimal monthSalary;

    public static SalaryResponseDTO convert(Salary salary) {
        return SalaryResponseDTO.builder()
                .id(salary.getId().toString())
                .createdAt(salary.getCreatedAt())
                .updatedAt(salary.getUpdatedAt())
                .employeeId(salary.getEmployee() != null ? salary.getEmployee().getId().toString() : null)
                .employeeName(salary.getEmployee() != null ? salary.getEmployee().getUser().getFullName() : null)
                .role(salary.getEmployee().getUser().getRole().getName().getValue())
                .month(salary.getMonth())
                .year(salary.getYear())
                .monthSalary(salary.getMonthSalary())
                .build();
    }

    public static List<SalaryResponseDTO> convert(List<Salary> salaries) {
        if (salaries == null || salaries.isEmpty()) {
            return List.of();
        }

        return salaries.stream()
                .map(SalaryResponseDTO::convert)
                .toList();
    }
}