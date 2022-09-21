package com.example.ld.Util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName Cookieutils
 * @Description TODO
 * @Date 2022/9/21 15:16
 */
public class Cookieutils {
    /**
     * 根据 resquest 和 cookie名 获取 cookie 值
     * 我们这里是想根据 ticket名 获取 ticket 字符串
     */
    public static String getticket(HttpServletRequest request,String name){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }

        return  null;
    }
}
