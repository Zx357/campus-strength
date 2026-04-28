package com.campuswall.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant")
public class SysTenant extends com.campuswall.common.model.BaseEntity {
    private String tenantCode;
    private String tenantName;
    private String logoUrl;
    private String themeColor;
    private String contactName;
    private String contactPhone;
    private Integer status;
    private Integer auditMode;
    private Integer allowAnonymous;
}
