package com.se330.coffee_shop_management_backend.dto.request.shippingaddresses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ShippingAddressesCreateRequestDTO {
    private String addressLine;
    private String addressCity;
    private String addressDistrict;
    private boolean addressIsDefault;
}
