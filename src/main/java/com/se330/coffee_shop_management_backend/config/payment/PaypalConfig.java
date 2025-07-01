package com.se330.coffee_shop_management_backend.config.payment;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfig {
    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${SECRET_KEY}")
    private String clientSecret;

    @Value("${PAYPAL_MODE}")
    private String mode;

    @Bean
    public APIContext apiContext() {
        return new APIContext(clientId, clientSecret, mode);
    }
}