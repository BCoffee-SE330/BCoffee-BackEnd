package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class EmployeeAdminResponse {
    // Mã nhân viên
    private String employeeId;
    // Thời gian tạo
    private LocalDateTime createdAt;
    // Thời gian cập nhật gần nhất
    private LocalDateTime updatedAt;
    // Ngày tuyển dụng
    private LocalDateTime employeeHireDate;
    // Mã chi nhánh nhân viên làm việc
    private String branchId;
    // Mã người dùng của nhân viên
    private String userId;
    // Mã chi nhánh mà nhân viên quản lý (nếu nhân viên là quản lý chi nhánh)
    private String managedBranchId;

    public static EmployeeAdminResponse convert(Employee employee) {
        return EmployeeAdminResponse.builder()
                .employeeId(employee.getId().toString())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .employeeHireDate(employee.getEmployeeHireDate())
                .branchId(employee.getBranch() != null ? employee.getBranch().getId().toString() : null)
                .userId(employee.getUser() != null ? employee.getUser().getId().toString() : null)
                .managedBranchId(employee.getManagedBranch() != null ? employee.getManagedBranch().getId().toString() : null)
                .build();
    }

    public static List<EmployeeAdminResponse> convert(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return List.of();
        }
        return employees.stream()
                .map(EmployeeAdminResponse::convert)
                .toList();
    }
}