package com.se330.coffee_shop_management_backend.service.paymentservices.imp;

import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.service.paymentservices.IPaymentMethodService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ImpPaymentMethodService implements IPaymentMethodService {

    private final PaymentMethodsRepository paymentMethodsRepository;

    public ImpPaymentMethodService(PaymentMethodsRepository paymentMethodsRepository) {
        this.paymentMethodsRepository = paymentMethodsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMethods findByIdPaymentMethod(UUID id) {
        return paymentMethodsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethods> findAllPaymentMethods(Pageable pageable) {
        return paymentMethodsRepository.findAll(pageable);
    }
}
