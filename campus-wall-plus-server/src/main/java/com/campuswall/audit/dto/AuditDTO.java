package com.campuswall.audit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditDTO {
    @NotBlank(message = "审核对象不能为空")
    private String targetType;
    @NotNull(message = "对象ID不能为空")
    private Long targetId;
    private String reason;
}
