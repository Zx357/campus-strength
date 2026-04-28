package com.campuswall.post.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wall_post")
public class WallPost extends com.campuswall.common.model.TenantEntity {
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private String images;
    private String topicTags;
    private String location;
    private Integer anonymous;
    private Integer status;
    private String auditReason;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    private Integer viewCount;
    private java.math.BigDecimal hotScore;
}
