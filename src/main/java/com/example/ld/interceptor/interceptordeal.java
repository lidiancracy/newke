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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
//    @Autowired
//    LoginTicketMapper loginTicketMapper;
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
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = Cookieutils.getticket(request, "ticket");
        if(ticket!=null ){
//            凭证存在,还要验证状态码以及是否超时
            String redisKey = RedisKeyUtil.getTicketKey(ticket);
            LoginTicket selectbyticket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);

            if(selectbyticket!=null && selectbyticket.getStatus()==0&& selectbyticket.getExpired().after(new Date())){
                User user = userMapper.selectById(selectbyticket.getUserId());
//                用 hostholder 将用户存下来
                hostholder.setUser(user);
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
