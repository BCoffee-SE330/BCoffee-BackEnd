package com.se330.coffee_shop_management_backend.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import com.se330.coffee_shop_management_backend.entity.Catalog;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "category_id"))
})
public class ProductCategory extends AbstractBaseEntity {
    @Column(name = "category_name", nullable = false)
    private String categoryName = "";

    @Column(name = "category_description", nullable = false, length = 1000)
    private String categoryDescription = "";

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;
}
