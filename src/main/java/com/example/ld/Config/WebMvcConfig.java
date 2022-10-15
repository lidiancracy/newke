package com.example.ld.Config;


import com.example.ld.interceptor.interceptordeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Autowired
    private interceptordeal interceptordeal;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor(interceptordeal)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

    }
}
