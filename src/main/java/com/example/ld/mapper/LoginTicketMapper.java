package com.example.ld.mapper;

import com.example.ld.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName LoginTicketMapper
 * @Description TODO
 * @Date 2022/9/19 19:58
 */
@Mapper
public interface LoginTicketMapper {
    /**
     * 根据ticket查找凭证
     */
    LoginTicket selectbyticket(String ticket);
    /**
     * 插入凭证
     */
    int insertticket(LoginTicket loginTicket);
    /**
     * 更新凭证
     */
    int updateticket(LoginTicket loginTicket,int status);
}
