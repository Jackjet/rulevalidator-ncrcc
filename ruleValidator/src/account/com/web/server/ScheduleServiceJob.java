package com.web.server;

/**
 * 调度任务服务
 * 
 * @since 6.3
 * @version 2013-12-23 下午01:49:28
 * @author sunshine
 */
public class ScheduleServiceJob {
    /**
     * 清除所有session
     */
    public void removeAllSession() {
        SingleOnline.purge();
    }
}
