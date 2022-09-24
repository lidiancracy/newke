package com.example.ld.Util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
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

    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }
}
