package com.se330.coffee_shop_management_backend.dto.request.cart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EmployeeCartRequestDTO {
    private UUID userId;
    List<CartDetailCreateRequestDTO> cartDetails;
}
