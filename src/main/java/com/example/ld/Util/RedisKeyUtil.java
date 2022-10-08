package com.example.ld.Util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_USER_follow = "follow";
    private static final String PREFIX_USER_followee = "followee";

    private static final String PREFIX_KAPTCHA = "kaptCha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";

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
    // 登录要验证码
    public static String getKapChaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录凭证
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }
}
