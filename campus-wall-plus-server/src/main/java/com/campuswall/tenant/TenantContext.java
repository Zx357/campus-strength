package com.campuswall.tenant;

public final class TenantContext {
    private static final ThreadLocal<Long> TENANT = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> IGNORE = ThreadLocal.withInitial(() -> false);

    private TenantContext() {}

    public static void setTenantId(Long tenantId) {
        TENANT.set(tenantId);
    }

    public static Long getTenantId() {
        return TENANT.get();
    }

    public static void setIgnore(boolean ignore) {
        IGNORE.set(ignore);
    }

    public static boolean isIgnore() {
        return Boolean.TRUE.equals(IGNORE.get());
    }

    public static void clear() {
        TENANT.remove();
        IGNORE.remove();
    }
}
