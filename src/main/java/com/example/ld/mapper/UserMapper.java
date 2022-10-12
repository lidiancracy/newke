package com.example.ld.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ld.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-17 19:57:22
 */
@Mapper
public interface UserMapper  extends BaseMapper<User> {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);
//
    int updateHeader(int id, String headerUrl);
//
    int updatePassword(int id, String password);
}
