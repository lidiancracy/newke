package com.example.ld.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * (LoginTicket)表实体类
 * 登陆成功后我们会得到一个用户凭证
 * @author makejava
 * @since 2022-09-17 19:57:21
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginTicket {

    private Integer id;

    private Integer userId;

    private String ticket;
    //0-有效; 1-无效;
    private Integer status;
    //凭证什么时候失效
    private Date expired;


}
