package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderPaymentAdminResponse {
    // Khóa ngoại tham chiếu đến bảng Order
    private String orderId;
// Khóa ngoại tham chiếu đến bảng PaymentMethod, hiện chỉ hỗ trợ
    // PENDING("ĐANG CHỜ"),
//        COMPLETED("HOÀN TẤT"),
//        FAILED("THẤT BẠI"),
//        REFUNDED("ĐÃ HOÀN TIỀN"),;
    private String paymentMethodId;
    // Tổng tiền thanh toán (đã áp dụng thuế và giảm giá)
    private BigDecimal amount;
    // Trạng thái thanh toán
    private Constants.PaymentStatusEnum status;
    // Mã giao dịch thanh toán (có thể là mã giao dịch của PayPal, MoMo, VNPay, v.v.)
    // Chỉ có mã giao dịch nếu thanh toán thành công
    private String transactionId;
    // Link thanh toán (Nếu chọn hình thức thanh toán là Paypal)
    private String paypalApprovalUrl;
    // Mã Payment PayPal (Nếu chọn hình thức thanh toán là Paypal, khác với transactionId)
    private String paypalPaymentId;

    // Mã kết quả của MoMo (Nếu chọn hình thức thanh toán là MoMo)
    private int momoResultCode;
    // Link thanh toán MoMo (Nếu chọn hình thức thanh toán là MoMo)
    private String momoPayUrl;
    // Deep link MoMo (Nếu chọn hình thức thanh toán là MoMo)
    private String momoDeepLink;
    // Link thanh toán VNPay (Nếu chọn hình thức thanh toán là VNPay)
    private String vnpayPayUrl;
    // Mã ngân hàng VNPay (Nếu chọn hình thức thanh toán là VNPay)
    private String vnpBankCode;
    // Loại thẻ VNPay (Nếu chọn hình thức thanh toán là VNPay)
    private String vnpCardType;
    // Lý do thất bại (Nếu thanh toán thất bại, lưu trữ lý do để người dùng biết)
    private String failureReason;

    // Thời gian tạo thanh toán
    private LocalDateTime createdAt;
    // Thời gian cập nhật thanh toán
    private LocalDateTime updatedAt;

    public static OrderPaymentAdminResponse convert(OrderPayment orderPayment) {
        return OrderPaymentAdminResponse.builder()
                .orderId(orderPayment.getOrder().getId().toString())
                .paymentMethodId(orderPayment.getPaymentMethod().getId().toString())
                .amount(orderPayment.getAmount())
                .status(orderPayment.getStatus())
                .transactionId(orderPayment.getTransactionId())
                .paypalApprovalUrl(orderPayment.getPaypalApprovalUrl())
                .paypalPaymentId(orderPayment.getPaypalPaymentId())
                .momoResultCode(orderPayment.getMomoResultCode())
                .momoPayUrl(orderPayment.getMomoPayUrl())
                .momoDeepLink(orderPayment.getMomoDeepLink())
                .vnpayPayUrl(orderPayment.getVnpayPayUrl())
                .vnpBankCode(orderPayment.getVnpBankCode())
                .vnpCardType(orderPayment.getVnpCardType())
                .failureReason(orderPayment.getFailureReason())
                .createdAt(orderPayment.getCreatedAt())
                .updatedAt(orderPayment.getUpdatedAt())
                .build();
    }

    public static List<OrderPaymentAdminResponse> convert(List<OrderPayment> orderPayments) {
        if (orderPayments == null || orderPayments.isEmpty()) {
            return List.of();
        }

        return orderPayments.stream()
                .map(OrderPaymentAdminResponse::convert)
                .toList();
    }
}
