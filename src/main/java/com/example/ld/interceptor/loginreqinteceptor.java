package com.example.ld.interceptor;

import com.example.ld.Util.hostholder;
import com.example.ld.annociation.loginrequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @ClassName loginreqinteceptor
 * @Description TODO
 * @Date 2022/9/23 17:48
 */
@Component
public class loginreqinteceptor implements HandlerInterceptor {
    @Autowired
    com.example.ld.Util.hostholder hostholder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        如果拦截的是方法
        if(handler instanceof HandlerMethod){
            HandlerMethod hand = (HandlerMethod) handler;
            Method method = hand.getMethod();
            loginrequired annotation = method.getAnnotation(loginrequired.class);
            if(annotation !=null && hostholder.getUser()==null){
//                如果有这个注解 并且没有 登录就拦截跳转到登录页面
                    response.sendRedirect(request.getContextPath()+"/login");
                    return  false;
            }
        }
        return  true;

    }
}
