package com.se330.coffee_shop_management_backend.dto.request.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentCreateRequestDTO {
    private String productId;
    private String commentContent;
    private int commentLeft;
    private int commentRight;
    private int commentParentId;
}