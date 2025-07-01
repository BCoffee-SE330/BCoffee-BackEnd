package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.util.Constants;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "payment_method_id"))
})
public class PaymentMethods extends AbstractBaseEntity {
    @Column(name = "payment_method_name", nullable = false, unique = true)
    private Constants.PaymentMethodEnum paymentMethodName;

    @Column(name = "payment_method_description")
    private String paymentMethodDescription;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderPayment> orderPayments = new ArrayList<>();
}