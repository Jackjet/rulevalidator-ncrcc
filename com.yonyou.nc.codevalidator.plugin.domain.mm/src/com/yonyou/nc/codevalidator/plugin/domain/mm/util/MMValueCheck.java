package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 值校验工具类
 * 
 * @since 6.3
 * @version 2012-8-31 上午08:48:18
 * @author lixdh
 * @author wanghjd
 * @version 2013-07-12 下午13:53:13
 * @author zhr
 */
public class MMValueCheck {

    /**
     * 判断Object是否为空
     * 
     * @param obj
     * @return
     */
    public static final boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof String) {
            return MMStringUtil.isEmpty((String) obj);
        }

        if (obj.getClass().isArray()) {
            return MMArrayUtil.isEmpty((Object[]) obj);
        }
        if (obj instanceof Collection) {
            return MMCollectionUtil.isEmpty((Collection<?>) obj);
        }
        if (obj instanceof Map) {
            return MMMapUtil.isEmpty((Map<?, ?>) obj);
        }
        if (obj instanceof MapList) {
            return MMMapUtil.isEmpty((MapList<?, ?>) obj);
        }
        if (obj instanceof StringBuilder) {
            return obj.toString().trim().length() <= 0;
        }
        if (obj instanceof StringBuffer) {
            return obj.toString().trim().length() <= 0;
        }
        return false;
    }

    /**
     * 判断Object[]是否为空
     * 
     * @param objs
     * @return
     */
    public static final boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }

    /**
     * 判断Character是否为空
     * 
     * @param packCharacter
     * @return
     */
    public static final boolean isEmpty(Character packCharacter) {
        return null == packCharacter;
    }

    /**
     * 判断Byte是否为空
     * 
     * @param packByte
     * @return
     */
    public static final boolean isEmpty(Byte packByte) {
        return null == packByte;
    }

    /**
     * 判断Boolean是否为空
     * 
     * @param packBoolean
     * @return
     */
    public static final boolean isEmpty(Boolean packBoolean) {
        return null == packBoolean;
    }

    /**
     * 判断Short是否为空
     * 
     * @param packShort
     * @return
     */
    public static final boolean isEmpty(Short packShort) {
        return null == packShort;
    }

    /**
     * 判断Integer是否为空
     * 
     * @param packInteger
     * @return
     */
    public static final boolean isEmpty(Integer packInteger) {
        return null == packInteger;
    }

    /**
     * 判断Long是否为空
     * 
     * @param packLong
     * @return
     */
    public static final boolean isEmpty(Long packLong) {
        return null == packLong;
    }

    /**
     * 判断Float是否为空
     * 
     * @param packFloat
     * @return
     */
    public static final boolean isEmpty(Float packFloat) {
        return null == packFloat;
    }

    /**
     * 判断Double是否为空
     * 
     * @param packDouble
     * @return
     */
    public static final boolean isEmpty(Double packDouble) {
        return null == packDouble;
    }

    /**
     * 判断Collection是否为空
     * 
     * @param collection
     * @return
     */
    public static final boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.size() <= 0;
    }

    /**
     * 判断Map是否为空
     * 
     * @param map
     * @return
     */
    public static final boolean isEmpty(Map<?, ?> map) {
        return null == map || map.size() <= 0;
    }

    /**
     * 判断MapList是否为空
     * 
     * @param mapList
     * @return
     */
    public static final boolean isEmpty(MapList<?, ?> mapList) {
        return null == mapList || mapList.size() <= 0;
    }

    /**
     * 判断Set是否为空
     * 
     * @param set
     * @return
     */
    public static final boolean isEmpty(Set<?> set) {
        return null == set || set.size() <= 0;
    }

    /**
     * 判断String是否为空
     * 
     * @param str
     * @return
     */
    public static final boolean isEmpty(String str) {
        return null == str || str.trim().length() <= 0;
    }

    /**
     * 判断StringBuilder是否为空
     * 
     * @param strBuilder
     * @return
     */
    public static final boolean isEmpty(StringBuilder strBuilder) {
        return null == strBuilder || strBuilder.toString().trim().length() <= 0;
    }

    /**
     * 判断StringBuffer是否为空
     * 
     * @param strBuffer
     * @return
     */
    public static final boolean isEmpty(StringBuffer strBuffer) {
        return null == strBuffer || strBuffer.toString().trim().length() == 0;
    }

  

    /**
     * 判断Object是否为非空
     * 
     * @param obj
     * @return
     */
    public static final boolean isNotEmpty(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj instanceof String) {
            return MMStringUtil.isNotEmpty((String) obj);
        }

        if (obj.getClass().isArray()) {
            return MMArrayUtil.isNotEmpty((Object[]) obj);
        }
        if (obj instanceof Collection) {
            return MMCollectionUtil.isNotEmpty((Collection<?>) obj);
        }
        if (obj instanceof Map) {
            return MMMapUtil.isNotEmpty((Map<?, ?>) obj);
        }
        if (obj instanceof MapList) {
            return MMMapUtil.isNotEmpty((MapList<?, ?>) obj);
        }
        if (obj instanceof StringBuilder) {
            return obj.toString().trim().length() > 0;
        }
        if (obj instanceof StringBuffer) {
            return obj.toString().trim().length() > 0;
        }
        return true;
    }

    /**
     * 判断Object[]是否为非空
     * 
     * @param objs
     * @return
     */
    public static final boolean isNotEmpty(Object[] objs) {
        return null != objs && objs.length > 0;
    }

    /**
     * 判断Character是否为非空
     * 
     * @param packCharacter
     * @return
     */
    public static final boolean isNotEmpty(Character packCharacter) {
        return null != packCharacter;
    }

    /**
     * 判断Byte是否为非空
     * 
     * @param packByte
     * @return
     */
    public static final boolean isNotEmpty(Byte packByte) {
        return null != packByte;
    }

    /**
     * 判断Boolean是否为非空
     * 
     * @param packBoolean
     * @return
     */
    public static final boolean isNotEmpty(Boolean packBoolean) {
        return null != packBoolean;
    }

    /**
     * 判断Short是否为非空
     * 
     * @param packShort
     * @return
     */
    public static final boolean isNotEmpty(Short packShort) {
        return null != packShort;
    }

    /**
     * 判断Integer是否为非空
     * 
     * @param packInteger
     * @return
     */
    public static final boolean isNotEmpty(Integer packInteger) {
        return null != packInteger;
    }

    /**
     * 判断Long是否为非空
     * 
     * @param packLong
     * @return
     */
    public static final boolean isNotEmpty(Long packLong) {
        return null != packLong;
    }

    /**
     * 判断Float是否为非空
     * 
     * @param packFloat
     * @return
     */
    public static final boolean isNotEmpty(Float packFloat) {
        return null != packFloat;
    }

    /**
     * 判断Double是否为非空
     * 
     * @param packDouble
     * @return
     */
    public static final boolean isNotEmpty(Double packDouble) {
        return null != packDouble;
    }

    /**
     * 判断Collection是否为非空
     * 
     * @param collection
     * @return
     */
    public static final boolean isNotEmpty(Collection<?> collection) {
        return null != collection && collection.size() > 0;
    }

    /**
     * 判断Map是否为非空
     * 
     * @param map
     * @return
     */
    public static final boolean isNotEmpty(Map<?, ?> map) {
        return null != map && map.size() > 0;
    }

    /**
     * 判断MapList是否为非空
     * 
     * @param mapList
     * @return
     */
    public static final boolean isNotEmpty(MapList<?, ?> mapList) {
        return null != mapList && mapList.size() > 0;
    }

    /**
     * 判断Set是否为非空
     * 
     * @param set
     * @return
     */
    public static final boolean isNotEmpty(Set<?> set) {
        return null != set && set.size() > 0;
    }

    /**
     * 判断String是否为非空
     * 
     * @param str
     * @return
     */
    public static final boolean isNotEmpty(String str) {
        return null != str && str.trim().length() > 0;
    }

    /**
     * 判断StringBuilder是否为非空
     * 
     * @param strBuilder
     * @return
     */
    public static final boolean isNotEmpty(StringBuilder strBuilder) {
        return null != strBuilder && strBuilder.toString().trim().length() > 0;
    }

    /**
     * 判断StringBuffer是否为非空
     * 
     * @param strBuffer
     * @return
     */
    public static final boolean isNotEmpty(StringBuffer strBuffer) {
        return null != strBuffer && strBuffer.toString().trim().length() > 0;
    }

  
    /**
     * 判断是否为真
     * 
     * @param b
     * @return
     */
    public static boolean isTrue(Boolean b) {
        return null == b ? false : b.booleanValue();
    }

    /**
     * 判断是否为数字或者字母
     * 
     * @param s
     * @return
     */
    public static boolean isNumberOrLetter(String s) {
        java.util.regex.Pattern patten = java.util.regex.Pattern.compile("[0-9A-Za-z]*");
        return patten.matcher(s).matches();
    }

    /**
     * 判断是否为数字
     * 
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        java.util.regex.Pattern patten = java.util.regex.Pattern.compile("[0-9]*");
        return patten.matcher(s).matches();
    }
}
