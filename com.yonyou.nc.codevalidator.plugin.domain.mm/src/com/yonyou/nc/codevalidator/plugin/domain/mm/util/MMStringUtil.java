package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * String类型工具类
 * 
 * @since 6.3
 * @version 2012-8-31 上午11:32:23
 * @author lixdh
 */
public class MMStringUtil {

    /**
     * 判断字符串是否为空
     * 
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return null == s || s.trim().length() <= 0;
    }

    /**
     * 判断字符串是否为非空
     * 
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return null != s && s.trim().length() > 0;
    }

    /**
     * 判断字符串数组是否为空
     * 
     * @param sArray
     * @return
     */
    public static boolean isEmpty(String[] sArray) {
        if (MMArrayUtil.isEmpty(sArray)) {
            return true;
        }
        for (String s : sArray) {
            if (MMStringUtil.isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两个字符串是否相等
     * 
     * @param s1
     * @param s2
     * @return
     */
    public static boolean isEqual(String s1, String s2) {
        String s11 = null == s1 ? String.valueOf("") : s1;
        String s22 = null == s2 ? String.valueOf("") : s2;
        return s11.trim().equals(s22.trim());
    }

    /**
     * 判断字符串数组是否包含给定字符串
     * 
     * @param sArray
     * @param s
     * @return
     */
    public static boolean isContain(String[] sArray, String s) {
        if (MMArrayUtil.isEmpty(sArray)) {
            return null == s;
        }
        for (String temp : sArray) {
            if (MMStringUtil.isEqual(temp, s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将数组合并成字符串
     * 
     * @param objs
     * @param nulltoken
     * @param splittoken
     * @return
     */
    public static String mergeString(Object[] objs, String nulltoken, String splittoken) {
        if (MMArrayUtil.isEmpty(objs)) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        String nvl = null == nulltoken ? "null" : nulltoken;
        String svalue = null;
        for (int i = 0; i < objs.length; i++) {
            if (i > 0 && null != splittoken) {
                sb.append(splittoken);
            }
            svalue = objs[i] == null ? null : objs[i].toString();
            if (MMStringUtil.isNotEmpty(svalue)) {
                sb.append(svalue);
            }
            else {
                sb.append(nvl);
            }
        }
        return sb.toString();
    }

    /**
     * 合并字符串
     * 
     * @param obj1
     * @param obj2
     * @return
     */
    public static String mergeString(Object obj1, Object obj2) {
        Object[] objs = new Object[] {
            obj1, obj2
        };
        return MMStringUtil.mergeString(objs, null, null);
    }

    /**
     * 取字符串非空值
     * 
     * @param s
     * @return
     */
    public static String toNotNullValue(String s) {
        return null == s ? "" : s.trim();
    }

    /**
     * 字符串数组去除空元素
     * 
     * @param sArray
     * @return
     */
    public static String[] removeNull(String[] sArray) {
        if (MMArrayUtil.isEmpty(sArray)) {
            return null;
        }
        List<String> retList = new ArrayList<String>();
        for (String s : sArray) {
            if (MMStringUtil.isEmpty(s)) {
                continue;
            }
            retList.add(s);
        }
        return retList.toArray(new String[0]);
    }

    /**
     * 字符串数组去除给定字符串
     * 
     * @param sArray
     * @param removeS
     * @return
     */
    public static String[] removeString(String[] sArray, String removeS) {
        if (MMArrayUtil.isEmpty(sArray)) {
            return null;
        }
        List<String> retList = new ArrayList<String>();
        for (String s : sArray) {
            if (!MMStringUtil.isEqual(s, removeS)) {
                retList.add(s);
            }
        }
        return retList.toArray(new String[0]);
    }

    /**
     * 字符串格式化
     * 
     * @param s
     * @param objs
     * @return
     */
    public static String format(String s, Object... objs) {
        return String.format(s, objs);
    }
}
