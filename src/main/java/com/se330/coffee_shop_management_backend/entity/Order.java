package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.util.Constants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "order_id"))
})
public class Order extends AbstractBaseEntity {

    @Column(name = "order_total_cost_after_discount", nullable = false)
    private BigDecimal orderTotalCostAfterDiscount;

    @Column(name = "order_discount_cost", nullable = false)
    private BigDecimal orderDiscountCost;

    @Column(name = "order_total_cost", nullable = false)
    private BigDecimal orderTotalCost;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private Constants.OrderStatusEnum orderStatus;

    @Column(name = "order_tracking_number", nullable = false)
    private String orderTrackingNumber;

    @Column(name = "o_created_at", nullable = true)
    private LocalDateTime oCreatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_employee",
                    foreignKeyDefinition = "FOREIGN KEY (employee_id) REFERENCES employees (employee_id) ON DELETE CASCADE"
            )
    )
    private Employee employee;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OrderPayment orderPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "shipping_address_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_shipping_address",
                    foreignKeyDefinition = "FOREIGN KEY (shipping_address_id) REFERENCES shipping_addresses (shipping_address_id)"
            )
    )
    private ShippingAddresses shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "branch_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_branch",
                    foreignKeyDefinition = "FOREIGN KEY (branch_id) REFERENCES branches (branch_id) ON DELETE CASCADE"
            )
    )
    private Branch branch;
}