package com.campuswall.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("message_record")
public class MessageRecord extends com.campuswall.common.model.TenantEntity {
    private Long userId;
    private String messageType;
    private String title;
    private String content;
    private String targetType;
    private Long targetId;
    private Integer readFlag;
}
