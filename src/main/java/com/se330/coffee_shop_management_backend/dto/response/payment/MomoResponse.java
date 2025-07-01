package com.se330.coffee_shop_management_backend.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MomoResponse {
    // Định danh duy nhất của tài khoản M4B của bạn
    private String partnerCode;

    // Định danh duy nhất cho mỗi yêu cầu (giống với yêu cầu ban đầu)
    private String requestId;

    // Mã đơn hàng của đối tác
    private String orderId;

    // Số tiền thanh toán (giống với số tiền yêu cầu ban đầu)
    private Long amount;

    // Thời gian trả kết quả thanh toán (timestamp)
    private Long responseTime;

    // Mô tả lỗi, ngôn ngữ dựa trên lang
    private String message;

    // Mã kết quả
    /*
    * resultCode = 0: giao dịch thành công.
    * resultCode = 9000: giao dịch được cấp quyền (authorization) thành công .
    * resultCode <> 0: giao dịch thất bại.
    * */
    private int resultCode;

    // URL để chuyển từ trang mua hàng sang trang thanh toán MoMo
    private String payUrl;

    // URL để mở ứng dụng MoMo trực tiếp
    private String deeplink;
}