package com.campuswall.common.aspect;

import cn.hutool.core.util.StrUtil;
import com.campuswall.security.SecurityUtils;
import com.campuswall.system.entity.SysOperationLog;
import com.campuswall.system.mapper.SysOperationLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class OperationLogAspect {
    private final SysOperationLogMapper logMapper;

    public OperationLogAspect(SysOperationLogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs == null ? null : attrs.getRequest();
        if (request == null || !shouldLog(request)) {
            return point.proceed();
        }
        SysOperationLog log = new SysOperationLog();
        log.setTenantId(SecurityUtils.tenantId());
        log.setUserId(SecurityUtils.userId());
        log.setUsername(String.valueOf(request.getAttribute("username")));
        log.setModuleName(point.getSignature().getDeclaringType().getSimpleName());
        log.setOperationType(request.getMethod());
        log.setRequestMethod(request.getMethod());
        log.setRequestUrl(request.getRequestURI());
        log.setRequestParams(StrUtil.sub(request.getQueryString(), 0, 2000));
        log.setIp(request.getRemoteAddr());
        log.setUserAgent(StrUtil.sub(request.getHeader("User-Agent"), 0, 500));
        try {
            Object result = point.proceed();
            log.setResult("success");
            logMapper.insert(log);
            return result;
        } catch (Throwable e) {
            log.setResult("fail");
            log.setErrorMsg(StrUtil.sub(e.getMessage(), 0, 1000));
            logMapper.insert(log);
            throw e;
        }
    }

    private boolean shouldLog(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        return ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))
                && (uri.startsWith("/api/admin") || uri.startsWith("/api/platform"));
    }
}
