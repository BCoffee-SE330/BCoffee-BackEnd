package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.TransferDetail;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class TransferDetailAdminResponse {
    // Mã của phiếu chuyển kho, khóa ngoại tham chiếu đến Transfer
    private String transferId;
    // Mã của nguyên liệu, khóa ngoại tham chiếu đến Ingredient
    private String ingredientId;
    // Số lượng nguyên liệu trong phiếu chuyển kho
    private int transferDetailQuantity;
    // Đơn vị của nguyên liệu trong phiếu chuyển kho (gram hoặc ml)
    private String transferDetailUnit;

    public static TransferDetailAdminResponse convert(TransferDetail transferDetail) {
        return TransferDetailAdminResponse.builder()
                .transferId(transferDetail.getTransfer().getId().toString())
                .ingredientId(transferDetail.getIngredient().getId().toString())
                .transferDetailQuantity(transferDetail.getTransferDetailQuantity())
                .transferDetailUnit(transferDetail.getTransferDetailUnit())
                .build();
    }

    public static List<TransferDetailAdminResponse> convert(List<TransferDetail> transferDetails) {
        if (transferDetails == null || transferDetails.isEmpty()) {
            return List.of();
        }
        return transferDetails.stream()
                .map(TransferDetailAdminResponse::convert)
                .toList();
    }
}
