package com.se330.coffee_shop_management_backend.dto.response.supplier;

import com.se330.coffee_shop_management_backend.entity.Supplier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class SupplierResponseDTO {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of shift creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of shift update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String supplierName;
    private String supplierPhone;
    private String supplierEmail;
    private String supplierAddress;

    public static SupplierResponseDTO convert(Supplier supplier) {
        return SupplierResponseDTO.builder()
                .id(supplier.getId().toString())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .supplierName(supplier.getSupplierName())
                .supplierPhone(supplier.getSupplierPhone())
                .supplierEmail(supplier.getSupplierEmail())
                .supplierAddress(supplier.getSupplierAddress())
                .build();
    }

    public static List<SupplierResponseDTO> convert(List<Supplier> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            return List.of();
        }

        return suppliers.stream()
                .map(SupplierResponseDTO::convert)
                .toList();
    }
}
