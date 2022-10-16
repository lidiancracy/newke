package com.example.ld.interceptor;

import com.example.ld.Util.hostholder;
import com.example.ld.entity.User;
import com.example.ld.service.impl.data_statisticsserviceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName statistic_interceptor
 * @Description TODO
 * @Date 2022/10/16 16:18
 */
@Component
public class statistic_interceptor implements HandlerInterceptor {
    @Autowired
    com.example.ld.Util.hostholder hostholder;
    @Autowired
    data_statisticsserviceimpl tj;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        记录uv
        tj.uv_writer(request.getRemoteHost());
//        记录dau
        if (hostholder.getUser() != null) {
            User user = hostholder.getUser();
            tj.dau_writer(user.getId());
        }
        return true;
    }
}
