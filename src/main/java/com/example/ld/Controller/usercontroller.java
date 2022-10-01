package com.example.ld.Controller;

import com.example.ld.Util.RedisKeyUtil;
import com.example.ld.Util.communityutil;
import com.example.ld.Util.hostholder;
import com.example.ld.annociation.loginrequired;
import com.example.ld.entity.User;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.invoke.VarHandle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName usercontroller
 * @Description TODO
 * @Date 2022/9/23 14:37
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class usercontroller {
    @loginrequired
    @GetMapping("/setting")
    public String getsetting(){
        return "/site/setting";
    }

    /**
     * 上传头像
     * @return
     */
    @Autowired
    hostholder hostholder;
    @Autowired
    UserService userService;
    @loginrequired
    @PostMapping("/upload")
    public String upload(MultipartFile headimage){
        String filename = headimage.getOriginalFilename();
        String date = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDateTime.now());
        filename =  date+System.currentTimeMillis()+filename.substring(filename.lastIndexOf("."));
        String name = null;
        try {
            name = userService.upload(headimage.getInputStream(), filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(name);
        User user = hostholder.getUser();
        userService.changepic(user.getId(),name);
        return "redirect:/index";
    }
    /**
     * 修改密码
     */
    @loginrequired
    @PostMapping("/changepassword")
    public String changepassword(String originalpassword, String newpassword, String newpsagain, Model model){
        User user = hostholder.getUser();
        String inputpass = originalpassword + hostholder.getUser().getSalt();
        if(communityutil.md5(inputpass).equals(user.getPassword()) ){
            log.info("原始密码正确");
            boolean checkchangepassword = userService.checkchangepassword(user.getId(),newpassword, newpsagain);
            if(checkchangepassword){
                log.info("密码修改成功");
            }else {
                log.error("新输入的密码有问题");
            }
            return "redirect:/index";
        }else {
            log.error( "原始密码错误");
            return "/site/setting";
        }

    }

    /**
     * 获取用户简介
     * /user/profile/userid
     * @return
     */
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;
    @GetMapping("/profile/{userid}")
    public String getprofile(@PathVariable int userid,Model model){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userid);
        Object o = redisTemplate.opsForValue().get(userLikeKey);
        if(o !=null){
            model.addAttribute("likeCount",(int)o);
        }

        User user = userMapper.selectById(userid);
        if(user!=null){
            model.addAttribute("user",user);
        }

        return "/site/profile";
    }

}
