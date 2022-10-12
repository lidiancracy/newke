package com.example.ld.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ld.Util.ActivateState;
import com.example.ld.Util.RedisKeyUtil;
import com.example.ld.Util.communityutil;
import com.example.ld.Util.hostholder;
import com.example.ld.entity.LoginTicket;
import com.example.ld.entity.User;
import com.example.ld.mapper.LoginTicketMapper;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.UserService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2022-09-17 19:57:22
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, ActivateState {

    @Autowired
    private UserMapper userMapper;

    @Value("${community.domain}")
    private String domain;
    @Value("${community.email}")
    private String myemail;
    @Autowired
    private JavaMailSender javaMailSender;
//    @Autowired
//    private LoginTicketMapper loginTicketMapper;


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

    /**
     * 激活邮箱
     * @param id
     * @param code
     * @return
     */
    @Override
    public int activateemail(int id, String code) {

        User cache = getCache(id);
        User user=null;
        if(cache==null){
            user = initCache(id);
        }else {
            user=cache;
        }
        if (user.getStatus() == 1) {
//
            return ActivateState.REPEATACTIVATE;
        } else if (user.getStatus() == 0) {
//            还未激活
            if (user.getActivationCode().equals(code)) {
                userMapper.updateStatus(id, 1);
                clearCache(id);
                return ActivateState.ACTIVATED;
            } else {
                return ActivateState.UNACTIVATED;
            }
        } else {
            return ActivateState.REPEATACTIVATE;
        }

    }

    /**
     * 登录验证
     * @param username
     * @param password
     * @param expiretime
     * @return
     */
    @Autowired
    RedisTemplate redisTemplate;
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

        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return  map;
    }

    /**
     * 退出登录
     * @param ticket
     */
    @Override
    public void logout(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    /**
     * 发送邮件api
     * @param myemail
     * @param title
     * @param url
     * @param s
     */
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
    /**
     * 头像文件上传api
     * @param inputStream
     * @param fileName
     * @return
     */
    @Value("${qiniu.accesskey}")
    String accesskey;
    @Value("${qiniu.secretkey}")
    String secretkey;
    @Value("${qiniu.domain}")
    String prewebside;
    @Override
    public String upload(InputStream inputStream, String fileName) {
        //构造一个带指定 Region 对象的配置类
        //Region.region2();代表华南地区
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //AK和SK从七牛云个人中心秘钥管理查看
        String accessKey = accesskey;
        String secretKey = secretkey;
        //bucket就是创建
        String bucket = "niuke-community";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        String result = null;

        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                if(response.statusCode==200){
                    //返回图片上传的路径地址

                    result = prewebside+fileName;
                }
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return result;
    }

    /**
     * 根据用户id 改头像
     */
    @Override
    public int changepic(Integer id, String url) {
        int i = userMapper.updateHeader(id, url);
        clearCache(id);
        return i;
    }

    /**
     * 验证输入两遍新密码的合理性，以及更新操作
     * @param newpassword
     * @param newpsagain
     * @return
     */
    @Autowired
    com.example.ld.Util.hostholder hostholder;
    @Override
    public boolean checkchangepassword(Integer id,String newpassword, String newpsagain) {
        if(!newpassword.equals(newpsagain)){
            return false;
        }else {
            userMapper.updatePassword(id,communityutil.md5(newpassword+hostholder.getUser().getSalt()) );
            return  true;
        }
    }

    // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2。取不到时就初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.当数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

}
