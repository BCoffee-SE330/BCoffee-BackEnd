package com.se330.coffee_shop_management_backend.service.paymentservices;

import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.payment.MomoIPNRequest;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface IOrderPaymentService {
    OrderPayment createOrderPayment(OrderPaymentCreateRequestDTO orderPaymentCreateRequestDTO) throws UnsupportedEncodingException;
    OrderPayment executePaypalPayment(String paymentId, String payerId);
    OrderPayment executeMomoPayment(MomoIPNRequest momoIPNRequest);
    OrderPayment vnpayExecutePayment(String vnp_BankCode, String vnp_CardType , String vnp_TransactionNo, String vnp_ResponseCode, String vnp_TxnRef);
    OrderPayment updateOrderPaymentStatus(UUID orderPaymentId, Constants.PaymentStatusEnum newStatus);
    Page<OrderPayment> findAllOrderPayments(Pageable pageable);
    Page<OrderPayment> findAllOrderPaymentsByCustomerId(UUID customerId, Pageable pageable);
    OrderPayment findByIdOrder(UUID id);
}
