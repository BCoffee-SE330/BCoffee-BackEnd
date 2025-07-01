package com.se330.coffee_shop_management_backend.dto.request.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentUpdateDTO {
    private int commentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String commentContent;
    private BigDecimal commentRating;
    private boolean commentIsDeleted;
}