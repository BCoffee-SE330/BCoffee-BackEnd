package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class CommentAdminResponse {
    // Mã của bình luận
    private int commentId;
    // Thời gian tạo bình luận
    private LocalDateTime createdAt;
    // Thời gian cập nhật bình luận gần nhất
    private LocalDateTime updatedAt;
    // Nội dung bình luận
    private String commentContent;
    // Vị trí bên trái trong cây bình luận
    private int commentLeft;
    // Vị trí bên phải trong cây bình luận
    private int commentRight;
    // Trạng thái xóa của bình luận
    private boolean commentIsDeleted;
    // Đánh giá sao của bình luận, giá trị từ 0.00 đến 5.00
    private BigDecimal commentRating;
    // Mã của sản phẩm được bình luận, tham chiếu đến bảng sản phẩm
    private String productId;

    public static CommentAdminResponse convert(Comment comment) {
        return CommentAdminResponse.builder()
                .commentId(comment.getCommentId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .commentContent(comment.getCommentContent())
                .commentLeft(comment.getCommentLeft())
                .commentRight(comment.getCommentRight())
                .commentIsDeleted(comment.isCommentIsDeleted())
                .commentRating(comment.getCommentRating())
                .productId(comment.getProduct() != null ? comment.getProduct().getId().toString() : null)
                .build();
    }

    public static List<CommentAdminResponse> convert(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return List.of();
        }
        return comments.stream()
                .map(CommentAdminResponse::convert)
                .toList();
    }
}