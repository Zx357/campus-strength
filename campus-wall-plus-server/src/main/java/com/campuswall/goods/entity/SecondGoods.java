package com.campuswall.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("second_goods")
public class SecondGoods extends com.campuswall.common.model.TenantEntity {
    private Long userId;
    private Long categoryId;
    private String title;
    private String description;
    private String images;
    private java.math.BigDecimal price;
    private java.math.BigDecimal originalPrice;
    private String conditionLevel;
    private String tradeMethod;
    private String location;
    private String contactPhone;
    private Integer status;
    private String auditReason;
    private Integer viewCount;
}
