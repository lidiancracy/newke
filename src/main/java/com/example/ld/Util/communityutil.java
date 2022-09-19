package com.example.ld.Util;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @ClassName communityutil
 * @Description TODO
 * @Date 2022/9/17 16:51
 */
public class communityutil {
//    生成随机字符串
    public static String generateuuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

//    MD5加密,只能加密不能解密
//    所以涉及到salt的概念 不管你密码是什么 我都给你加上几个随机字母后加密
    public static String md5(String key){
        if(!StringUtils.hasText(key)){
//            如果为空 包括null length=0 空格字符
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
