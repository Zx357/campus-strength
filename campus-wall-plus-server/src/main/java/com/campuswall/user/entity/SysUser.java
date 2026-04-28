package com.campuswall.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends com.campuswall.common.model.TenantEntity {
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private String openid;
    private String studentNo;
    private String realName;
    private String college;
    private String major;
    private String grade;
    private String roleCode;
    private Integer authStatus;
    private Integer status;
    private java.time.LocalDateTime lastLoginTime;
}
