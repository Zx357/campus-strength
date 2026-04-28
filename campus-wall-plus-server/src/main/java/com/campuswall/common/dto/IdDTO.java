package com.campuswall.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IdDTO {
    @NotNull(message = "ID不能为空")
    private Long id;
}
