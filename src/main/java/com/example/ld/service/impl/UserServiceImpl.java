package com.example.ld.service.impl;


import com.example.ld.Util.ActivateState;
import com.example.ld.Util.communityutil;
import com.example.ld.entity.LoginTicket;
import com.example.ld.entity.User;
import com.example.ld.mapper.LoginTicketMapper;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2022-09-17 19:57:22
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService, ActivateState {

    @Autowired
    private UserMapper userMapper;

    @Value("${community.domain}")
    private String domain;
    @Value("${community.email}")
    private String myemail;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private LoginTicketMapper loginTicketMapper;


    /**
     * @author: 83799
     * @date: 2022/9/17 21:17
     * @dscription: 主要接受提交的三个参数 username email password
     * @Param:* @Param null:
     * @Return:* @return: null
     */
    @Override
    public Map<String, Object> register(User user) {
        HashMap<String, Object> map = new HashMap<>();
//        空值验证
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (!StringUtils.hasText(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (!StringUtils.hasText(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (!StringUtils.hasText(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }
        //        重复性验证
        User user1 = userMapper.selectByName(user.getUsername());
        if (user1 != null) {
            map.put("usernameMsg", "用户名重复");
            return map;
        }
        User user2 = userMapper.selectByEmail(user.getEmail());
        if (user2 != null) {
            map.put("emailMsg", "email重复");
            return map;
        }
//        否则正常注册
        user.setSalt(communityutil.generateuuid().substring(0, 5));
        user.setPassword(communityutil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(communityutil.generateuuid());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
//        我们还需要把邮件发送给我用户
//        邮箱格式为 localhost8080/activate/userid/activatecode
        String url = domain + "/activate/" + user.getId() + "/" + user.getActivationCode();
//        发送邮件
        log.info(url);
        sendemail(myemail, "激活邮件", url, user.getEmail());
        return map;
    }

    @Override
    public int activateemail(int id, String code) {
        User user = userMapper.selectById(id);
        if (user.getStatus() == 1) {
//
            return ActivateState.REPEATACTIVATE;
        } else if (user.getStatus() == 0) {
//            还未激活
            if (user.getActivationCode().equals(code)) {
                userMapper.updateStatus(id, 1);
                return ActivateState.ACTIVATED;
            } else {
                return ActivateState.UNACTIVATED;
            }
        } else {
            return ActivateState.REPEATACTIVATE;
        }

    }

    @Override
    public Map<String, Object> login(String username, String password, int expiretime) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.hasText(username)) {
            map.put("usernameMsg", "用户名为空");
            return map;
        }
        if (!StringUtils.hasText(password)) {
            map.put("passwordMsg", "密码为空");
            return map;
        }
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "用户不存在");
            return map;
        }
        if (!communityutil.md5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("passwordMsg", "密码错误");
            return map;
        }
        if (user.getStatus() == ActivateState.UNACTIVATED) {
            map.put("usernameMsg", "账号未激活");
            return map;
        }
//        否则生成凭证,存入数据库中,还得把ticket告诉浏览器 这样才能建立连接
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(communityutil.generateuuid());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiretime * 1000));
        loginTicketMapper.insertticket(loginTicket);
        map.put("ticket",loginTicket.getTicket());
        return  map;
    }

    /**
     * 退出登录
     * @param ticket
     */
    @Override
    public void logout(String ticket) {
        LoginTicket selectbyticket = loginTicketMapper.selectbyticket(ticket);
        loginTicketMapper.updateticket(selectbyticket,1);
    }


    private void sendemail(String myemail, String title, String url, String s) {
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom(myemail);
        //谁要接收
        message.setTo(s);
        //邮件标题
        message.setSubject(title);
        //邮件内容
        message.setText(url);
        try {
            javaMailSender.send(message);
            log.info("发送成功");
        } catch (MailException e) {
            e.printStackTrace();
            log.error("发送失败");
        }
    }


}
