package com.se330.coffee_shop_management_backend.dto.response.transfer;

import com.se330.coffee_shop_management_backend.entity.Transfer;
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
public class TransferResponseDTO{
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

    private String transferDescription;
    private String transferTrackingNumber;
    private BigDecimal transferTotalCost;
    private String branchId;
    private String warehouseId;

    public static TransferResponseDTO convert(Transfer transfer) {
        return TransferResponseDTO.builder()
                .id(transfer.getId().toString())
                .createdAt(transfer.getCreatedAt())
                .updatedAt(transfer.getUpdatedAt())
                .transferDescription(transfer.getTransferDescription())
                .transferTrackingNumber(transfer.getTransferTrackingNumber())
                .transferTotalCost(transfer.getTransferTotalCost())
                .branchId(transfer.getBranch().getId().toString())
                .warehouseId(transfer.getWarehouse().getId().toString())
                .build();
    }

    public static List<TransferResponseDTO> convert(List<Transfer> transfers) {
        if (transfers == null || transfers.isEmpty()) {
            return List.of();
        }

        return transfers.stream()
                .map(TransferResponseDTO::convert)
                .toList();
    }
}
