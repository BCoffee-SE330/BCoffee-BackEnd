package com.se330.coffee_shop_management_backend.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class TransferAdminResponse {
    // Mã định danh của phiếu chuyển kho
    private String transferId;
    // Mã định danh của kho hàng (nơi được chuyển đi), khóa ngoại tham chiếu đến bảng kho
    private String warehouseId;
    // Mã định danh của chi nhánh (nơi được chuyển đến), khóa ngoại tham chiếu đến bảng chi nhánh
    private String branchId;
    // Mô tả của phiếu chuyển kho
    private String transferDescription;
    // Mã theo dõi của phiếu chuyển kho
    private String transferTrackingNumber;
    // Tổng chi phí của phiếu chuyển kho
    private BigDecimal transferTotalCost;

    // Thời gian tạo phiếu chuyển kho
    private LocalDateTime createdTime;
    // Thời gian cập nhật phiếu chuyển kho gần nhất
    private LocalDateTime updatedTime;

    public static TransferAdminResponse convert(com.se330.coffee_shop_management_backend.entity.Transfer transfer) {
        return TransferAdminResponse.builder()
                .transferId(transfer.getId().toString())
                .warehouseId(transfer.getWarehouse().getId().toString())
                .branchId(transfer.getBranch().getId().toString())
                .transferDescription(transfer.getTransferDescription())
                .transferTrackingNumber(transfer.getTransferTrackingNumber())
                .transferTotalCost(transfer.getTransferTotalCost())
                .createdTime(transfer.getCreatedAt())
                .updatedTime(transfer.getUpdatedAt())
                .build();
    }

    public static java.util.List<TransferAdminResponse> convert(java.util.List<com.se330.coffee_shop_management_backend.entity.Transfer> transfers) {
        if (transfers == null || transfers.isEmpty()) {
            return java.util.List.of();
        }
        return transfers.stream()
                .map(TransferAdminResponse::convert)
                .toList();
    }
}
