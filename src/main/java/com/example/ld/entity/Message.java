package com.example.ld.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * (Message)表实体类
 *
 * @author makejava
 * @since 2022-09-17 19:57:22
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private Integer id;


    private Integer fromId;

    private Integer toId;

    private String conversationId;

    private String content;
    //0-未读;1-已读;2-删除;
    private Integer status;

    private Date createTime;


}
