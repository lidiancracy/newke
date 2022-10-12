package com.example.ld.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ld.Util.ActivateState;
import com.example.ld.entity.Message;
import com.example.ld.entity.User;
import com.example.ld.mapper.MessageMapper;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.MSGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName Msgservimpl
 * @Description TODO
 * @Date 2022/9/28 15:35
 */
@Service
public class Msgservimpl extends ServiceImpl<MessageMapper, Message> implements MSGService, ActivateState {
    @Autowired
    MessageMapper messageMapper;

    /**
     * 用户会话个数
     * @param id
     * @return
     */
    @Override
    public int msgcount(Integer id) {
        return messageMapper.msgcount(id);
    }

    /**
     * 分页查询messagelist
     * @param id
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<Message> findmsglist_page(Integer id, int offset, int limit) {
        return messageMapper.findlist(id,offset,limit);
    }

    /**
     * 每条会话未读数
     * @param conversationId
     * @return
     */
    @Override
    public int getunreadcount(String conversationId) {

        return messageMapper.unread(conversationId);
    }

    /**
     * 每个会话多少条对话
     * @param conversationId
     * @return
     */
    @Override
    public int eachcount(String conversationId) {

        return messageMapper.eachcount(conversationId);
    }

    /**
     * 获取所有该会话的对话
     * @param conversationId
     * @return
     */
    @Override
    public List<Message> selectallby_convid(String conversationId) {

        return messageMapper.getallbyconvid(conversationId);
    }

    @Override
    public List<Message> selectallby_convid_fenye(String conversationId, int limit, int offset) {
        return messageMapper.getallbyconvid_fenye(conversationId,limit,offset);
    }

    /**
     * 三类通知一共未读的数量
     * @return
     */
    @Override
    public Integer unreadall_3() {
        LambdaQueryWrapper<Message> mqwrapper = new LambdaQueryWrapper<>();
//        系统消息 且未读
        mqwrapper.eq(Message::getFromId,1);
        mqwrapper.eq(Message::getStatus,0);
        long count = count(mqwrapper);
        return Integer.parseInt(String.valueOf(count));
    }

    /**
     * 最新的那条系统message
     * 评论的
     * @return
     */
    @Override
    public Message findlatest_comment() {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<Message>();
        queryWrapper.eq(Message::getFromId,1);
        queryWrapper.eq(Message::getConversationId,TOPIC_COMMENT);
        queryWrapper.orderByDesc(Message::getId);
        List<Message> messageList = messageMapper.selectList(queryWrapper);
        return messageList.get(0);
    }
    /**
     * 最新的那条系统message
     * 点赞的
     * @return
     */
    @Override
    public Message findlatest_like() {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<Message>();
        queryWrapper.eq(Message::getFromId,1);
        queryWrapper.eq(Message::getConversationId,TOPIC_LIKE);
        queryWrapper.orderByDesc(Message::getId);
        List<Message> messageList = messageMapper.selectList(queryWrapper);
        return messageList.get(0);
    }

    /**
     * 评论未读
     * @return
     */
    @Override
    public Integer count_commentunread() {
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        messageLambdaQueryWrapper.eq(Message::getFromId,1)
                .eq(Message::getConversationId,TOPIC_COMMENT)
                .eq(Message::getStatus,0);
        long count = count(messageLambdaQueryWrapper);
        return Integer.parseInt(String.valueOf(count));
    }

    /**
     * 评论总数
     * @return
     */
    @Override
    public Integer count_commentall() {
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        messageLambdaQueryWrapper.eq(Message::getFromId,1)
                .eq(Message::getConversationId,TOPIC_COMMENT);
        long count = count(messageLambdaQueryWrapper);
        return Integer.parseInt(String.valueOf(count));
    }

    /**
     * 点赞未读
     * @return
     */
    @Override
    public Integer count_likeunread() {
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        messageLambdaQueryWrapper.eq(Message::getFromId,1)
                .eq(Message::getConversationId,TOPIC_LIKE)
                .eq(Message::getStatus,0);
        long count = count(messageLambdaQueryWrapper);
        return Integer.parseInt(String.valueOf(count));
    }

    /**
     * 点赞总数
     * @return
     */
    @Override
    public Integer count_likeall() {
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        messageLambdaQueryWrapper.eq(Message::getFromId,1)
                .eq(Message::getConversationId,TOPIC_LIKE);
        long count = count(messageLambdaQueryWrapper);
        return Integer.parseInt(String.valueOf(count));
    }

    @Override
    public Message findlatest_fl() {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<Message>();
        queryWrapper.eq(Message::getFromId,1);
        queryWrapper.eq(Message::getConversationId,TOPIC_FOLLOW);
        queryWrapper.orderByDesc(Message::getId);
        List<Message> messageList = messageMapper.selectList(queryWrapper);
        return messageList.get(0);
    }

    @Override
    public Integer count_fl_unread() {
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        messageLambdaQueryWrapper.eq(Message::getFromId,1)
                .eq(Message::getConversationId,TOPIC_FOLLOW)
                .eq(Message::getStatus,0);
        long count = count(messageLambdaQueryWrapper);
        return Integer.parseInt(String.valueOf(count));
    }

    @Override
    public Integer count_fl_all() {
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        messageLambdaQueryWrapper.eq(Message::getFromId,1)
                .eq(Message::getConversationId,TOPIC_FOLLOW);
        long count = count(messageLambdaQueryWrapper);
        return Integer.parseInt(String.valueOf(count));
    }


}
