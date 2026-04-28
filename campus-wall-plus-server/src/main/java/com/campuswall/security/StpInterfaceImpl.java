package com.campuswall.security;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.campuswall.common.constant.RoleCodes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String role = (String) StpUtil.getSessionByLoginId(loginId).get("roleCode");
        List<String> permissions = new ArrayList<>();
        if (RoleCodes.PLATFORM_ADMIN.equals(role)) {
            permissions.add("*");
            permissions.add("app:write");
        } else if (RoleCodes.TENANT_ADMIN.equals(role)) {
            permissions.add("tenant:*");
            permissions.add("app:write");
        } else if (RoleCodes.TENANT_AUDITOR.equals(role)) {
            permissions.add("audit:*");
        } else if (RoleCodes.STUDENT.equals(role)) {
            permissions.add("app:write");
        }
        return permissions;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String role = (String) StpUtil.getSessionByLoginId(loginId).get("roleCode");
        return role == null ? List.of() : List.of(role);
    }
}
