package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.repository.CommentRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class Comment {

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void create() {
        log.info("Creating dummy comments...");

        // Get all products
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            log.error("Cannot create comments: No products found");
            return;
        }

        List<com.se330.coffee_shop_management_backend.entity.Comment> comments = new ArrayList<>();
        Random random = new Random();

        // Comment content templates for different product categories
        Map<String, List<String>> categoryComments = new HashMap<>();

        // Coffee comments
        categoryComments.put("Cà phê", Arrays.asList(
                "Hương vị thơm ngon, đúng chuẩn cà phê Việt Nam.",
                "Rất đậm đà và hài hòa, tôi sẽ mua lại lần sau.",
                "Cà phê ngon, dịch vụ tốt. Highly recommend!",
                "Hương vị độc đáo, không thể tìm thấy ở đâu khác.",
                "Cà phê ngon nhưng hơi đắt so với mặt bằng chung.",
                "Đúng kiểu cà phê truyền thống mà tôi yêu thích.",
                "Vị đắng vừa phải, hậu vị ngọt thanh."
        ));

        // Tea comments
        categoryComments.put("Trà", Arrays.asList(
                "Trà thơm, vị thanh mát rất phù hợp cho mùa hè.",
                "Nguyên liệu tươi ngon, trà có vị rất tự nhiên.",
                "Trà thơm nhưng hơi ngọt, có thể giảm đường được không?",
                "Tuyệt vời, trà có nhiều topping rất ngon.",
                "Hương vị tinh tế, rất đáng để thử.",
                "Trà có hậu vị rất ngọt và thơm.",
                "Không gian quán đẹp, trà ngon, tôi rất hài lòng!"
        ));

        // Freeze comments
        categoryComments.put("Freeze", Arrays.asList(
                "Freeze rất mát lạnh, hoàn hảo cho những ngày nóng.",
                "Đá xay mịn, vị rất cân bằng và thơm ngon.",
                "Freeze hơi ngọt, nhưng vẫn rất ngon.",
                "Topping phong phú, tôi đặc biệt thích lớp kem phủ trên cùng.",
                "Lần sau tôi sẽ thử thêm nhiều vị khác nữa.",
                "Giá cả phải chăng so với chất lượng.",
                "Freeze đá xay mịn, vị ngọt vừa phải, rất thích!"
        ));

        // Food comments
        categoryComments.put("Đồ ăn", Arrays.asList(
                "Bánh rất ngon và tươi mới, đặc biệt là lớp vỏ giòn tan.",
                "Phần nhân đầy đặn và thơm ngon.",
                "Giá hơi cao nhưng xứng đáng với chất lượng.",
                "Bánh được làm rất kỹ, ngon hơn những nơi khác tôi từng thử.",
                "Tươi ngon, kết hợp hoàn hảo với đồ uống.",
                "Size bánh hơi nhỏ so với giá tiền.",
                "Đồ ăn vặt hoàn hảo khi nhâm nhi cùng cà phê."
        ));

        // Other comments
        categoryComments.put("Khác", Arrays.asList(
                "Sản phẩm chất lượng cao, đúng như mô tả.",
                "Đóng gói cẩn thận, giao hàng nhanh chóng.",
                "Hương vị giữ được như ở quán, rất tiện lợi.",
                "Sẽ mua lại trong tương lai, rất hài lòng.",
                "Giá cả hợp lý, chất lượng tốt.",
                "Hướng dẫn sử dụng dễ hiểu, sản phẩm tiện lợi.",
                "Sản phẩm đúng chuẩn, hương vị đậm đà."
        ));

        // Generate 3-5 comments for each product
        for (Product product : products) {
            int commentCount = random.nextInt(10) + 20; // Generate 10-30 comments

            // Get category name
            String categoryName = product.getProductCategory().getCategoryName();
            List<String> relevantComments = categoryComments.getOrDefault(categoryName, categoryComments.get("Khác"));

            BigDecimal totalRating = BigDecimal.ZERO;

            for (int i = 0; i < commentCount; i++) {
                // Select random comment from appropriate category
                String content = relevantComments.get(random.nextInt(relevantComments.size()));

                BigDecimal rating = BigDecimal.valueOf(random.nextInt(501)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                // Create comment
                com.se330.coffee_shop_management_backend.entity.Comment comment =
                        com.se330.coffee_shop_management_backend.entity.Comment.builder()
                                .commentIsDeleted(false)
                                .commentRight(random.nextInt(3) + 1) // Random right comment (1-3)
                                .commentLeft(random.nextInt(3) + 1) // Random left comment (1-3)
                                .commentRating(rating)
                                .commentContent(content)
                                .product(product)
                                .build();

                totalRating = totalRating.add(rating);

                comments.add(comment);
            }
            BigDecimal averageRating = totalRating.divide(BigDecimal.valueOf(commentCount), RoundingMode.HALF_UP);

            product.setProductRatingsAverage(averageRating.setScale(1, RoundingMode.HALF_UP));
            product.setProductCommentCount(product.getProductCommentCount() + commentCount);
        }

        productRepository.saveAll(products);
        commentRepository.saveAll(comments);
        log.info("Created {} comments for {} products", comments.size(), products.size());
    }
}
