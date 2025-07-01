package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkins")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "checkin_id"))
})
public class Checkin extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "shift_id",
            foreignKey = @ForeignKey(
                    name = "fk_checkin_shift",
                    foreignKeyDefinition = "FOREIGN KEY (shift_id) REFERENCES shifts (shift_id) ON DELETE CASCADE"
            )
    )
    private Shift shift;

    @Column(name = "checkin_time", nullable = false)
    private LocalDateTime checkinTime;
}