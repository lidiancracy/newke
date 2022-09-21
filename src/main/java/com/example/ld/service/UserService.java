package com.example.ld.service;


import com.example.ld.entity.User;


import java.util.Map;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2022-09-17 19:57:22
 */
public interface UserService {
    public Map<String,Object> register(User user);

    int activateemail(int id, String code) ;

    /**
     * 判断 账号密码验证码
     * @param username
     * @param password
     * @param expiretime
     * @return
     */
    public Map<String,Object> login(String username,String password,int expiretime);

    void logout(String ticket);
}
