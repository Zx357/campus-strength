package com.campuswall.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateDTO {
    @NotNull(message = "帖子不能为空")
    private Long postId;
    private Long parentId;
    private Long replyUserId;
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
