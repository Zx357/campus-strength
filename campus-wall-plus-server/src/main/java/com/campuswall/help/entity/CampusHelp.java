package com.campuswall.help.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("campus_help")
public class CampusHelp extends com.campuswall.common.model.TenantEntity {
    private Long userId;
    private Integer type;
    private String title;
    private String content;
    private String images;
    private java.math.BigDecimal rewardAmount;
    private String location;
    private Integer status;
}
