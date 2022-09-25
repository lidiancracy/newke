package com.example.ld.Controller;

import com.example.ld.entity.DiscussPost;
import com.example.ld.entity.User;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.postservice;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName indexcontroller
 * @Description TODO
 * @Date 2022/9/24 18:09
 */
@Slf4j
@Controller
public class indexcontroller {
    /**
     * 实现帖子的显示和分页
     *
     * @return
     */
    @Autowired
    postservice postservice;
    @Autowired
    UserMapper userMapper;
    @RequestMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, 3);// pageNum:当前页码数，第一次进来时默认为1（首页）
        List<DiscussPost> list = postservice.selectall();//list:页面要展示的数据的集合
        PageInfo<DiscussPost> pageInfo = new PageInfo<DiscussPost>(list);//pageInfo:将分页数据和显示的数据封装到PageInfo当中
        model.addAttribute("pageInfo", pageInfo);//将封装好的数据返回到前台页面
        /**
         * 接下来将user 和post对象传入model里面
         */
        List<DiscussPost> postlist = pageInfo.getList();
        List<Map<String, Object>> maps = new ArrayList<>();
        if(postlist!=null){
            for (DiscussPost discussPost : postlist) {
//            根据用户id查出 用户user
                String userId = discussPost.getUserId();
                User user = userMapper.selectById(Integer.parseInt(userId));
                HashMap<String, Object> tempmap = new HashMap<>();
                tempmap.put("user",user);
                tempmap.put("post",discussPost);
                log.info(user.toString());
                log.info(discussPost.toString());
                maps.add(tempmap);
            }
        }
        model.addAttribute("discussPosts",maps);
        return "index";
    }

}
