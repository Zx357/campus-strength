package com.campuswall.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.common.constant.RoleCodes;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.dto.StatusDTO;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.user.entity.SysUser;
import com.campuswall.user.service.SysUserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
public class UserController extends CrudController<SysUser> {
    private static final java.util.List<String> ADMIN_ROLES = java.util.List.of(
            RoleCodes.PLATFORM_ADMIN,
            RoleCodes.TENANT_ADMIN,
            RoleCodes.TENANT_AUDITOR
    );
    private static final java.util.List<String> APP_ROLES = java.util.List.of(RoleCodes.STUDENT, RoleCodes.GUEST);

    private final SysUserService service;

    public UserController(SysUserService service) {
        this.service = service;
    }

    @Override
    protected IService<SysUser> service() {
        return service;
    }

    @GetMapping("/admin-page")
    public Result<PageResult<SysUser>> adminPage(PageQuery query) {
        return Result.success(pageByRoles(query, ADMIN_ROLES));
    }

    @GetMapping("/app-page")
    public Result<PageResult<SysUser>> appPage(PageQuery query) {
        return Result.success(pageByRoles(query, APP_ROLES));
    }

    @PutMapping("/ban")
    public Result<Void> ban(@RequestBody StatusDTO dto) {
        SysUser user = requireUser(dto.getId());
        user.setStatus(dto.getStatus());
        service.updateById(user);
        return Result.success();
    }

    @PutMapping("/auth")
    public Result<Void> auth(@RequestBody AuthStatusDTO dto) {
        SysUser user = requireUser(dto.getId());
        user.setAuthStatus(dto.getAuthStatus());
        service.updateById(user);
        return Result.success();
    }

    @PutMapping("/role")
    public Result<Void> role(@RequestBody RoleDTO dto) {
        SysUser user = requireUser(dto.getId());
        user.setRoleCode(dto.getRoleCode());
        service.updateById(user);
        return Result.success();
    }

    private SysUser requireUser(Long id) {
        SysUser user = service.getById(id);
        if (user == null) {
            throw new BusinessException("User not found");
        }
        return user;
    }

    private PageResult<SysUser> pageByRoles(PageQuery query, java.util.List<String> roles) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>()
                .in("role_code", roles)
                .orderByDesc("created_at");

        if (query.getKeyword() != null && !query.getKeyword().isBlank()) {
            String keyword = query.getKeyword().trim();
            wrapper.and(w -> w.like("username", keyword)
                    .or()
                    .like("nickname", keyword)
                    .or()
                    .like("phone", keyword));
        }

        return PageResult.of(service.page(new Page<>(query.getCurrent(), query.getSize()), wrapper));
    }

    @Data
    public static class AuthStatusDTO {
        @NotNull
        private Long id;
        @NotNull
        private Integer authStatus;
        private String reason;
    }

    @Data
    public static class RoleDTO {
        @NotNull
        private Long id;
        @NotBlank
        private String roleCode;
    }
}
