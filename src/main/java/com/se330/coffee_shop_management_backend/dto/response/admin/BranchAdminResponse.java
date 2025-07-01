package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Branch;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class BranchAdminResponse {
    // Mã của chi nhánh
    private String branchId;
    // Tên cua chi nhánh
    private String branchName;
    // Thông tin địa chỉ chi nhánh
    private String branchAddress;
    // Thông tin liên hệ của chi nhánh
    private String branchPhone;
    // Email của chi nhánh
    private String branchEmail;
    // Mã của nhân viên quản lý chi nhánh, khóa ngoại tham chiếu đến bảng nhân viên
    private String managerId;

    public static BranchAdminResponse convert(Branch branch) {
        return BranchAdminResponse.builder()
                .branchId(branch.getId().toString())
                .branchName(branch.getBranchName())
                .branchAddress(branch.getBranchAddress())
                .branchPhone(branch.getBranchPhone())
                .branchEmail(branch.getBranchEmail())
                .managerId(branch.getManager().getId().toString())
                .build();
    }

    public static List<BranchAdminResponse> convert(List<Branch> branches) {
        if (branches == null || branches.isEmpty()) {
            return List.of();
        }

        return branches.stream()
                .map(BranchAdminResponse::convert)
                .toList();
    }
}
