package com.campuswall.dashboard.controller;

import com.campuswall.common.result.Result;
import com.campuswall.post.mapper.WallPostMapper;
import com.campuswall.report.mapper.ReportRecordMapper;
import com.campuswall.system.mapper.SysTenantMapper;
import com.campuswall.user.mapper.SysUserMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {
    private final SysUserMapper userMapper;
    private final WallPostMapper postMapper;
    private final ReportRecordMapper reportMapper;
    private final SysTenantMapper tenantMapper;

    public DashboardController(SysUserMapper userMapper, WallPostMapper postMapper, ReportRecordMapper reportMapper, SysTenantMapper tenantMapper) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.reportMapper = reportMapper;
        this.tenantMapper = tenantMapper;
    }

    @GetMapping("/summary")
    public Result<Map<String, Long>> summary() {
        return Result.success(Map.of(
                "userCount", userMapper.selectCount(null),
                "postCount", postMapper.selectCount(null),
                "pendingReportCount", reportMapper.selectCount(null),
                "tenantCount", tenantMapper.selectCount(null)
        ));
    }

    @GetMapping("/post-trend")
    public Result<List<Map<String, Object>>> trend() {
        return Result.success(List.of());
    }

    @GetMapping("/content-ratio")
    public Result<List<Map<String, Object>>> ratio() {
        return Result.success(List.of());
    }
}
