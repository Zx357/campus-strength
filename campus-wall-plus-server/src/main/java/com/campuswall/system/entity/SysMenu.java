package com.campuswall.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends com.campuswall.common.model.BaseEntity {
    private Long parentId;
    private String menuName;
    private String menuType;
    private String routePath;
    private String component;
    private String permission;
    private String icon;
    private Integer sort;
    private Integer status;
}
