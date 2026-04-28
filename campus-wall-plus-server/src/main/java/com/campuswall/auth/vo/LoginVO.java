package com.campuswall.auth.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {
    private String token;
    private Long userId;
    private Long tenantId;
    private String username;
    private String nickname;
    private String roleCode;
}
