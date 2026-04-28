package com.campuswall.post.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("wall_collect")
@Data
public class WallCollect {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long userId;
    private Long tenantId;
    private String targetType;
    private Long targetId;
    @TableField(fill = FieldFill.INSERT)
    private java.time.LocalDateTime createdAt;
}
