package com.campuswall.tenant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campuswall.common.result.Result;
import com.campuswall.system.entity.SysTenant;
import com.campuswall.system.service.SysTenantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/tenant")
public class AppTenantController {
    private final SysTenantService service;

    public AppTenantController(SysTenantService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public Result<List<SysTenant>> list() {
        return Result.success(service.list(new LambdaQueryWrapper<SysTenant>().eq(SysTenant::getStatus, 1).orderByDesc(SysTenant::getCreatedAt)));
    }

    @GetMapping("/detail")
    public Result<SysTenant> detail(@RequestParam String tenantCode) {
        return Result.success(service.getOne(new LambdaQueryWrapper<SysTenant>().eq(SysTenant::getTenantCode, tenantCode).last("limit 1")));
    }
}
