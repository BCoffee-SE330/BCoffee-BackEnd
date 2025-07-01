package com.se330.coffee_shop_management_backend.dto.request.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TransferDetailUpdateRequestDTO {
    private UUID id;
    private int transferDetailQuantity;
    private String transferDetailUnit;
    private UUID ingredientId;

    @Schema(description = "this field could be null or random UUID if creating transfer detail through transfer")
    private UUID branchId;

    @Schema(description = "this field could be null or random UUID if creating transfer detail through transfer")
    private UUID transferId;
}
