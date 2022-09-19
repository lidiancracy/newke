package com.example.ld.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2022-09-17 19:57:22
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;


    private String username;

    private String password;

    private String salt;

    private String email;
    //0-普通用户; 1-超级管理员; 2-版主;
    private Integer type;
    //0-未激活; 1-已激活;
    private Integer status;

    private String activationCode;

    private String headerUrl;

    private Date createTime;


}
