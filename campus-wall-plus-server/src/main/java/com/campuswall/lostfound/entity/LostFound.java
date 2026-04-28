package com.campuswall.lostfound.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lost_found")
public class LostFound extends com.campuswall.common.model.TenantEntity {
    private Long userId;
    private Integer type;
    private String title;
    private String description;
    private String images;
    private java.time.LocalDateTime lostTime;
    private String lostLocation;
    private String contactName;
    private String contactPhone;
    private Integer status;
    private String auditReason;
    private Integer viewCount;
}
