package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.util.Map;

/**
 * Map类型工具类
 * 
 * @since 6.3
 * @version 2012-8-31 上午08:59:41
 * @author lixdh
 */
public class MMMapUtil {

    /**
     * 判断Map是否为空
     * 
     * @param m
     * @return
     */
    public static boolean isEmpty(Map<?, ?> m) {
        return null == m || m.size() == 0;
    }

    /**
     * 判断Map是否为非空
     * 
     * @param m
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> m) {
        return null != m && m.size() > 0;
    }

    /**
     * 将键值数组附加到Map
     * 
     * @param <K>
     * @param <V>
     * @param m
     * @param keys
     * @param values
     */
    public static <K, V> void addArrayToMap(Map<K, V> m, K[] keys, V[] values) {
        if (null == m || MMArrayUtil.isEmpty(keys) || MMArrayUtil.isEmpty(values)
                || !MMArrayUtil.isLengthEqual(keys, values)) {
            return;
        }
        for (int i = 0; i < keys.length; i++) {
            m.put(keys[i], values[i]);
        }
    }

    /**
     * 判断Map是否为空
     * 
     * @param m
     * @return
     */
    public static boolean isEmpty(MapList<?, ?> m) {
        return null == m || m.size() == 0;
    }

    /**
     * 判断Map是否为非空
     * 
     * @param m
     * @return
     */
    public static boolean isNotEmpty(MapList<?, ?> m) {
        return null != m && m.size() > 0;
    }

}
