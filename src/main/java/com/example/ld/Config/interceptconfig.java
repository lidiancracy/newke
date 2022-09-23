package com.example.ld.Config;

import com.example.ld.interceptor.interceptordeal;
import com.example.ld.interceptor.loginreqinteceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName interceptconfig
 * @Description TODO
 * @Date 2022/9/21 15:47
 */
@Configuration
public class interceptconfig implements WebMvcConfigurer {
    @Autowired
    private interceptordeal interceptordeal;
    @Autowired
    private loginreqinteceptor loginreqinteceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        我们这里没写拦截地址，默认拦截所有
        registry.addInterceptor(interceptordeal)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpeg");
        registry.addInterceptor(loginreqinteceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpeg");
    }
}
