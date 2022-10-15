package com.example.ld.interceptor;

/**
 * @ClassName interceptor
 * @Description TODO
 * @Date 2022/9/21 15:46
 */

import com.example.ld.Util.Cookieutils;
import com.example.ld.Util.RedisKeyUtil;
import com.example.ld.Util.hostholder;
import com.example.ld.entity.LoginTicket;
import com.example.ld.entity.User;
import com.example.ld.mapper.LoginTicketMapper;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @ClassName interceptor
 * @Description TODO
 * @Date 2022/9/21 15:14
 */
@Component
@Slf4j
public class interceptordeal implements HandlerInterceptor {

    @Autowired
    UserMapper userMapper;
    @Autowired
    com.example.ld.Util.hostholder hostholder;

    /**
     * prehandle一般没仇 都返回个true就完事了
     * 我们在这层主要就是获取个用户对象,有的话就获取存入，没有就算了，无所谓
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = Cookieutils.getticket(request, "ticket");
        if(ticket!=null ){
//            凭证存在,还要验证状态码以及是否超时
            String redisKey = RedisKeyUtil.getTicketKey(ticket);
            LoginTicket selectbyticket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);

            if(selectbyticket!=null && selectbyticket.getStatus()==0&& selectbyticket.getExpired().after(new Date())){
                User user = userMapper.selectById(selectbyticket.getUserId());
                log.info(String.valueOf(user));
//                用 hostholder 将用户存下来
                hostholder.setUser(user);
                //构建用户认证结果，存入SecurityContext，以便Security进行授权
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        // principal: 主要信息; credentials: 证书; authorities: 权限;
                        /**
                         * 每一个请求 请求前，ss会判断，诶 他有没有登录，如果登陆了的话，就将权限 user信息存入到ss的context里面
                         */
                        user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }
//        log.info("prehandler");
        return  true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostholder.getUser();
        /**
         * 我们几乎所有页面都有header，header中需要用户信息,所以这里注入非常合理
         */
        if (user !=null && modelAndView !=null){
            modelAndView.addObject("loginUser",user);
        }
//        log.info("posthandler");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostholder.clear();

//        log.info("afthandler");
    }
}
