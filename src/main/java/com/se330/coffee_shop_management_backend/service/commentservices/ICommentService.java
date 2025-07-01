package com.se330.coffee_shop_management_backend.service.commentservices;

import com.se330.coffee_shop_management_backend.dto.request.comment.CommentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.comment.CommentUpdateDTO;
import com.se330.coffee_shop_management_backend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICommentService {
    Comment findByIdComment(int id);
    Page<Comment> findAllComments(Pageable pageable);
    Page<Comment> findAllCommentsByProductId(String productId, Pageable pageable);
    Comment createComment(CommentCreateRequestDTO commentCreateRequestDTO);
    Comment updateComment(CommentUpdateDTO commentUpdateDTO);
    void deleteComment(int id);
}