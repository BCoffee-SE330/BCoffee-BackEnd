package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sub_checkins")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "sub_checkin_id"))
})
public class SubCheckin extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "shift_id",
            foreignKey = @ForeignKey(
                    name = "fk_checkin_shift",
                    foreignKeyDefinition = "FOREIGN KEY (shift_id) REFERENCES shifts (shift_id) ON DELETE CASCADE"
            )
    )
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            foreignKey = @ForeignKey(
                    name = "fk_subcheckin_employee",
                    foreignKeyDefinition = "FOREIGN KEY (employee_id) REFERENCES employees (employee_id) ON DELETE CASCADE"
            )
    )
    private Employee employee;

    @Column(name = "checkin_time", nullable = false)
    private LocalDateTime checkinTime;
}