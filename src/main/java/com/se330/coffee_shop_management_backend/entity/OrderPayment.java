package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.util.Constants.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "order_payment_id"))
})
public class OrderPayment extends AbstractBaseEntity {

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum status;

    @Column(name = "transaction_id")
    private String transactionId;

    // Paypal
    @Column(name = "paypal_approval_url")
    private String paypalApprovalUrl;
    @Column(name = "paypal_payment_id")
    private String paypalPaymentId;

    // MoMo
    @Column(name = "momo_result_code")
    private int momoResultCode;
    @Column(name = "momo_pay_url")
    private String momoPayUrl;
    @Column(name = "momo_deep_link")
    private String momoDeepLink;

    // VNPay
    @Column(name = "vnpay_pay_url", length = 1050)
    private String vnpayPayUrl;
    @Column(name = "vnp_bank_code")
    private String vnpBankCode;
    @Column(name = "vnp_card_type")
    private String vnpCardType;

    @Column(name = "failure_reason", columnDefinition = "text")
    private String failureReason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_payment_order",
                    foreignKeyDefinition = "FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE"
            )
    )
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "payment_method_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_payment_payment_method",
                    foreignKeyDefinition = "FOREIGN KEY (payment_method_id) REFERENCES payment_methods (payment_method_id)"
            )
    )
    private PaymentMethods paymentMethod;
}