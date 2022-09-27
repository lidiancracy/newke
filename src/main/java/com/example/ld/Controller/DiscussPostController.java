package com.example.ld.Controller;


import com.example.ld.Util.ActivateState;
import com.example.ld.Util.communityutil;
import com.example.ld.Util.hostholder;
import com.example.ld.entity.Comment;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@Slf4j
public class DiscussPostController implements ActivateState {

    @Autowired
    private postservice postservice;

    @Autowired
    private hostholder hostHolder;

    /**
     * 发帖子
     * @param title
     * @param content
     * @return
     */
    @PostMapping("/discuss/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return communityutil.getJSONString(403, "你还没有登录！");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId().toString());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        postservice.addDiscussPost(post);

        // 报错的情况统一处理
        return communityutil.getJSONString(0, "发布成功！");
    }

    /**
     * 帖子详情页
     * @param postid
     * @param model
     * @return
     */
    @Autowired
    UserMapper userMapper;
    @GetMapping("/postdetail")
    public String postdetail(@RequestParam String postid,@RequestParam(defaultValue =  "1") Integer pageNum,  Model model){
//        根据帖子id查出帖子所有信息 上半部
        DiscussPost discussPost=postservice.getpostbyid(postid);
        User lz = userMapper.selectById( Integer.parseInt(discussPost.getUserId()));
        int count=postservice.count(postid);

//         帖子一级回复  下半部
        PageHelper.startPage(pageNum,5);
        List<Comment> comments =postservice.selectall_1(postid);
        PageInfo<Comment> pageInfo = new PageInfo<Comment>(comments);
        /**
         * 根据postid查出再将评论分页所有评论
         */
        ArrayList<HashMap<String, Object>> hashMaps = new ArrayList<>();
        List<Comment> list = pageInfo.getList();
        if(list!=null){
//            如果这条post有评论
            for (Comment comment : list) {
//                需要封装一个 user 封装一个comment
                HashMap<String, Object> tempmap = new HashMap<>();
                User user = userMapper.selectById(comment.getUserId());
                tempmap.put("comment",comment);
                tempmap.put("user",user);
                hashMaps.add(tempmap);
            }
        }
        model.addAttribute("comments",hashMaps);
        model.addAttribute("post",discussPost);
        model.addAttribute("user",lz);
//        model.addAttribute("replyCount",count);
        model.addAttribute("pageInfo",pageInfo);
        return "/site/discuss-detail";
    }
}
