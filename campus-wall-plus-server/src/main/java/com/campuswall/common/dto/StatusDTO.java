package com.campuswall.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusDTO {
    @NotNull(message = "ID不能为空")
    private Long id;
    @NotNull(message = "状态不能为空")
    private Integer status;
}
