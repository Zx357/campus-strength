package com.campuswall.activity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.campuswall.common.model.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("campus_activity_signup")
public class CampusActivitySignup extends TenantEntity {
    private Long activityId;
    private Long userId;
    private Integer status;
    private String contactPhone;
    private String remark;
}
