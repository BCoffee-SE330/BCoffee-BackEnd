package com.se330.coffee_shop_management_backend.dto.response.comment;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class CommentResponseDTO {

    @Schema(
            name = "id",
            description = "Comment ID",
            type = "Integer",
            example = "1"
    )
    private int commentId;

    @Schema(
            name = "createdAt",
            description = "Date time field of comment creation",
            type = "LocalDateTime",
            example = "2023-10-15T14:30:22"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of comment update",
            example = "2023-10-15T15:45:10"
    )
    private LocalDateTime updatedAt;

    private String commentContent;
    private int commentLeft;
    private int commentRight;
    private boolean commentIsDeleted;
    private BigDecimal commentRating;
    private String productId;

    public static CommentResponseDTO convert(Comment comment) {
        if (comment == null) {
            return null;
        }

        return CommentResponseDTO.builder()
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

    public static List<CommentResponseDTO> convert(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return Collections.emptyList();
        }

        return comments.stream()
                .map(CommentResponseDTO::convert)
                .collect(Collectors.toList());
    }
}