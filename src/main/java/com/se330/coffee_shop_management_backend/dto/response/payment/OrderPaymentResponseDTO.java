package com.se330.coffee_shop_management_backend.dto.response.payment;

import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.util.Constants.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentResponseDTO {
    private String orderPaymentId;
    private BigDecimal amount;
    private PaymentStatusEnum status;
    private String transactionId;
    private String failureReason;
    private String orderId;
    private String paymentMethodId;
    private String paypalPaymentId;
    private String paypalApprovalUrl;
    private String momoPayUrl;
    private String momoDeepLink;
    private int momoResultCode;
    private String vnpayPayUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderPaymentResponseDTO convert(OrderPayment orderPayment) {
        return OrderPaymentResponseDTO.builder()
                .orderPaymentId(orderPayment.getId().toString())
                .amount(orderPayment.getAmount())
                .status(orderPayment.getStatus())
                .transactionId(orderPayment.getTransactionId())
                .paypalPaymentId(orderPayment.getPaypalPaymentId())
                .paypalApprovalUrl(orderPayment.getPaypalApprovalUrl())
                .momoPayUrl(orderPayment.getMomoPayUrl())
                .momoDeepLink(orderPayment.getMomoDeepLink())
                .vnpayPayUrl(orderPayment.getVnpayPayUrl())
                .momoResultCode(orderPayment.getMomoResultCode())
                .failureReason(orderPayment.getFailureReason())
                .orderId(orderPayment.getOrder() != null ? orderPayment.getOrder().getId().toString() : null)
                .paymentMethodId(orderPayment.getPaymentMethod() != null ?
                        orderPayment.getPaymentMethod().getId().toString() : null)
                .createdAt(orderPayment.getCreatedAt())
                .updatedAt(orderPayment.getUpdatedAt())
                .build();
    }

    public static List<OrderPaymentResponseDTO> convert(List<OrderPayment> orderPayments) {
        if (orderPayments == null || orderPayments.isEmpty()) {
            return List.of();
        }

        return orderPayments.stream()
                .map(OrderPaymentResponseDTO::convert)
                .collect(Collectors.toList());
    }
}