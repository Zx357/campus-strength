package com.campuswall.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sensitive_word")
public class SensitiveWord extends com.campuswall.common.model.BaseEntity {
    private Long tenantId;
    private String word;
    private Integer level;
    private Integer status;
}
