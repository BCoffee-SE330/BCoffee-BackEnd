package com.se330.coffee_shop_management_backend.dto.request.transfer;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
public class TransferCreateRequestDTO {
    private String transferDescription;
    private BigDecimal transferTotalCost;
    private UUID branchId;
    private UUID warehouseId;

    private List<TransferDetailCreateRequestDTO> transferDetails;
}
