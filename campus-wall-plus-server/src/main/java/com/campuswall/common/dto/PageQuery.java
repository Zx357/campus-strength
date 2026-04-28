package com.campuswall.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageQuery {
    @Min(value = 1, message = "页码不能小于1")
    private long current = 1;
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页最多100条")
    private long size = 10;
    private String keyword;
}
