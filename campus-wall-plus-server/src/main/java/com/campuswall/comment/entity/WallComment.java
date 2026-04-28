package com.campuswall.comment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wall_comment")
public class WallComment extends com.campuswall.common.model.TenantEntity {
    private Long postId;
    private Long userId;
    private Long parentId;
    private Long replyUserId;
    private String content;
    private Integer status;
    private Integer likeCount;
}
