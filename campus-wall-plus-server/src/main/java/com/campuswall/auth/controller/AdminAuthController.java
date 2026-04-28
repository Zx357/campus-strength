package com.campuswall.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.campuswall.auth.dto.LoginDTO;
import com.campuswall.auth.service.AuthService;
import com.campuswall.auth.vo.LoginVO;
import com.campuswall.common.result.Result;
import com.campuswall.user.entity.SysUser;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {
    private final AuthService authService;

    public AdminAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.adminLogin(dto));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success();
    }

    @GetMapping("/profile")
    public Result<SysUser> profile() {
        return Result.success(authService.profile());
    }
}
