package com.se330.coffee_shop_management_backend.dto.response.shippingadresses;

import com.se330.coffee_shop_management_backend.entity.ShippingAddresses;
import com.se330.coffee_shop_management_backend.entity.User;
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
public class ShippingAddressesResponseDTO {
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

    private String userId;
    private String addressLine;
    private String addressCity;
    private String addressDistrict;
    private boolean addressIsDefault;

    public static ShippingAddressesResponseDTO convert(ShippingAddresses shippingAddresses) {
        User existingUser = shippingAddresses.getUser();
        if (existingUser == null)
            throw new RuntimeException("How the fuck do you even get here?");

        return ShippingAddressesResponseDTO.builder()
                .id(shippingAddresses.getId().toString())
                .createdAt(shippingAddresses.getCreatedAt())
                .updatedAt(shippingAddresses.getUpdatedAt())
                .userId(existingUser.getId().toString())
                .addressLine(shippingAddresses.getAddressLine())
                .addressCity(shippingAddresses.getAddressCity())
                .addressDistrict(shippingAddresses.getAddressDistrict())
                .addressIsDefault(shippingAddresses.isAddressIsDefault())
                .build();
    }

    public static List<ShippingAddressesResponseDTO> convert(List<ShippingAddresses> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        return list.stream()
                .map(ShippingAddressesResponseDTO::convert)
                .collect(Collectors.toList());
    }
}
