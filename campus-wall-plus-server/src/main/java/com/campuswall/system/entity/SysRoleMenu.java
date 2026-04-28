package com.campuswall.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_menu")
public class SysRoleMenu extends com.campuswall.common.model.BaseEntity {
    private Long roleId;
    private Long menuId;
}
