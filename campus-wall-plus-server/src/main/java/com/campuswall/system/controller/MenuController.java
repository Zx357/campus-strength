package com.campuswall.system.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.system.entity.SysMenu;
import com.campuswall.system.service.SysMenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/menu")
public class MenuController extends CrudController<SysMenu> {
    private final SysMenuService service;

    public MenuController(SysMenuService service) {
        this.service = service;
    }

    @Override
    protected IService<SysMenu> service() {
        return service;
    }
}
