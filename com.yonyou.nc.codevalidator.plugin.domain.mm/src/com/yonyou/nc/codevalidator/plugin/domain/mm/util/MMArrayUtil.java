package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Array���͹�����
 * 
 * @since 6.3
 * @version 2012-8-31 ����08:42:53
 * @author lixdh
 */
public class MMArrayUtil {

    /**
     * �ж������Ƿ�Ϊ��
     * 
     * @param objs
     * @return
     */
    public static boolean isEmpty(Object[] objs) {
        return null == objs || objs.length == 0;
    }

    /**
     * �ж������Ƿ�Ϊ�ǿ�
     * 
     * @param objs
     * @return
     */
    public static boolean isNotEmpty(Object[] objs) {
        return null != objs && objs.length > 0;
    }

    /**
     * �ж������Ƿ������Ԫ��
     * 
     * @param objs
     * @return
     */
    public static boolean isContainNull(Object[] objs) {
        if (MMArrayUtil.isEmpty(objs)) {
            return true;
        }
        for (Object obj : objs) {
            if (null == obj) {
                return true;
            }
        }
        return false;
    }

    /**
     * �ж������Ƿ񲻰�����Ԫ��
     * 
     * @param objs
     * @return
     */
    public static boolean isNotContainNull(Object[] objs) {
        if (MMArrayUtil.isEmpty(objs)) {
            return false;
        }
        for (Object obj : objs) {
            if (null == obj) {
                return false;
            }
        }
        return true;
    }

    /**
     * �ж��������鳤���Ƿ����
     * 
     * @param objs1
     * @param objs2
     * @return
     */
    public static boolean isLengthEqual(Object[] objs1, Object[] objs2) {

        if (MMArrayUtil.isEmpty(objs1) && MMArrayUtil.isEmpty(objs2)) {
            return true;
        }
        else if (MMArrayUtil.isNotEmpty(objs1) && MMArrayUtil.isNotEmpty(objs2) && objs1.length == objs2.length) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * �ϲ��������
     * 
     * @param <T>
     * @param objs
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] combineArray(T[]... objs) {
        if (null == objs) {
            return null;
        }
        int length = 0;
        int count = 0;
        T[] result = null;
        for (T[] array : objs) {
            if (MMArrayUtil.isEmpty(array)) {
                continue;
            }
            if (null == result) {
                result = array;
            }
            count++;
            length += array.length;
        }
        if (length == 0 || count == 1 || null == result) {
            return result;
        }
        result = (T[]) Array.newInstance(result[0].getClass(), length);
        int destPos = 0;
        for (Object[] array : objs) {
            if (MMArrayUtil.isEmpty(array)) {
                continue;
            }
            System.arraycopy(array, 0, result, destPos, array.length);
            destPos += array.length;
        }
        return result;
    }

    /**
     * ȥ�������еĿ�Ԫ��
     * 
     * @param <T>
     * @param objs
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] removeNull(T[] objs, Class<T> clazz) {
        if (MMArrayUtil.isEmpty(objs)) {
            return null;
        }
        List<T> tempList = new ArrayList<T>();
        for (T temp : tempList) {
            if (null == temp) {
                continue;
            }
            tempList.add(temp);
        }
        T[] result = (T[]) Array.newInstance(clazz, tempList.size());
        tempList.toArray(result);
        return result;
    }

    /**
     * Objectת��������T[]
     * 
     * @param <T>
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(T obj) {
        T[] array = null;
        if (obj.getClass().isArray()) {
            Object[] objs = (Object[]) obj;
            array = (T[]) Array.newInstance(objs[0].getClass(), objs.length);
            for (int i = 0; i < objs.length; i++) {
                array[i] = (T) objs[i];
            }
        }
        else {
            array = (T[]) Array.newInstance(obj.getClass(), 1);
            array[0] = obj;
        }
        return array;
    }

    /**
     * Object[]ת��������T[]
     * 
     * @param <T>
     * @param objs
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Object[] objs) {
        if (MMArrayUtil.isEmpty(objs)) {
            return null;
        }
        T[] result = (T[]) Array.newInstance(objs[0].getClass(), objs.length);
        if (result.getClass().isAssignableFrom(objs.getClass())) {
            return (T[]) objs;
        }
        System.arraycopy(objs, 0, result, 0, objs.length);
        return result;
    }

    /**
     * Collectionת��������T[]
     * 
     * @param <T>
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> c) {
        if (MMCollectionUtil.isEmpty(c)) {
            return null;
        }
        Iterator<T> iter = c.iterator();
        if (!iter.hasNext()) {
            return null;
        }
        T t = iter.next();
        T[] result = (T[]) Array.newInstance(t.getClass(), c.size());
        return c.toArray(result);
    }

    /**
     * Collectionת��������T[]
     * 
     * @param <T>
     * @param c
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> c, Class<T> clazz) {
        if (MMCollectionUtil.isEmpty(c)) {
            return null;
        }
        T[] result = (T[]) Array.newInstance(clazz, c.size());
        return c.toArray(result);
    }

    /**
     * ��������������������ת��Ϊ���飬�贫��Ŀ��������ͣ���������������򷵻�size=1������
     * 
     * @param <T> ���Ͳ��������������������
     * @param dataClass ���뷺��T�����Ͳ����������������������
     * @param obj ��ת��Ϊ�����Object�Ͷ������
     * @return ת����ķ�������
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Class<T> dataClass, Object obj) {
        T[] array = null;
        if (obj.getClass().isArray()) {
            Object[] objs = (Object[]) obj;
            array = (T[]) Array.newInstance(dataClass, objs.length);
            for (int i = 0; i < objs.length; i++) {
                array[i] = (T) objs[i];
            }
        }
        else {
            array = (T[]) Array.newInstance(obj.getClass(), 1);
            array[0] = (T) obj;
        }
        return array;
    }
}
