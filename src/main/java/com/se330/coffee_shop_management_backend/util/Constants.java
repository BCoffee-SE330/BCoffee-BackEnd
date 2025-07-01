package com.se330.coffee_shop_management_backend.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

public final class Constants {
    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_TYPE = "Bearer";

    public static final int EMAIL_VERIFICATION_TOKEN_LENGTH = 64;

    public static final int PASSWORD_RESET_TOKEN_LENGTH = 32;

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private Constants() {
    }

    @Getter
    @AllArgsConstructor
    public enum RoleEnum {
        ADMIN("QUẢN TRỊ VIÊN"),
        CUSTOMER("KHÁCH HÀNG"),
        EMPLOYEE("NHÂN VIÊN"),
        MANAGER("QUẢN LÝ"),
        USER("NGƯỜI DÙNG"),;

        private final String value;

        public static RoleEnum get(final String name) {
            return Stream.of(RoleEnum.values())
                .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid role name: %s", name)));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum OrderStatusEnum {
        PENDING("ĐANG CHỜ"),
        PROCESSING("ĐANG XỬ LÝ"),
        COMPLETED("HOÀN TẤT"),
        DELIVERING("ĐANG GIAO HÀNG"),
        DELIVERED("ĐÃ GIAO HÀNG"),
        CANCELLED("ĐÃ HỦY"),;

        private final String value;

        public static OrderStatusEnum get(final String name) {
            return Stream.of(OrderStatusEnum.values())
                .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid order status name: %s", name)));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum NotificationTypeEnum {
        /**
         * Thông báo liên quan đến quản lý đơn hàng.
         * Ví dụ:
         * - Tạo đơn hàng thành công (online), đã thanh toán thành công
         * - Tạo đơn hàng không thành công:
         *     - Thanh toán thất bại (không đủ tiền)
         *     - Không đủ nguyên liệu để làm đơn hàng
         *     - Đơn hàng không hợp lệ
         * - Đơn hàng được nhận (online)
         * - Đơn hàng bị hủy
         * - Đơn hàng giao thành công
         * - Mua hàng tại quầy thành công
         */
        ORDER("ĐƠN HÀNG"),

        PAYMENT("THANH TOÁN"),

        /**
         * Thông báo về các chương trình khuyến mãi và giảm giá.
         * Ví dụ:
         * - Thêm khuyến mãi mới
         * - Xóa khuyến mãi
         * - Cập nhật khuyến mãi
         * - Khuyến mãi sắp hết hạn
         */
        DISCOUNT("KHUYẾN MÃI"),

        /**
         * Thông báo hệ thống ảnh hưởng đến tất cả người dùng.
         * Ví dụ:
         * - Thông báo bảo trì
         * - Cập nhật phiên bản
         * - Thay đổi chính sách
         */
        SYSTEM("HỆ THỐNG"),

        /**
         * Thông báo liên quan đến quản lý kho.
         * Ví dụ:
         * - Cảnh báo hàng sắp hết
         * - Cảnh báo hàng sắp hết hạn
         */
        INVENTORY("KHO HÀNG"),

        /**
         * Thông báo liên quan đến quản lý nhân sự.
         * Ví dụ:
         * - Nhận thông báo chào mừng đến chi nhánh
         * - Phân công ca làm mới
         * - Checkin thành công
         * - Thông báo lương về
         */
        EMPLOYEE("NHÂN SỰ"),

        /**
         * Thông báo trực tiếp từ quản lý đến nhân viên.
         * Ví dụ:
         * - Gửi thông báo cho nhân viên thành công
         * - Nhân viên nhận thông báo từ manager
         */
        MANAGER("QUẢN LÝ"),

        /**
         * Thông báo liên quan đến hóa đơn nhập kho.
         * Ví dụ:
         * - Nhập nguyên liệu vào nhà kho thành công
         * - Nhập nguyên liệu vào nhà kho thất bại
         * - Cập nhật phiếu nhập kho
         * - Hủy phiếu nhập kho
         */
        INVOICE("KHO HÀNG"),

        /**
         * Thông báo liên quan đến chuyển kho.
         * Ví dụ:
         * - Xuất nguyên liệu cho chi nhánh thành công
         * - Xuất nguyên liệu cho chi nhánh thất bại:
         *     - Nhà kho không đủ nguyên liệu
         * - Cập nhật phiếu nhập xuất
         * - Hủy phiếu nhập xuất
         */
        TRANSFER("CHUYỂN KHO"),

        /**
         * Thông báo liên quan đến chi nhánh.
         */
        BRANCH("CHI NHÁNH"),

        /**
         * Thông báo liên quan đến nhà cung cấp.
         * Ví dụ:
         * - Thêm thông tin nhà cung cấp
         * - Cập nhật thông tin nhà cung cấp
         * - Xóa thông tin nhà cung cấp
         */
        SUPPLIER("NHÀ CUNG CẤP"),

        /**
         * Thông báo liên quan đến các loại sản phẩm.
         * Ví dụ:
         * - Thêm product mới
         * - Cập nhật thông tin product
         * - Xóa thông tin product
         */
        PRODUCT("SẢN PHẨM"),

        /**
         * Thông báo liên quan đến nhà kho.
         * Ví dụ:
         * - Thêm nhà kho mới
         * - Cập nhật thông tin nhà kho
         * - Xóa thông tin nhà kho
         */
        WAREHOUSE("NHÀ KHO"),;

        private final String value;

        public static NotificationTypeEnum get(final String name) {
            return Stream.of(NotificationTypeEnum.values())
                    .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid notification type name: %s", name)));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum DiscountTypeEnum {
        PERCENTAGE("PHẦN TRĂM"),
        AMOUNT("SỐ TIỀN"),;

        private final String value;

        public static DiscountTypeEnum get(final String name) {
            return Stream.of(DiscountTypeEnum.values())
                .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid discount type name: %s", name)));
        }
    }

    public static String getTokenFromPath(final String path) {
        if (path == null || path.isEmpty())
            return null;

        final String[] fields = path.split("/");

        if (fields.length == 0)
            return null;

        try {
            return fields[2];
        } catch (final IndexOutOfBoundsException e) {
            System.out.println("Cannot find user or channel id from the path!. Ex:"+ e.getMessage());
        }
        return null;
    }

    @Getter
    @AllArgsConstructor
    public enum DayOfWeekEnum {
        MONDAY("THỨ HAI"),
        TUESDAY("THỨ BA"),
        WEDNESDAY("THỨ TƯ"),
        THURSDAY("THỨ NĂM"),
        FRIDAY("THỨ SÁU"),
        SATURDAY("THỨ BẢY"),
        SUNDAY("CHỦ NHẬT"),;

        private final String value;

        public static DayOfWeekEnum get(final String name) {
            return Stream.of(DayOfWeekEnum.values())
                .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid day of week name: %s", name)));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum PaymentMethodEnum {
        CASH("TIỀN MẶT"),
        PAYPAL("PAYPAL"),
        VNPAY("VN PAY"),
        MOMO("MOMO"),
        ZALOPAY("ZALO PAY"),;

        private final String value;

        public static PaymentMethodEnum get(final String name) {
            return Stream.of(PaymentMethodEnum.values())
                .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid payment method name: %s", name)));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum PaymentStatusEnum {
        PENDING("ĐANG CHỜ"),
        COMPLETED("HOÀN TẤT"),
        FAILED("THẤT BẠI"),
        REFUNDED("ĐÃ HOÀN TIỀN"),;

        private final String value;

        public static PaymentStatusEnum get(final String name) {
            return Stream.of(PaymentStatusEnum.values())
                .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid payment status name: %s", name)));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum ProductVariantTierIdx {
        SMALL("small"),
        MEDIUM("medium"),
        LARGE("large"),
        DEFAULT("default"),;

        private final String value;
        public static ProductVariantTierIdx get(final String name) {
            return Stream.of(ProductVariantTierIdx.values())
                .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid product variant tier index name: %s", name)));
        }
    }
}
