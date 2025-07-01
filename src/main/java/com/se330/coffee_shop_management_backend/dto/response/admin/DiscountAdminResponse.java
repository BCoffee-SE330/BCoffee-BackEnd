package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Discount;
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
public class DiscountAdminResponse {
    // Mã của phiếu giảm giá
    private String discountId;
    // Thời gian tạo và phiếu giảm giá
    private LocalDateTime createdAt;
    // Thời gian cập nhật phiếu giảm giá
    private LocalDateTime updatedAt;
    // Tên cua phiếu giảm giá
    private String discountName;
    // Mô tả về phiếu giảm giá
    private String discountDescription;
    // Loại phiếu giảm giá (Chỉ có 2 loai: PERCENTAGE (phần trăm) và AMOUNT(tiền cố định))
    private Constants.DiscountTypeEnum discountType;
    // Giá trị của phiếu giảm giá (phần trăm hoặc tiền cố định, nếu là tiền cố định thì giá trị theo tiền VNĐ, ví dụ 20000)
    private BigDecimal discountValue;
    // Mã của phiếu giảm giá (để khách hàng nhập khi thanh toán)
    private String discountCode;
    // Ngày bắt đầu áp dụng phiếu giảm giá
    private LocalDateTime discountStartDate;
    // Ngày kết thúc áp dụng phiếu giảm giá
    private LocalDateTime discountEndDate;
    // Số lượng người dùng tối đa có thể sử dụng phiếu giảm giá này
    private int discountMaxUsers;
    // Số lượng người dùng đã sử dụng phiếu giảm giá này
    private int discountUserCount;
    // Số lần sử dụng tối đa của mỗi người dùng
    private int discountMaxPerUser;
    // Giá trị đơn hàng tối thiểu để áp dụng phiếu giảm giá
    private BigDecimal discountMinOrderValue;
    // Trạng thái của phiếu giảm giá (còn hiệu lực hay không)
    private boolean discountIsActive;
    // Mã của chi nhánh áp dụng phiếu giảm giá, // khóa ngoại tham chiếu đến bảng chi nhánh
    private String branchId;

    public static DiscountAdminResponse convert(Discount discount) {
        return DiscountAdminResponse.builder()
                .discountId(discount.getId().toString())
                .createdAt(discount.getCreatedAt())
                .updatedAt(discount.getUpdatedAt())
                .discountName(discount.getDiscountName())
                .discountDescription(discount.getDiscountDescription())
                .discountType(discount.getDiscountType())
                .discountValue(discount.getDiscountValue())
                .discountCode(discount.getDiscountCode())
                .discountStartDate(discount.getDiscountStartDate())
                .discountEndDate(discount.getDiscountEndDate())
                .discountMaxUsers(discount.getDiscountMaxUsers())
                .discountUserCount(discount.getDiscountUserCount())
                .discountMaxPerUser(discount.getDiscountMaxPerUser())
                .discountMinOrderValue(discount.getDiscountMinOrderValue())
                .discountIsActive(discount.isDiscountIsActive())
                .branchId(discount.getBranch() != null ? discount.getBranch().getId().toString() : null)
                .build();
    }

    public static List<DiscountAdminResponse> convert(List<Discount> discounts) {
        if (discounts == null || discounts.isEmpty()) {
            return List.of();
        }
        return discounts.stream()
                .map(DiscountAdminResponse::convert)
                .toList();
    }
}