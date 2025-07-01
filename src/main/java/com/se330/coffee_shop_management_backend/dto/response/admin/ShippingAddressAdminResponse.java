package com.se330.coffee_shop_management_backend.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ShippingAddressAdminResponse {
    // Mã định danh của địa chỉ giao hàng
    private String addressId;
    // Địa chỉ giao hàng
    private String addressLine;
    private String addressCity;
    private String addressDistrict;
    // Trạng thái địa chỉ giao hàng có phải là địa chỉ mặc định hay không
    private boolean addressIsDefault;
    // Mã định danh của người dùng, khóa ngoại tham chiếu đến bảng người dùng
    private String userId;

    public static ShippingAddressAdminResponse convert(com.se330.coffee_shop_management_backend.entity.ShippingAddresses shippingAddress) {
        return ShippingAddressAdminResponse.builder()
                .addressId(shippingAddress.getId().toString())
                .addressLine(shippingAddress.getAddressLine())
                .addressCity(shippingAddress.getAddressCity())
                .addressDistrict(shippingAddress.getAddressDistrict())
                .addressIsDefault(shippingAddress.isAddressIsDefault())
                .userId(shippingAddress.getUser() != null ? shippingAddress.getUser().getId().toString() : null)
                .build();
    }

    public static java.util.List<ShippingAddressAdminResponse> convert(java.util.List<com.se330.coffee_shop_management_backend.entity.ShippingAddresses> shippingAddresses) {
        if (shippingAddresses == null || shippingAddresses.isEmpty()) {
            return java.util.List.of();
        }
        return shippingAddresses.stream()
                .map(ShippingAddressAdminResponse::convert)
                .toList();
    }
}
