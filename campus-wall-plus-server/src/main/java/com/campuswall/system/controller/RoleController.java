package com.campuswall.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.system.entity.SysRole;
import com.campuswall.system.service.SysRoleService;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/admin-page")
    public Result<PageResult<SysRole>> adminPage(PageQuery query) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>()
                .ne("role_type", "app")
                .orderByDesc("created_at");

        if (query.getKeyword() != null && !query.getKeyword().isBlank()) {
            String keyword = query.getKeyword().trim();
            wrapper.and(w -> w.like("role_name", keyword).or().like("role_code", keyword));
        }

        return Result.success(PageResult.of(service.page(new Page<>(query.getCurrent(), query.getSize()), wrapper)));
    }
}
