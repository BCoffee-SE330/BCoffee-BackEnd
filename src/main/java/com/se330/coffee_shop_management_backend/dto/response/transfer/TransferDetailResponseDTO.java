package com.se330.coffee_shop_management_backend.dto.response.transfer;

import com.se330.coffee_shop_management_backend.entity.TransferDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class TransferDetailResponseDTO {
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

    private int transferDetailQuantity;
    private String transferDetailUnit;
    private String ingredientId;
    private String transferId;

    public static TransferDetailResponseDTO convert(TransferDetail transferDetail) {
        return TransferDetailResponseDTO.builder()
                .id(transferDetail.getId().toString())
                .createdAt(transferDetail.getCreatedAt())
                .updatedAt(transferDetail.getUpdatedAt())
                .transferDetailQuantity(transferDetail.getTransferDetailQuantity())
                .transferDetailUnit(transferDetail.getTransferDetailUnit())
                .ingredientId(transferDetail.getIngredient().getId().toString())
                .transferId(transferDetail.getTransfer().getId().toString())
                .build();
    }

    public static List<TransferDetailResponseDTO> convert(List<TransferDetail> transferDetails) {
        if (transferDetails == null || transferDetails.isEmpty()) {
            return List.of();
        }

        return transferDetails.stream()
                .map(TransferDetailResponseDTO::convert)
                .toList();
    }
}
