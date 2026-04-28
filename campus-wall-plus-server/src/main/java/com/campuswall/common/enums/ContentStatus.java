package com.campuswall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentStatus {
    PENDING(0, "待审核"),
    PUBLISHED(1, "已发布"),
    REJECTED(2, "已驳回"),
    DELETED(3, "已删除"),
    OFFLINE(4, "已下架");

    private final int code;
    private final String text;
}
