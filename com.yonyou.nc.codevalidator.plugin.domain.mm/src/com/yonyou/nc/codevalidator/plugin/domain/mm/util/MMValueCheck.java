package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * ֵУ�鹤����
 * 
 * @since 6.3
 * @version 2012-8-31 ����08:48:18
 * @author lixdh
 * @author wanghjd
 * @version 2013-07-12 ����13:53:13
 * @author zhr
 */
public class MMValueCheck {

    /**
     * �ж�Object�Ƿ�Ϊ��
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
     * �ж�Object[]�Ƿ�Ϊ��
     * 
     * @param objs
     * @return
     */
    public static final boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }

    /**
     * �ж�Character�Ƿ�Ϊ��
     * 
     * @param packCharacter
     * @return
     */
    public static final boolean isEmpty(Character packCharacter) {
        return null == packCharacter;
    }

    /**
     * �ж�Byte�Ƿ�Ϊ��
     * 
     * @param packByte
     * @return
     */
    public static final boolean isEmpty(Byte packByte) {
        return null == packByte;
    }

    /**
     * �ж�Boolean�Ƿ�Ϊ��
     * 
     * @param packBoolean
     * @return
     */
    public static final boolean isEmpty(Boolean packBoolean) {
        return null == packBoolean;
    }

    /**
     * �ж�Short�Ƿ�Ϊ��
     * 
     * @param packShort
     * @return
     */
    public static final boolean isEmpty(Short packShort) {
        return null == packShort;
    }

    /**
     * �ж�Integer�Ƿ�Ϊ��
     * 
     * @param packInteger
     * @return
     */
    public static final boolean isEmpty(Integer packInteger) {
        return null == packInteger;
    }

    /**
     * �ж�Long�Ƿ�Ϊ��
     * 
     * @param packLong
     * @return
     */
    public static final boolean isEmpty(Long packLong) {
        return null == packLong;
    }

    /**
     * �ж�Float�Ƿ�Ϊ��
     * 
     * @param packFloat
     * @return
     */
    public static final boolean isEmpty(Float packFloat) {
        return null == packFloat;
    }

    /**
     * �ж�Double�Ƿ�Ϊ��
     * 
     * @param packDouble
     * @return
     */
    public static final boolean isEmpty(Double packDouble) {
        return null == packDouble;
    }

    /**
     * �ж�Collection�Ƿ�Ϊ��
     * 
     * @param collection
     * @return
     */
    public static final boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.size() <= 0;
    }

    /**
     * �ж�Map�Ƿ�Ϊ��
     * 
     * @param map
     * @return
     */
    public static final boolean isEmpty(Map<?, ?> map) {
        return null == map || map.size() <= 0;
    }

    /**
     * �ж�MapList�Ƿ�Ϊ��
     * 
     * @param mapList
     * @return
     */
    public static final boolean isEmpty(MapList<?, ?> mapList) {
        return null == mapList || mapList.size() <= 0;
    }

    /**
     * �ж�Set�Ƿ�Ϊ��
     * 
     * @param set
     * @return
     */
    public static final boolean isEmpty(Set<?> set) {
        return null == set || set.size() <= 0;
    }

    /**
     * �ж�String�Ƿ�Ϊ��
     * 
     * @param str
     * @return
     */
    public static final boolean isEmpty(String str) {
        return null == str || str.trim().length() <= 0;
    }

    /**
     * �ж�StringBuilder�Ƿ�Ϊ��
     * 
     * @param strBuilder
     * @return
     */
    public static final boolean isEmpty(StringBuilder strBuilder) {
        return null == strBuilder || strBuilder.toString().trim().length() <= 0;
    }

    /**
     * �ж�StringBuffer�Ƿ�Ϊ��
     * 
     * @param strBuffer
     * @return
     */
    public static final boolean isEmpty(StringBuffer strBuffer) {
        return null == strBuffer || strBuffer.toString().trim().length() == 0;
    }

  

    /**
     * �ж�Object�Ƿ�Ϊ�ǿ�
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
     * �ж�Object[]�Ƿ�Ϊ�ǿ�
     * 
     * @param objs
     * @return
     */
    public static final boolean isNotEmpty(Object[] objs) {
        return null != objs && objs.length > 0;
    }

    /**
     * �ж�Character�Ƿ�Ϊ�ǿ�
     * 
     * @param packCharacter
     * @return
     */
    public static final boolean isNotEmpty(Character packCharacter) {
        return null != packCharacter;
    }

    /**
     * �ж�Byte�Ƿ�Ϊ�ǿ�
     * 
     * @param packByte
     * @return
     */
    public static final boolean isNotEmpty(Byte packByte) {
        return null != packByte;
    }

    /**
     * �ж�Boolean�Ƿ�Ϊ�ǿ�
     * 
     * @param packBoolean
     * @return
     */
    public static final boolean isNotEmpty(Boolean packBoolean) {
        return null != packBoolean;
    }

    /**
     * �ж�Short�Ƿ�Ϊ�ǿ�
     * 
     * @param packShort
     * @return
     */
    public static final boolean isNotEmpty(Short packShort) {
        return null != packShort;
    }

    /**
     * �ж�Integer�Ƿ�Ϊ�ǿ�
     * 
     * @param packInteger
     * @return
     */
    public static final boolean isNotEmpty(Integer packInteger) {
        return null != packInteger;
    }

    /**
     * �ж�Long�Ƿ�Ϊ�ǿ�
     * 
     * @param packLong
     * @return
     */
    public static final boolean isNotEmpty(Long packLong) {
        return null != packLong;
    }

    /**
     * �ж�Float�Ƿ�Ϊ�ǿ�
     * 
     * @param packFloat
     * @return
     */
    public static final boolean isNotEmpty(Float packFloat) {
        return null != packFloat;
    }

    /**
     * �ж�Double�Ƿ�Ϊ�ǿ�
     * 
     * @param packDouble
     * @return
     */
    public static final boolean isNotEmpty(Double packDouble) {
        return null != packDouble;
    }

    /**
     * �ж�Collection�Ƿ�Ϊ�ǿ�
     * 
     * @param collection
     * @return
     */
    public static final boolean isNotEmpty(Collection<?> collection) {
        return null != collection && collection.size() > 0;
    }

    /**
     * �ж�Map�Ƿ�Ϊ�ǿ�
     * 
     * @param map
     * @return
     */
    public static final boolean isNotEmpty(Map<?, ?> map) {
        return null != map && map.size() > 0;
    }

    /**
     * �ж�MapList�Ƿ�Ϊ�ǿ�
     * 
     * @param mapList
     * @return
     */
    public static final boolean isNotEmpty(MapList<?, ?> mapList) {
        return null != mapList && mapList.size() > 0;
    }

    /**
     * �ж�Set�Ƿ�Ϊ�ǿ�
     * 
     * @param set
     * @return
     */
    public static final boolean isNotEmpty(Set<?> set) {
        return null != set && set.size() > 0;
    }

    /**
     * �ж�String�Ƿ�Ϊ�ǿ�
     * 
     * @param str
     * @return
     */
    public static final boolean isNotEmpty(String str) {
        return null != str && str.trim().length() > 0;
    }

    /**
     * �ж�StringBuilder�Ƿ�Ϊ�ǿ�
     * 
     * @param strBuilder
     * @return
     */
    public static final boolean isNotEmpty(StringBuilder strBuilder) {
        return null != strBuilder && strBuilder.toString().trim().length() > 0;
    }

    /**
     * �ж�StringBuffer�Ƿ�Ϊ�ǿ�
     * 
     * @param strBuffer
     * @return
     */
    public static final boolean isNotEmpty(StringBuffer strBuffer) {
        return null != strBuffer && strBuffer.toString().trim().length() > 0;
    }

  
    /**
     * �ж��Ƿ�Ϊ��
     * 
     * @param b
     * @return
     */
    public static boolean isTrue(Boolean b) {
        return null == b ? false : b.booleanValue();
    }

    /**
     * �ж��Ƿ�Ϊ���ֻ�����ĸ
     * 
     * @param s
     * @return
     */
    public static boolean isNumberOrLetter(String s) {
        java.util.regex.Pattern patten = java.util.regex.Pattern.compile("[0-9A-Za-z]*");
        return patten.matcher(s).matches();
    }

    /**
     * �ж��Ƿ�Ϊ����
     * 
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        java.util.regex.Pattern patten = java.util.regex.Pattern.compile("[0-9]*");
        return patten.matcher(s).matches();
    }
}
