package com.campuswall.security;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.campuswall.common.constant.RoleCodes;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
            SaRouter.match("/api/admin/**")
                    .notMatch("/api/admin/auth/login")
                    .check(r -> StpUtil.checkLogin());
            SaRouter.match("/api/platform/**", r -> StpUtil.checkRole(RoleCodes.PLATFORM_ADMIN));
            SaRouter.match("/api/common/file/upload", StpUtil::checkLogin);
            for (String path : new String[]{
                    "/api/app/user/profile", "/api/app/message/**", "/api/app/post/my-page",
                    "/api/app/post/my-collect-page", "/api/app/comment/my-page",
                    "/api/app/goods/my-page", "/api/app/lost-found/my-page",
                    "/api/app/help/my-page", "/api/app/activity/my-signup-page",
                    "/api/app/report/page"
            }) {
                SaRouter.match(path, StpUtil::checkLogin);
            }
            for (String path : new String[]{
                    "/api/app/post/publish", "/api/app/post/update", "/api/app/post/delete",
                    "/api/app/comment/create", "/api/app/comment/delete",
                    "/api/app/report/create", "/api/app/post/like", "/api/app/post/collect",
                    "/api/app/lost-found/publish", "/api/app/lost-found/update",
                    "/api/app/lost-found/delete", "/api/app/lost-found/found",
                    "/api/app/goods/publish", "/api/app/goods/update", "/api/app/goods/delete",
                    "/api/app/goods/sold", "/api/app/help/publish", "/api/app/help/update",
                    "/api/app/help/delete", "/api/app/help/finish",
                    "/api/app/activity/signup", "/api/app/activity/cancel-signup"
            }) {
                SaRouter.match(path, r -> {
                    StpUtil.checkLogin();
                    StpUtil.checkPermission("app:write");
                });
            }
        })).addPathPatterns("/**")
                .excludePathPatterns("/api/admin/auth/login", "/api/app/auth/**", "/api/app/tenant/**");
    }
}
