package com.se330.coffee_shop_management_backend.config.payment;

import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.PaymentStrategy;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.imp.CashPaymentStrategy;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.imp.MomoPaymentStrategy;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.imp.PaypalPaymentStrategy;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.imp.VNPayPaymentStrategy;
import com.se330.coffee_shop_management_backend.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaymentStrategyConfig {

    @Bean
    public Map<Constants.PaymentMethodEnum, PaymentStrategy> paymentStrategies(
            CashPaymentStrategy cashPaymentStrategy,
            PaypalPaymentStrategy paypalPaymentStrategy,
            MomoPaymentStrategy momoPaymentStrategy,
            VNPayPaymentStrategy vnpayPaymentStrategy
            // ZaloPayPaymentStrategy zaloPayPaymentStrategy,
    ) {

        Map<Constants.PaymentMethodEnum, PaymentStrategy> strategies = new HashMap<>();
        strategies.put(Constants.PaymentMethodEnum.CASH, cashPaymentStrategy);
        strategies.put(Constants.PaymentMethodEnum.PAYPAL, paypalPaymentStrategy);
        strategies.put(Constants.PaymentMethodEnum.MOMO, momoPaymentStrategy);
        strategies.put(Constants.PaymentMethodEnum.VNPAY, vnpayPaymentStrategy);
        // strategies.put(Constants.PaymentMethodEnum.ZALOPAY, zaloPayPaymentStrategy);

        return strategies;
    }
}