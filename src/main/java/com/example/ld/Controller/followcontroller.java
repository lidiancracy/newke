package com.example.ld.Controller;

import com.example.ld.Util.ActivateState;
import com.example.ld.Util.communityutil;
import com.example.ld.Util.hostholder;
import com.example.ld.entity.User;
import com.example.ld.service.followService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName followcontroller
 * @Description TODO
 * @Date 2022/10/8 15:27
 */
@Controller
@Slf4j
public class followcontroller implements ActivateState {
    @Autowired
    hostholder hostholder;
    @Autowired
    followService followservice;
    /**
     * 关注某人
     * 如何储存在redis里面
     * 我们需要添加这个人的id到
     * @return
     */
    @PostMapping("/follow")
    @ResponseBody
    public String followpeople(int entityType,int entityId){
        User user = hostholder.getUser();
        if(user!=null){
            Integer userid = user.getId();
            followservice.followsomeone(userid,entityType,entityId);
        }
        return communityutil.getJSONString(0, "已关注！");
    }

    /**
     * 取关某人
     * @return
     */
    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollowpeople(int entityType,int entityId){
        User user = hostholder.getUser();
        if(user!=null){
            Integer userid = user.getId();
            followservice.unfollowsomeone(userid,entityType,entityId);
        }
        return communityutil.getJSONString(0, "已取关！");
    }
}
