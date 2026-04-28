package com.campuswall.security;

import cn.dev33.satoken.stp.StpUtil;
import com.campuswall.common.constant.RoleCodes;

public final class SecurityUtils {
    private SecurityUtils() {}

    public static Long userId() {
        return StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
    }

    public static Long tenantId() {
        Object tenantId = StpUtil.isLogin() ? StpUtil.getSession().get("tenantId") : null;
        return tenantId == null ? null : Long.valueOf(tenantId.toString());
    }

    public static String roleCode() {
        Object role = StpUtil.isLogin() ? StpUtil.getSession().get("roleCode") : null;
        return role == null ? RoleCodes.GUEST : role.toString();
    }

    public static boolean isPlatformAdmin() {
        return RoleCodes.PLATFORM_ADMIN.equals(roleCode());
    }
}
