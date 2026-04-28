package com.campuswall.user.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.common.dto.StatusDTO;
import com.campuswall.common.exception.BusinessException;
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
    private final SysUserService service;

    public UserController(SysUserService service) {
        this.service = service;
    }

    @Override
    protected IService<SysUser> service() {
        return service;
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
