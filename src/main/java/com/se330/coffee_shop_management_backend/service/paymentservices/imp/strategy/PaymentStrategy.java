package com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy;

import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.payment.MomoIPNRequest;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;

import java.io.UnsupportedEncodingException;

public interface PaymentStrategy {
    OrderPayment pay(OrderPaymentCreateRequestDTO paymentRequest) throws UnsupportedEncodingException;
    default OrderPayment executePayment(String paymentId, String payerId) {
        throw new UnsupportedOperationException("This payment method doesn't support payment execution");
    }
    default OrderPayment momoExecutePayment(MomoIPNRequest momoIPNRequest) {
        throw new UnsupportedOperationException("This payment method only supports momo payment execution");
    }
    default OrderPayment vnpayExecutePayment(String vnp_BankCode, String vnp_CardType , String vnp_TransactionNo, String vnp_ResponseCode, String vnp_TxnRef  ) {
        throw new UnsupportedOperationException("This payment method only supports vnpay payment execution");
    }
}