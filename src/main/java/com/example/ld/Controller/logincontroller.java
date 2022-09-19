package com.example.ld.Controller;

import com.example.ld.Util.ActivateState;
import com.example.ld.entity.User;
import com.example.ld.service.UserService;
import com.google.code.kaptcha.Producer;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName logincontroller
 * @Description TODO
 * @Date 2022/9/17 16:38
 */
@Slf4j
@Controller
public class logincontroller implements ActivateState {
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }
    @RequestMapping(path = {"/index","/"}, method = RequestMethod.GET)
    public String getindex() {
        return "/index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    // http://localhost:8080/community/activation/id/code
    @GetMapping("/activate/{id}/{code}")
    public String activateemail(Model model,@PathVariable("id") int id,@PathVariable("code") String code){
        int result = userService.activateemail(id, code);
        if (result == ACTIVATED) {
            model.addAttribute("msg", "激活成功，您的账号已经可以正常使用了！");
            model.addAttribute("target", "/login");
        } else if (result == REPEATACTIVATE) {
            model.addAttribute("msg", "无效的操作，该账号已经被激活了！");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败，您提供的激活码是错误的！");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

//获取验证图片
@Autowired
private Producer producer;

    /**
     * 获得生成的随机验证码
     * @param response
     * @param httpSession
     */
    @GetMapping("/kaptcha")
    public void getKapatchaImage(HttpServletResponse response, HttpSession httpSession){
        //生成验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);

        //验证码字符串写入到session中.服务器将依托cookie 【key,value】value中存放的是当前生成的sessionid.
        //以后浏览器访问浏览器时，将会携带该cookie信息，使得多个浏览器请求中携带重要的信息【基于HTTP请求是无状态的，将验证字符串存入到session中，保存到服务中】
        httpSession.setAttribute("kapatchaStr", text);

        //图片响应给response
        //响应格式为.png的图片
        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            //响应给浏览器的是验证码图片
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
