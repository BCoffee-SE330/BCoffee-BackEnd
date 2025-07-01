package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class PaymentMethodAdminResponse {
    // Mã phương thức thanh toán
    private String paymentMethodId;
    // Tên phương thức thanh toán
    // Hiện tại chỉ hỗ trợ
    //CASH("TIỀN MẶT"),
    //        PAYPAL("PAYPAL"),
    //        VNPAY("VN PAY"),
    //        MOMO("MOMO"), Đang phát triển
    //        ZALOPAY("ZALO PAY"),; Đang phát triển
    private Constants.PaymentMethodEnum paymentMethodName;
    // Mô tả phương thức thanh toán
    private String paymentMethodDescription;
    // Trạng thái hoạt động của phương thức thanh toán
    private boolean isActive;

    public static PaymentMethodAdminResponse convert(PaymentMethods paymentMethod) {
        return PaymentMethodAdminResponse.builder()
                .paymentMethodId(paymentMethod.getId().toString())
                .paymentMethodName(paymentMethod.getPaymentMethodName())
                .paymentMethodDescription(paymentMethod.getPaymentMethodDescription())
                .isActive(paymentMethod.isActive())
                .build();
    }

    public static List<PaymentMethodAdminResponse> convert(List<PaymentMethods> paymentMethods) {
        if (paymentMethods == null || paymentMethods.isEmpty()) {
            return List.of();
        }
        return paymentMethods.stream().map(PaymentMethodAdminResponse::convert).toList();
    }

}
