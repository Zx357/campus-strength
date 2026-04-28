package com.campuswall.auth.controller;

import com.campuswall.auth.dto.WxLoginDTO;
import com.campuswall.auth.service.AuthService;
import com.campuswall.auth.vo.LoginVO;
import com.campuswall.common.result.Result;
import com.campuswall.user.entity.SysUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppAuthController {
    private final AuthService authService;

    public AppAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/app/auth/wx-login")
    public Result<LoginVO> wxLogin(@Valid @RequestBody WxLoginDTO dto) {
        return Result.success(authService.wxLogin(dto));
    }

    @PostMapping("/api/app/auth/guest")
    public Result<LoginVO> guest(@RequestParam @NotBlank(message = "学校编码不能为空") String tenantCode) {
        return Result.success(authService.guest(tenantCode));
    }

    @GetMapping("/api/app/user/profile")
    public Result<SysUser> profile() {
        return Result.success(authService.profile());
    }
}
