package com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.services;

import com.se330.coffee_shop_management_backend.dto.request.payment.CreateMomoRequest;
import com.se330.coffee_shop_management_backend.dto.response.payment.MomoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "momo-api", url = "${MOMO_END_POINT}")
public interface MomoApi {

    @PostMapping(value = "/create", consumes = "application/json; charset=UTF-8")
    MomoResponse createMomoQR(@RequestBody CreateMomoRequest createMomoRequest);
}
