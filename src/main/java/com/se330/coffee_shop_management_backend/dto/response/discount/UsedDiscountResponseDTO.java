package com.se330.coffee_shop_management_backend.dto.response.discount;

import com.se330.coffee_shop_management_backend.entity.UsedDiscount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class UsedDiscountResponseDTO {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of last update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    @Schema(description = "Associated order detail ID")
    private String orderDetailId;

    @Schema(description = "Applied discount ID")
    private String discountId;

    @Schema(description = "Number of times discount was used")
    private int timesUse;

    public static UsedDiscountResponseDTO convert(UsedDiscount usedDiscount) {
        if (usedDiscount == null) {
            return null;
        }

        return UsedDiscountResponseDTO.builder()
                .id(usedDiscount.getId().toString())
                .createdAt(usedDiscount.getCreatedAt())
                .updatedAt(usedDiscount.getUpdatedAt())
                .orderDetailId(usedDiscount.getOrderDetail() != null ?
                        usedDiscount.getOrderDetail().getId().toString() : null)
                .discountId(usedDiscount.getDiscount() != null ?
                        usedDiscount.getDiscount().getId().toString() : null)
                .timesUse(usedDiscount.getTimesUse())
                .build();
    }

    public static List<UsedDiscountResponseDTO> convert(List<UsedDiscount> usedDiscounts) {
        if (usedDiscounts == null || usedDiscounts.isEmpty()) {
            return Collections.emptyList();
        }

        return usedDiscounts.stream()
                .map(UsedDiscountResponseDTO::convert)
                .collect(Collectors.toList());
    }
}