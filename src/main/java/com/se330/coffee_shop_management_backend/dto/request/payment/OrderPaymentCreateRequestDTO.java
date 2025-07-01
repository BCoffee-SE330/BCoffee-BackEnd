package com.se330.coffee_shop_management_backend.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPaymentCreateRequestDTO {
    private UUID orderId;
    private UUID paymentMethodId;
    private BigDecimal amount;
    private String transactionId;
    private String failureReason;
}
