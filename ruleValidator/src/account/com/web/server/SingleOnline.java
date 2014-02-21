package com.web.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunshine
 * 
 */
public class SingleOnline {

    /**
     * 是否强制登录
     */
    public static final String ENFORCE = "enforce";

    private static Map<String, String> mapOnline = new ConcurrentHashMap<String, String>();

    /**
     * 将用户添加到在线列表
     * 
     * @param userCode
     * @param sessionId
     */
    public static void addUser(String userName, String sessionId) {
        mapOnline.put(userName, sessionId);
    }

    /**
     * 从在线列表删除用户
     * 
     * @param userName
     */
    public static void removeUser(String userName) {
        mapOnline.remove(userName);
    }

    /**
     * 用户是否在线
     * 
     * @param userCode
     * @param sessionId
     * @return
     */
    public static boolean isOnline(String userName) {
        if (mapOnline.containsKey(userName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * session是否有效
     * 
     * @param userName
     * @param sessionId
     * @return
     */
    public static boolean isSessionValid(String sessionId) {
        boolean flag = false;
        if (mapOnline.containsValue(sessionId)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 清除所有session
     */
    public static void purge() {
        mapOnline.clear();
    }
}
