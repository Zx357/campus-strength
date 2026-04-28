package com.campuswall.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WxLoginDTO {
    @NotBlank(message = "学校编码不能为空")
    private String tenantCode;
    private String code;
    private String nickname;
    private String avatarUrl;
}
