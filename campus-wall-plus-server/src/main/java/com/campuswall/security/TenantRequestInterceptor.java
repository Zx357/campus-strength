package com.campuswall.security;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campuswall.common.constant.RoleCodes;
import com.campuswall.system.entity.SysTenant;
import com.campuswall.system.mapper.SysTenantMapper;
import com.campuswall.tenant.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantRequestInterceptor implements HandlerInterceptor {
    private final SysTenantMapper tenantMapper;

    public TenantRequestInterceptor(SysTenantMapper tenantMapper) {
        this.tenantMapper = tenantMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TenantContext.clear();
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/platform") || uri.startsWith("/api/admin/auth")) {
            TenantContext.setIgnore(true);
        }
        if (StpUtil.isLogin()) {
            String role = (String) StpUtil.getSession().get("roleCode");
            Object tenantId = StpUtil.getSession().get("tenantId");
            if (RoleCodes.PLATFORM_ADMIN.equals(role)) {
                TenantContext.setIgnore(true);
            } else if (tenantId != null) {
                TenantContext.setTenantId(Long.valueOf(tenantId.toString()));
            }
            return true;
        }
        String tenantCode = request.getHeader("X-Tenant-Code");
        if (tenantCode == null || tenantCode.isBlank()) {
            tenantCode = request.getParameter("tenantCode");
        }
        if (tenantCode != null && !tenantCode.isBlank()) {
            TenantContext.setIgnore(true);
            SysTenant tenant = tenantMapper.selectOne(new LambdaQueryWrapper<SysTenant>().eq(SysTenant::getTenantCode, tenantCode).eq(SysTenant::getStatus, 1).last("limit 1"));
            TenantContext.setIgnore(false);
            if (tenant != null) {
                TenantContext.setTenantId(tenant.getId());
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
    }
}
