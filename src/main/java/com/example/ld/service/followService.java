package com.example.ld.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    List<HashMap<String, Object>> findall(int userid, int offset, int limit);

    List<HashMap<String, Object>> findallfans(int userid, int offset, int limit);
}
