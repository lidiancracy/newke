package com.example.ld.Util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_USER_follow = "follow";
    private static final String PREFIX_USER_followee = "followee";

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞
//     like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + "userlikecount" + SPLIT + userId;
    }

    //    关注列表 redis里面记录关注了上面以及啥时候关注的（score）
    public static String getfollow(int entitytype, int userId) {
        return PREFIX_USER_follow + SPLIT + userId + SPLIT + entitytype;
    }

    //  粉丝列表  redis里面记录谁关注了上面以及啥时候关注我的（score）
    public static String getfollowee(int entitytype, int userId) {
        return PREFIX_USER_followee + SPLIT + userId + SPLIT + entitytype;
    }

}
