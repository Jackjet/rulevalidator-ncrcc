package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.util.Collection;

/**
 * Collection���͹�����
 * 
 * @since 6.3
 * @version 2012-8-31 ����09:02:47
 * @author lixdh
 */
public class MMCollectionUtil {

    /**
     * �жϼ����Ƿ�Ϊ��
     * 
     * @param c
     * @return
     */
    public static boolean isEmpty(Collection<?> c) {
        return null == c || c.size() == 0;
    }

    /**
     * �жϼ����Ƿ�Ϊ�ǿ�
     * 
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Collection<?> c) {
        return null != c && c.size() > 0;
    }

    /**
     * �жϼ����Ƿ������Ԫ��
     * 
     * @param c
     * @return
     */
    public static boolean isContainNull(Collection<?> c) {
        if (MMCollectionUtil.isEmpty(c)) {
            return true;
        }
        for (Object obj : c) {
            if (null == obj) {
                return true;
            }
        }
        return false;
    }

    /**
     * �жϼ����Ƿ񲻰�����Ԫ��
     * 
     * @param c
     * @return
     */
    public static boolean isNotContainNull(Collection<?> c) {
        if (MMCollectionUtil.isEmpty(c)) {
            return false;
        }
        for (Object obj : c) {
            if (null == obj) {
                return false;
            }
        }
        return true;
    }

    /**
     * �����鸽�ӵ�����
     * 
     * @param <T>
     * @param c
     * @param values
     */
    public static <T> void addArrayToCollection(Collection<T> c, T[] values) {
        if (c == null || MMArrayUtil.isEmpty(values)) {
            return;
        }
        for (T value : values) {
            c.add(value);
        }
    }
}
