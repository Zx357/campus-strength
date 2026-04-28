package com.campuswall.system.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.system.entity.SysRole;
import com.campuswall.system.service.SysRoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/role")
public class RoleController extends CrudController<SysRole> {
    private final SysRoleService service;

    public RoleController(SysRoleService service) {
        this.service = service;
    }

    @Override
    protected IService<SysRole> service() {
        return service;
    }
}
