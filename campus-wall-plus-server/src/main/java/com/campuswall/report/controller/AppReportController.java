package com.campuswall.report.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.report.entity.ReportRecord;
import com.campuswall.report.mapper.ReportRecordMapper;
import com.campuswall.security.SecurityUtils;
import com.campuswall.tenant.TenantContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/report")
public class AppReportController {
    private final ReportRecordMapper mapper;

    public AppReportController(ReportRecordMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping("/page")
    public Result<PageResult<ReportRecord>> page(@Valid PageQuery query) {
        return Result.success(PageResult.of(mapper.selectPage(
                new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<ReportRecord>()
                        .eq(ReportRecord::getUserId, SecurityUtils.userId())
                        .orderByDesc(ReportRecord::getCreatedAt)
        )));
    }

    @PostMapping("/create")
    public Result<Long> create(@RequestBody ReportRecord report) {
        report.setTenantId(TenantContext.getTenantId());
        report.setUserId(SecurityUtils.userId());
        report.setStatus(0);
        mapper.insert(report);
        return Result.success(report.getId());
    }
}
