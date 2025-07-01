package com.se330.coffee_shop_management_backend.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class SupplierAdminResponse {
    // Mã định danh của nhà cung cấp
    private String supplierId;
    // Tên của nhà cung cấp
    private String supplierName;
    // Số điện thoại của nhà cung cấp
    private String supplierPhone;
    // Email của nhà cung cấp
    private String supplierEmail;
    // Địa chỉ của nhà cung cấp
    private String supplierAddress;

    // Thời gian tạo bản ghi nhà cung cấp
    private LocalDateTime createdAt;
    // Thời gian cập nhật bản ghi nhà cung cấp gần nhất
    private LocalDateTime updatedAt;

    public static SupplierAdminResponse convert(com.se330.coffee_shop_management_backend.entity.Supplier supplier) {
        return SupplierAdminResponse.builder()
                .supplierId(supplier.getId().toString())
                .supplierName(supplier.getSupplierName())
                .supplierPhone(supplier.getSupplierPhone())
                .supplierEmail(supplier.getSupplierEmail())
                .supplierAddress(supplier.getSupplierAddress())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }

    public static java.util.List<SupplierAdminResponse> convert(java.util.List<com.se330.coffee_shop_management_backend.entity.Supplier> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            return java.util.List.of();
        }
        return suppliers.stream()
                .map(SupplierAdminResponse::convert)
                .toList();
    }
}
