package com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se330.coffee_shop_management_backend.dto.request.payment.CreateMomoRequest;
import com.se330.coffee_shop_management_backend.dto.response.payment.MomoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MomoService {

    @Value("${MOMO_PARTNER_CODE}")
    private String PARTNER_CODE;
    @Value(value = "${MOMO_ACCESS_KEY}")
    private String ACCESS_KEY;
    @Value(value = "${MOMO_SECRET_KEY}")
    private String SECRET_KEY;
    @Value(value = "${MOMO_REDIRECT_URL}")
    private String REDIRECT_URL;
    @Value(value = "${MOMO_IPN_URL}")
    private String IPM_URL;
    @Value(value = "${MOMO_REQUEST_TYPE}")
    private String REQUEST_TYPE;
    @Value(value = "${MOMO_PARTNER_NAME}")
    private String PARTNER_NAME;
    @Value(value = "${MOMO_STORE_ID}")
    private String STORE_ID;

    private final MomoApi momoApi;

    public MomoResponse createQR(UUID orderId, UUID userId, Long amount){  // no usages
        String orderInfo = "Thanh toán đơn hàng: " + orderId.toString();
        String requestId = createRequestId(orderId, userId);
        String extraData = "eyJ1c2VybmFtZSI6Im1vbW8iLCJza3VzIjoidmFsdWUxLHZhbHVlMiJ9";  // {"username":"momo","skus":"value1,value2"} but base64 encoded

        try {
//            CreateMomoRequest request = CreateMomoRequest.builder()
//                    .partnerCode(PARTNER_CODE)
//                    .partnerName(PARTNER_NAME)
//                    .storeId(STORE_ID)
//                    .requestId(requestId)
//                    .amount(amount)
//                    .orderId(orderId.toString())
//                    .orderInfo(orderInfo)
//                    .redirectUrl(REDIRECT_URL)
//                    .ipnUrl(IPM_URL)
//                    .requestType(REQUEST_TYPE)
//                    .extraData(extraData)
//                    .lang("vi")
//                    .build();
//
//            String signature = generateSignature(request);
//            request.setSignature(signature);

            CreateMomoRequest request = CreateMomoRequest.builder()
                    .partnerCode("MOMOIQA420180417")
//                    .partnerName("Test Merchant")
//                    .storeId("storeid")
                    .requestId("1686811111")
                    .amount(10000L)
                    .orderId("ORDER_123")
                    .orderInfo("MoMo Test")
                    .redirectUrl("https://google.com")
                    .ipnUrl("https://google.com")
                    .requestType("captureWallet")
                    .extraData("")
                    .lang("vi")
                    .build();

            // Manually generate signature
            String testRaw = "accessKey=Q8gdY3JtVrGK&partnerCode=MOMOIQA420180417&requestType=captureWallet&ipnUrl=https://google.com&redirectUrl=https://google.com&orderId=ORDER_123&amount=10000&lang=vi&orderInfo=MoMo Test&requestId=1686811111&extraData=&partnerName=Test Merchant&storeId=storeid";
            request.setSignature(signHmacSHA256(testRaw, "dbHX48xqVvO9w3Tl8f9kUl4e4eH0aB7D"));

            log.info("FINAL REQUEST: {}", new ObjectMapper().writeValueAsString(request));

            return momoApi.createMomoQR(request);
        } catch (Exception e) {
            log.error("Signature generation failed: ", e);
            return null;
        }

    }

    private String generateSignature(CreateMomoRequest request) throws Exception {
        Map<String, String> params = new TreeMap<>();
        params.put("accessKey", ACCESS_KEY);
        params.put("amount", String.valueOf(request.getAmount()));
        params.put("extraData", request.getExtraData());
        params.put("ipnUrl", request.getIpnUrl());
        params.put("orderId", request.getOrderId());
        params.put("orderInfo", request.getOrderInfo());
        params.put("partnerCode", request.getPartnerCode());
        params.put("redirectUrl", request.getRedirectUrl());
        params.put("requestId", request.getRequestId());
        params.put("requestType", request.getRequestType());

        // Add new fields if present
        if (request.getPartnerName() != null) {
            params.put("partnerName", request.getPartnerName());
        }
        if (request.getStoreId() != null) {
            params.put("storeId", request.getStoreId());
        }

        // Build raw data string
        StringBuilder rawData = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            rawData.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        rawData.deleteCharAt(rawData.length() - 1); // Remove last "&"

        return signHmacSHA256(rawData.toString(), SECRET_KEY);
    }


    private String signHmacSHA256(String data, String key) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKey);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String createRequestId(UUID orderId, UUID userId) {
        return String.format("%s_%s", orderId.toString(), userId.toString());
    }

}
