package com.se330.coffee_shop_management_backend.dto.request.order;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public class EmployeeOrderRequestDTO {
    private UUID userId;
    private UUID paymentMethodId;
    List<CartDetailCreateRequestDTO> cartDetails;
}
