package com.campuswall.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends com.campuswall.common.model.BaseEntity {
    private String roleCode;
    private String roleName;
    private String roleType;
    private Integer status;
}
