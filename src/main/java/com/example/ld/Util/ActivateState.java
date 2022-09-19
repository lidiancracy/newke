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
}
