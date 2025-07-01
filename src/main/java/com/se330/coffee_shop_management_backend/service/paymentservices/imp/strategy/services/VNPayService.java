package com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.services;

import com.se330.coffee_shop_management_backend.config.payment.VNPayConfig;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.util.HashSecretKeyVNPay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VNPayService {

    @Value("${VNP_URL}")
    public String vnp_PayUrl;

    @Value("${VNP_RETURN_URL}")
    public String vnp_ReturnUrl;

    @Value("${VNP_TMN_CODE}")
    public String vnp_TmnCode;

    @Value("${VNP_HASH_SECRET}")
    public String secretKey;

    @Value("${VNP_VERSION}")
    public String vnp_Version;

    @Value("${VNP_COMMAND}")
    public String vnp_Command;

    @Value("${VNP_CURRENCY}")
    public String vnp_Currency;

    @Value("${VNP_LOCALE}")
    public String vnp_Locale;

    @Value("${VNP_ORDER_TYPE}")
    public String vnp_OrderType;

    public String createPaymentUrl(String orderId, long amount, String ipAddress) throws UnsupportedEncodingException {

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", vnp_Currency);
        vnp_Params.put("vnp_TxnRef", orderId + "_" + UUID.randomUUID());
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + orderId);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", ipAddress);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.DATE, 15); // Set expiration time to 15 minutes from now
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = HashSecretKeyVNPay.hmacSHA512(secretKey, hashData.toString());

        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return vnp_PayUrl + "?" + queryUrl;
    }
}
