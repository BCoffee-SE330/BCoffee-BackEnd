package com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.repository.OrderPaymentRepository;
import com.se330.coffee_shop_management_backend.repository.OrderRepository;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.PaymentStrategy;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.services.VNPayService;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class VNPayPaymentStrategy implements PaymentStrategy {

    private final VNPayService vnPayService;
    private final OrderRepository orderRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final INotificationService notificationService;

    @Override
    public OrderPayment pay(OrderPaymentCreateRequestDTO paymentRequest) throws UnsupportedEncodingException {
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + paymentRequest.getOrderId()));

        PaymentMethods paymentMethod = paymentMethodsRepository.findById(paymentRequest.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found with id: " + paymentRequest.getPaymentMethodId()));

        long amount = paymentRequest.getAmount().longValueExact() * 100;

        try {
            String vnpayUrl = vnPayService.createPaymentUrl(order.getId().toString(), amount, "1.1.1.1");
            return orderPaymentRepository.save(
                    OrderPayment.builder()
                            .amount(paymentRequest.getAmount())
                            .paymentMethod(paymentMethod)
                            .order(order)
                            .vnpayPayUrl(vnpayUrl)
                            .status(Constants.PaymentStatusEnum.PENDING)
                            .build()
            );
        } catch (Exception e) {
            return orderPaymentRepository.save(
                    OrderPayment.builder()
                            .amount(paymentRequest.getAmount())
                            .paymentMethod(paymentMethod)
                            .order(order)
                            .status(Constants.PaymentStatusEnum.FAILED)
                            .failureReason(e.getMessage())
                            .build()
            );
        }
    }

    @Override
    public OrderPayment vnpayExecutePayment(String vnp_BankCode, String vnp_CardType , String vnp_TransactionNo, String vnp_ResponseCode, String vnp_TxnRef  ) {

        String orderId = vnp_TxnRef.split("_")[0];
        OrderPayment currentOrderPayment = orderPaymentRepository.findByOrder_Id(UUID.fromString(orderId));
        currentOrderPayment.setVnpBankCode(vnp_BankCode);
        currentOrderPayment.setVnpCardType(vnp_CardType);
        currentOrderPayment.setTransactionId(vnp_TransactionNo);

        if (vnp_ResponseCode.equals("00")) {
            currentOrderPayment.setStatus(Constants.PaymentStatusEnum.COMPLETED);
            if (currentOrderPayment.getOrder() != null && currentOrderPayment.getOrder().getUser() != null) {
                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.PAYMENT)
                                .notificationContent("Thanh toán thành công cho đơn hàng " + currentOrderPayment.getOrder().getId())
                                .senderId(null)
                                .receiverId(currentOrderPayment.getOrder().getUser().getId())
                                .isRead(false)
                                .build()
                );
            }
        } else {
            currentOrderPayment.setStatus(Constants.PaymentStatusEnum.FAILED);
            currentOrderPayment.setFailureReason("VNPay payment failed with response code: " + vnp_ResponseCode);
            if (currentOrderPayment.getOrder() != null && currentOrderPayment.getOrder().getUser() != null) {
                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.PAYMENT)
                                .notificationContent("Thanh toán thành công cho đơn hàng " + currentOrderPayment.getOrder().getId())
                                .senderId(null)
                                .receiverId(currentOrderPayment.getOrder().getUser().getId())
                                .isRead(false)
                                .build()
                );
            }
        }

        return orderPaymentRepository.save(currentOrderPayment);
    }
}
