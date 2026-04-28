package com.campuswall.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_operation_log")
public class SysOperationLog extends com.campuswall.common.model.BaseEntity {
    private Long tenantId;
    private Long userId;
    private String username;
    private String moduleName;
    private String operationType;
    private String requestMethod;
    private String requestUrl;
    private String requestParams;
    private String ip;
    private String userAgent;
    private String result;
    private String errorMsg;
}
