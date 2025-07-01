package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Role;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class RoleAdminResponse {
    // Mã định danh của vai trò
    private String roleId;
    // Tên của vai trò
    // Hiện chỉ có ADMIN: quản trị viên hệ thống
    // EMPLOYEE: nhân viên chi nhánh
    // MANAGER: quản lý chi nhánh
    // CUSTOMER: khách hàng
    private Constants.RoleEnum name;

    public static RoleAdminResponse convert(Role role) {
        return RoleAdminResponse.builder()
                .roleId(role.getId().toString())
                .name(role.getName())
                .build();
    }

    public static List<RoleAdminResponse> convert(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        return roles.stream()
                .map(RoleAdminResponse::convert)
                .toList();
    }
}
