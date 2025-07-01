package com.se330.coffee_shop_management_backend.service.paymentservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.payment.MomoIPNRequest;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.repository.OrderPaymentRepository;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.service.paymentservices.IOrderPaymentService;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.PaymentStrategy;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImpOrderPaymentService implements IOrderPaymentService {

    private final PaymentMethodsRepository paymentMethodsRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final Map<Constants.PaymentMethodEnum, PaymentStrategy> paymentStrategies;

    @Override
    @Transactional
    public OrderPayment createOrderPayment(OrderPaymentCreateRequestDTO orderPaymentCreateRequestDTO) throws UnsupportedEncodingException {
        PaymentMethods paymentMethod = paymentMethodsRepository.findById(orderPaymentCreateRequestDTO.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found with id: " + orderPaymentCreateRequestDTO.getPaymentMethodId()));

        PaymentStrategy strategy = paymentStrategies.get(paymentMethod.getPaymentMethodName());

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod.getPaymentMethodName());
        }

        return strategy.pay(orderPaymentCreateRequestDTO);
    }

    @Override
    @Transactional
    public OrderPayment updateOrderPaymentStatus(UUID orderPaymentId, Constants.PaymentStatusEnum newStatus) {
        OrderPayment orderPayment = orderPaymentRepository.findById(orderPaymentId)
                .orElseThrow(() -> new IllegalArgumentException("Order payment not found with id: " + orderPaymentId));

        // Update the status of the order payment
        orderPayment.setStatus(newStatus);

        if (newStatus == Constants.PaymentStatusEnum.COMPLETED) {
            // TODO: Send completed noti
        }
        else if (newStatus == Constants.PaymentStatusEnum.FAILED) {
            // TODO: Send failed noti
        }
        else if (newStatus == Constants.PaymentStatusEnum.REFUNDED) {
            // TODO: Send refunded noti
        }

        return orderPaymentRepository.save(orderPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderPayment> findAllOrderPayments(Pageable pageable) {
        return orderPaymentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderPayment> findAllOrderPaymentsByCustomerId(UUID customerId, Pageable pageable) {
        return orderPaymentRepository.findAllByOrder_User_Id(customerId, pageable);
    }

    @Override
    public OrderPayment findByIdOrder(UUID id) {
        return orderPaymentRepository.findByOrder_Id(id);
    }

    @Override
    @Transactional
    public OrderPayment executePaypalPayment(String paymentId, String payerId) {
        OrderPayment orderPayment = orderPaymentRepository.findByPaypalPaymentId(paymentId);
        if (orderPayment == null) {
            throw new IllegalArgumentException("Payment not found with transaction ID: " + paymentId);
        }

        PaymentMethods paymentMethod = orderPayment.getPaymentMethod();
        PaymentStrategy strategy = paymentStrategies.get(paymentMethod.getPaymentMethodName());

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod.getPaymentMethodName());
        }

        return strategy.executePayment(paymentId, payerId);
    }

    @Override
    public OrderPayment executeMomoPayment(MomoIPNRequest momoIPNRequest) {

        PaymentStrategy strategy = paymentStrategies.get(Constants.PaymentMethodEnum.MOMO);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment method: MOMO");
        }

        return strategy.momoExecutePayment(momoIPNRequest);
    }

    @Override
    public OrderPayment vnpayExecutePayment(String vnp_BankCode, String vnp_CardType, String vnp_TransactionNo, String vnp_ResponseCode, String vnp_TxnRef) {
        PaymentStrategy strategy = paymentStrategies.get(Constants.PaymentMethodEnum.VNPAY);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment method: VNPAY");
        }

        return strategy.vnpayExecutePayment(vnp_BankCode, vnp_CardType, vnp_TransactionNo, vnp_ResponseCode, vnp_TxnRef);
    }
}
