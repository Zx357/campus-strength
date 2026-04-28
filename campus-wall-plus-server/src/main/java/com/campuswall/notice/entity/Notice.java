package com.campuswall.notice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("notice")
public class Notice extends com.campuswall.common.model.TenantEntity {
    private String title;
    private String content;
    private Integer type;
    private Integer status;
    private Integer topFlag;
}
