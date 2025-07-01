package com.se330.coffee_shop_management_backend.service.paymentservices;

import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IPaymentMethodService {
    PaymentMethods findByIdPaymentMethod(UUID id);
    Page<PaymentMethods> findAllPaymentMethods(Pageable pageable);
}
