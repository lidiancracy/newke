package com.example.ld.service;


import com.example.ld.entity.User;


import java.io.InputStream;
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
    /**
     * 文件上传
     */
    String upload(InputStream inputStream , String fileName);
    /**
     * 根据用户id 更改头像链接
     */
    int changepic(Integer id,String url);

    boolean checkchangepassword(Integer id,String newpassword, String newpsagain);
}
