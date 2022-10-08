package com.example.ld.service;

/**
 * @ClassName followService
 * @Description TODO
 * @Date 2022/10/8 15:46
 */
public interface followService {

    /**
     *
     */
    void followsomeone(Integer userid, int entityType, int entityId);

    void unfollowsomeone(Integer userid, int entityType, int entityId);

    boolean followornot(int userid, int i, Integer loginid);

    long followcount(int userid, int i);

    long fans(int userid, int i);
}
