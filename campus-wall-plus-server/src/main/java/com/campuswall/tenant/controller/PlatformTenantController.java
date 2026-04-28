package com.campuswall.tenant.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.common.result.Result;
import com.campuswall.system.entity.SysTenant;
import com.campuswall.system.service.SysTenantService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/platform/tenant")
public class PlatformTenantController extends CrudController<SysTenant> {
    private final SysTenantService service;

    public PlatformTenantController(SysTenantService service) {
        this.service = service;
    }

    @Override
    protected IService<SysTenant> service() {
        return service;
    }

    @GetMapping("/stats")
    public Result<Long> stats() {
        return Result.success(service.count());
    }
}
