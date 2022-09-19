package com.example.ld.Controller;

import com.example.ld.Util.ActivateState;
import com.example.ld.entity.User;
import com.example.ld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @ClassName logincontroller
 * @Description TODO
 * @Date 2022/9/17 16:38
 */
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


}
