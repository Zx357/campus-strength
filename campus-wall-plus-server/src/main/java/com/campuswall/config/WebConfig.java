package com.campuswall.config;

import com.campuswall.security.TenantRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final TenantRequestInterceptor tenantRequestInterceptor;

    @Value("${campus-wall.upload.local-dir:uploads}")
    private String localDir;
    @Value("${campus-wall.upload.url-prefix:/uploads/}")
    private String urlPrefix;

    public WebConfig(TenantRequestInterceptor tenantRequestInterceptor) {
        this.tenantRequestInterceptor = tenantRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantRequestInterceptor).addPathPatterns("/api/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOriginPatterns("*").allowedMethods("*").allowedHeaders("*").allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(urlPrefix + "**").addResourceLocations("file:" + localDir + "/");
    }
}
