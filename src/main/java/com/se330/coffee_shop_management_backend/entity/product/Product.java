package com.se330.coffee_shop_management_backend.entity.product;

import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import com.se330.coffee_shop_management_backend.entity.Comment;
import com.se330.coffee_shop_management_backend.entity.FavoriteDrink;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Indexed
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "product_id"))
})
public class Product extends AbstractBaseEntity {
    @Column(name = "product_name", nullable = false)
    @FullTextField(analyzer = "vietnamese", searchAnalyzer = "vietnamese_search")
    private String productName = "";

    @Column(name = "product_thumb", nullable = false)
    private String productThumb = "";

    @Column(name = "product_description", nullable = false, length = 1000)
    private String productDescription = "";

    @Column(name = "product_price", nullable = false)
    private BigDecimal productPrice = BigDecimal.ZERO;

    @Column(name = "product_slug", nullable = false)
    private String productSlug = "";

    @Column(name = "product_comment_count", nullable = false)
    private int productCommentCount = 0;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "5.0", inclusive = true)
    @Digits(integer = 1, fraction = 2)
    @Column(name = "product_ratings_average", nullable = false, precision = 3, scale = 2)
    private BigDecimal productRatingsAverage;

    @Column(name = "product_is_published", nullable = false)
    private Boolean productIsPublished;

    @Column(name = "product_is_deleted", nullable = false)
    private Boolean productIsDeleted = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductVariant> productVariants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_product_product-category")
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<FavoriteDrink> favoritedBy = new ArrayList<>();
}
