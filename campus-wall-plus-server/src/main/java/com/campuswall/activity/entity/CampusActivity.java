package com.campuswall.activity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("campus_activity")
public class CampusActivity extends com.campuswall.common.model.TenantEntity {
    private String title;
    private String content;
    private String coverUrl;
    private java.time.LocalDateTime activityTime;
    private String activityLocation;
    private String organizer;
    private Integer signupRequired;
    private java.time.LocalDateTime signupDeadline;
    private Integer maxPeople;
    private Integer status;
}
