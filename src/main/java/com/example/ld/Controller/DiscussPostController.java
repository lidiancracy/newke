package com.example.ld.Controller;


import com.example.ld.Event.producer;
import com.example.ld.Util.ActivateState;
import com.example.ld.Util.RedisKeyUtil;
import com.example.ld.Util.communityutil;
import com.example.ld.Util.hostholder;
import com.example.ld.annociation.loginrequired;
import com.example.ld.entity.*;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.CommentService;
import com.example.ld.service.impl.like_serviceimpl;
import com.example.ld.service.postservice;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
     *
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
     *
     * @param postid
     * @param model
     * @return
     */
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentService commentservice;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    hostholder hostholder;
    @Autowired
    like_serviceimpl like_serviceimpl;

    @GetMapping("/postdetail/{postid}")
    public String postdetail(@PathVariable String postid, Model model, Page page) {
//        根据帖子id查出帖子所有信息 上半部
        DiscussPost discussPost = postservice.getpostbyid(postid);
        User lz = userMapper.selectById(Integer.parseInt(discussPost.getUserId()));

        model.addAttribute("user", lz);
        int count = postservice.count(postid);
        postservice.updatecount(postid, count);
        discussPost.setCommentCount(count);
        model.addAttribute("post", discussPost);

//        likeStatus likeCount 传一下 ，每次刷新页面显示
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(1, Integer.parseInt(postid));
        Long size = redisTemplate.opsForSet().size(entityLikeKey);
        int likestate = 0;
        if (hostholder.getUser() != null) {
            if (redisTemplate.opsForSet().isMember(entityLikeKey, hostholder.getUser().getId())) {
                likestate = 1;
            }
        }

        model.addAttribute("likeStatus", likestate);
        model.addAttribute("likeCount", size);
        /**
         * 评论分页设置
         * seltlimit 是设置每页几个
         * setpath 是设置页面上下页跳转,路径永远不变，单参数一直变
         * set rows 是设置一共多少条信息，用于计算各页信息
         */
        page.setLimit(5);
        page.setPath("/postdetail/" + postid);
        page.setRows(discussPost.getCommentCount());
        /**
         * 查询出每页的实体集合
         * 渲染前端
         */
        List<Comment> commentList = commentservice.findcommentlist_page(
                YIJI_TITLE, postid, page.getOffset(), page.getLimit());
        ArrayList<HashMap<String, Object>> comments = new ArrayList<>();
        if (commentList != null && !commentList.isEmpty()) {
            for (Comment comment : commentList) {
                Integer userId = comment.getUserId();
                User user = userMapper.selectById(userId);
                HashMap<String, Object> cvo = new HashMap<>();
                if (user != null) {
                    cvo.put("user", user);
                }
                cvo.put("comment", comment);
//                like
                long entityLikeCount = like_serviceimpl.findEntityLikeCount(2, comment.getId());
                cvo.put("likeCount", entityLikeCount);
                if (hostholder.getUser() != null) {
                    int entityLikeStatus = like_serviceimpl.findEntityLikeStatus(hostholder.getUser().getId(), 2, comment.getId());
                    cvo.put("likeStatus", entityLikeStatus);
                }
                comments.add(cvo);
            }
        }
        model.addAttribute("comments", comments);
        return "/site/discuss-detail";
    }


    /**
     * 添加评论，添加完毕后
     * 重定向 渲染
     */

    @Autowired
    producer producer;

    @loginrequired
    @PostMapping("/addacomment")
    public String addcomment(String postid, String content) {
        Integer id = hostholder.getUser().getId();
        DiscussPost getpostbyid = postservice.getpostbyid(postid);
        Integer userid = null;
        if (getpostbyid != null) {
            userid = Integer.valueOf(getpostbyid.getUserId());
        }
        postservice.addcomment(id, postid, content);
//        添加评论 触发消息

        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(id)
                .setEntityType(ENTITY_TYPE_COMMENT)
                .setEntityId(Integer.parseInt(postid))
                .setEntityUserId(userid);
        producer.senmsg(event);

        return "redirect:/postdetail/" + postid;
    }
}
