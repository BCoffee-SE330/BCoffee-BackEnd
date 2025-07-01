package com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.imp;

import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.repository.OrderPaymentRepository;
import com.se330.coffee_shop_management_backend.repository.OrderRepository;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.PaymentStrategy;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final OrderRepository orderRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    @Override
    public OrderPayment pay(OrderPaymentCreateRequestDTO paymentRequest) {
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + paymentRequest.getOrderId()));

        PaymentMethods paymentMethod = paymentMethodsRepository.findById(paymentRequest.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found with id: " + paymentRequest.getPaymentMethodId()));

        return orderPaymentRepository.save(
                OrderPayment.builder()
                        .amount(paymentRequest.getAmount())
                        .paymentMethod(paymentMethod)
                        .order(order)
                        .status(Constants.PaymentStatusEnum.COMPLETED)
                        .transactionId(paymentRequest.getTransactionId())
                        .build()
        );
    }
}