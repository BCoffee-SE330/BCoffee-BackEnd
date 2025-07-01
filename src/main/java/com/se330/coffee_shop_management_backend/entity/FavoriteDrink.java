package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_drinks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "favorite_drink_id"))
})
public class FavoriteDrink extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_favorite_drink_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(
                    name = "fk_favorite_drink_product",
                    foreignKeyDefinition = "FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE"
            )
    )
    private Product product;
}