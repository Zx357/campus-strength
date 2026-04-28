package com.campuswall.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TenantEntity extends BaseEntity {
    private Long tenantId;
}
