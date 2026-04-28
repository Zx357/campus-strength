package com.campuswall.category.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_category")
public class BizCategory extends com.campuswall.common.model.TenantEntity {
    private String moduleCode;
    private String categoryName;
    private String icon;
    private Integer sort;
    private Integer status;
}
