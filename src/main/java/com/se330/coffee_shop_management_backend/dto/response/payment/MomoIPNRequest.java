package com.se330.coffee_shop_management_backend.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MomoIPNRequest {
    private String orderType;
    private Long amount;
    private String partnerCode;
    private String orderId;
    private String extraData;
    private String signature;
    private Long transId;
    private Long responseTime;
    private Integer resultCode;
    private String message;
    private String payType;
    private String requestId;
    private String orderInfo;
}