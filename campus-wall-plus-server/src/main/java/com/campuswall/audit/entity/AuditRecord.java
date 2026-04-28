package com.campuswall.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("audit_record")
public class AuditRecord extends com.campuswall.common.model.TenantEntity {
    private String targetType;
    private Long targetId;
    private Integer auditStatus;
    private String auditReason;
    private Long auditorId;
    private java.time.LocalDateTime auditedAt;
}
