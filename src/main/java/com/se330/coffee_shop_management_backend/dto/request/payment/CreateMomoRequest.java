package com.se330.coffee_shop_management_backend.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMomoRequest {
    // Định danh duy nhất của tài khoản M4B của bạn
    private String partnerCode;

    // Định danh duy nhất cho mỗi yêu cầu
    private String requestId;

    // Mã định danh của ứng dụng
    private String partnerName;

    // ID của cửa hàng
    private String storeId;

    // Số tiền cần thanh toán (VND)
    private Long amount;

    // Mã đơn hàng của đối tác
    private String orderId;

    // Thông tin đơn hàng
    private String orderInfo;

    // URL redirect sau khi thanh toán
    private String redirectUrl;

    // API của đối tác nhận kết quả thanh toán
    private String ipnUrl;

    // Loại yêu cầu
    private String requestType;

    // Dữ liệu bổ sung
    private String extraData;

    // Ngôn ngữ (vi hoặc en)
    private String lang;

    // Chữ ký xác nhận giao dịch
    private String signature;
}