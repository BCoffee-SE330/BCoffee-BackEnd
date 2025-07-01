package com.se330.coffee_shop_management_backend.dto.response.employee;

import com.se330.coffee_shop_management_backend.entity.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class EmployeeResponseDTO {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of employee creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of employee update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;
    private LocalDateTime employeeHireDate;

    // Related entities
    private String branchId;
    private String branchName;
    private String userId;
    private String userFullName;
    private String userAvatarUrl;
    private LocalDateTime userDoB;
    private String userPhone;
    private String userGender;
    private String userEmail;
    private String userRole;
    private String managedBranchId;

    public static EmployeeResponseDTO convert(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId().toString())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .employeeHireDate(employee.getEmployeeHireDate())
                .managedBranchId(employee.getManagedBranch() != null ? employee.getManagedBranch().getId().toString() : null)
                .branchId(employee.getBranch() != null ? employee.getBranch().getId().toString() : null)
                .userId(employee.getUser() != null ? employee.getUser().getId().toString() : null)
                .userFullName(employee.getUser() != null ? employee.getUser().getFullName() : null)
                .userAvatarUrl(employee.getUser() != null ? employee.getUser().getAvatar() : null)
                .userDoB(employee.getUser() != null ? employee.getUser().getBirthDate() : null)
                .userPhone(employee.getUser() != null ? employee.getUser().getPhoneNumber() : null)
                .userGender(employee.getUser() != null ? employee.getUser().getGender() : null)
                .userEmail(employee.getUser() != null ? employee.getUser().getEmail() : null)
                .userRole(employee.getUser() != null && employee.getUser().getRole() != null ? employee.getUser().getRole().getName().getValue() : null)
                .branchName(employee.getBranch() != null ? employee.getBranch().getBranchName() : null)
                .build();
    }

    public static List<EmployeeResponseDTO> convert(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return Collections.emptyList();
        }

        return employees.stream()
                .map(EmployeeResponseDTO::convert)
                .collect(Collectors.toList());
    }
}