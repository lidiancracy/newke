package com.example.ld.Util;

/**
 * @ClassName ActivateState
 * @Description TODO
 * @Date 2022/9/19 13:19
 */
public interface ActivateState {
    /**
     * 未激活0
     * 激活1
     * 重复激活2
     */
    public static final int UNACTIVATED = 0;
    public static final int ACTIVATED = 1;
    public static final int REPEATACTIVATE = 2;
    /**
     * 记住我 状态下的凭证存储时间
     * 默认凭证存储时间
     */
    public static final int default_exptime = 3600*24;
    public static final int remenber_exptime = 3600*24*100;
    /**
     * entity_type为0 表示在帖子下面回复
     * entity_type为1 表示在帖子下面的用户回复下回复
     */
    public static final int YIJI_TITLE = 0;
    public static final int ERJI_TITLE = 1;
    /**
     * 1表示私信已读
     * 0表示私信未读
     */
    public static final int MSG_YIDU = 1;
    public static final int MSG_WD = 0;
}
