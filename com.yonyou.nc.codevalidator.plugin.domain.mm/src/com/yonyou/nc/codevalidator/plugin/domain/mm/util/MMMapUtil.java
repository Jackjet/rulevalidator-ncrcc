package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.util.Map;

/**
 * Map���͹�����
 * 
 * @since 6.3
 * @version 2012-8-31 ����08:59:41
 * @author lixdh
 */
public class MMMapUtil {

    /**
     * �ж�Map�Ƿ�Ϊ��
     * 
     * @param m
     * @return
     */
    public static boolean isEmpty(Map<?, ?> m) {
        return null == m || m.size() == 0;
    }

    /**
     * �ж�Map�Ƿ�Ϊ�ǿ�
     * 
     * @param m
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> m) {
        return null != m && m.size() > 0;
    }

    /**
     * ����ֵ���鸽�ӵ�Map
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
     * �ж�Map�Ƿ�Ϊ��
     * 
     * @param m
     * @return
     */
    public static boolean isEmpty(MapList<?, ?> m) {
        return null == m || m.size() == 0;
    }

    /**
     * �ж�Map�Ƿ�Ϊ�ǿ�
     * 
     * @param m
     * @return
     */
    public static boolean isNotEmpty(MapList<?, ?> m) {
        return null != m && m.size() > 0;
    }

}
