package com.example.ld.Util;

import com.example.ld.entity.User;
import org.springframework.stereotype.Component;

/**
 * @ClassName hostholder
 * @Description TODO
 * @Date 2022/9/21 15:26
 */
@Component
public class hostholder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
