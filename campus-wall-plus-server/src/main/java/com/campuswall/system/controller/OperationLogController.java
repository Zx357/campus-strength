package com.campuswall.system.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.system.entity.SysOperationLog;
import com.campuswall.system.service.SysOperationLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/operation-log")
public class OperationLogController extends CrudController<SysOperationLog> {
    private final SysOperationLogService service;

    public OperationLogController(SysOperationLogService service) {
        this.service = service;
    }

    @Override
    protected IService<SysOperationLog> service() {
        return service;
    }
}
