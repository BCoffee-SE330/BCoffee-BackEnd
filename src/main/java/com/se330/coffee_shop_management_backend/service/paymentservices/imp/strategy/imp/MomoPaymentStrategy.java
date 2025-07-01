package com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.imp;

import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.payment.MomoIPNRequest;
import com.se330.coffee_shop_management_backend.dto.response.payment.MomoResponse;
import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.repository.OrderPaymentRepository;
import com.se330.coffee_shop_management_backend.repository.OrderRepository;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.PaymentStrategy;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.services.MomoService;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MomoPaymentStrategy implements PaymentStrategy {

    private final OrderRepository orderRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final MomoService momoService;

    @Override
    public OrderPayment pay(OrderPaymentCreateRequestDTO paymentRequest) {
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + paymentRequest.getOrderId()));

        PaymentMethods paymentMethod = paymentMethodsRepository.findById(paymentRequest.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found with id: " + paymentRequest.getPaymentMethodId()));

        MomoResponse momoResponse = momoService.createQR(order.getId(), order.getUser().getId(), paymentRequest.getAmount().longValueExact());

        return OrderPayment.builder()
                .amount(BigDecimal.valueOf(momoResponse.getAmount()))
                .status(Constants.PaymentStatusEnum.PENDING)
                .momoDeepLink(momoResponse.getDeeplink())
                .momoPayUrl(momoResponse.getPayUrl())
                .momoResultCode(momoResponse.getResultCode())
                .failureReason(momoResponse.getMessage())
                .build();
    }

    @Override
    public OrderPayment momoExecutePayment(MomoIPNRequest momoIPNRequest ) {
        OrderPayment orderPayment = orderPaymentRepository.findByOrder_Id(UUID.fromString(momoIPNRequest.getOrderId()));
        if (orderPayment == null) {
            throw new IllegalArgumentException("Order payment not found for order ID: " + momoIPNRequest.getOrderId());
        }
        if (momoIPNRequest.getResultCode().equals(0) || momoIPNRequest.getResultCode() == 9000) {
            orderPayment.setMomoResultCode(momoIPNRequest.getResultCode());
            orderPayment.setStatus(Constants.PaymentStatusEnum.COMPLETED);
        } else {
            orderPayment.setMomoResultCode(momoIPNRequest.getResultCode());
            orderPayment.setStatus(Constants.PaymentStatusEnum.FAILED);
            orderPayment.setFailureReason(momoIPNRequest.getMessage());
        }

        orderPayment.setTransactionId(momoIPNRequest.getTransId().toString());
        orderPaymentRepository.save(orderPayment);

        return orderPayment;
    }
}
