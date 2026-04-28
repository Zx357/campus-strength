package com.campuswall.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("report_record")
public class ReportRecord extends com.campuswall.common.model.TenantEntity {
    private Long userId;
    private String targetType;
    private Long targetId;
    private String reasonType;
    private String reasonContent;
    private String images;
    private Integer status;
    private String handleResult;
    private Long handlerId;
    private java.time.LocalDateTime handledAt;
}
