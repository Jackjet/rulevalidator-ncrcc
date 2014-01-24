package com.yonyou.nc.codevalidator.sdk.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ���鴦�����ع����ֻ࣬Ҫ�Ǵ�NC�ʲ����Ż��
 * @author mazhqa
 * @since V2.5
 */
@SuppressWarnings("unchecked")
public final class ArrayUtils {

	/**
	 * �޷����������ҵ�Ԫ��ʱ��indexֵ
	 */
	private final static int INDEX_NOT_FOUND = -1;

	/**
	 * ��������ݵ�Map�������Ҫ���Ƹ������ɿ���LRU���档
	 */
	private static Map<Class<?>, Object> emptyArrayCache = new HashMap<Class<?>, Object>();

	/**
	 * An empty immutable <code>String</code> array.
	 */
	public final static String[] EMPTY_STRING_ARRAY = getEmptyArray(String.class);

	private ArrayUtils() {

	}

	public static <T> T[] convert(Object[] srcArray, Class<T> convertToClass) {
		if (srcArray == null || srcArray.length == 0 || convertToClass == null) {
			return null;
		}
		T[] convertResult = (T[]) Array.newInstance(convertToClass, srcArray.length);
		System.arraycopy(srcArray, 0, convertResult, 0, srcArray.length);
		return convertResult;
	}

	/**
	 * check whether the given array is null or its' length is 0
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		if (array == null || array.length == 0)
			return true;

		return false;
	}

	public static boolean isNotEmpty(Object[] array) {
		return !isEmpty(array);
	}

	/**
	 * check whether the given array is null or its' length is 0
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(int[] array) {
		if (array == null || array.length == 0)
			return true;

		return false;
	}

	/**
	 * ��������
	 * 
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> T[] clone(T[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * �ж����������Ƿ�һ��
	 * 
	 * @param <T>
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static <T> boolean equals(T[] array1, T[] array2) {
		if (array1 == null && array2 != null)
			return false;

		if (array1 != null && array2 == null)
			return false;

		if (array1 != null && array2 != null && array1.length != array2.length)
			return false;

		if (array1 != null && array2 != null) {
			for (int i = 0; i < array1.length; i++) {
				if (array1[i] != array2[i] && !array1[i].equals(array2[i])) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * �ж������е�ֵ�Ƿ�һ�£�˳����Բ�һ����
	 * 
	 * @param <T>
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static <T> boolean equalsValue(T[] array1, T[] array2) {
		if (array1 == null && array2 == null)
			return true;
		// ��֤�˳���һ�£��������һ����ֵ������һ�������ж����ҵ�����ô����һ���ġ�
		if (array1 != null && array2 != null && array1.length == array2.length) {
			List<T> array1ToList = Arrays.asList(array1);
			List<T> array2ToList = Arrays.asList(array2);
			for (T value : array1ToList) {
				if (!array2ToList.contains(value)) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * �ж��������Ƿ������ͬ��ֵ
	 * 
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> boolean isSameValue(T[] array) {
		if (array == null || array.length == 0) {
			return false;
		}

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (array[i].equals(array[j]) && i != j) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * �ж�������ֵ���Ƿ������ͬ��ֵ
	 * 
	 * @param <T>
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static <T> boolean hasSameValue(T[] array1, T[] array2) {
		if (array1 == null || array1.length == 0 || array2 == null || array2.length == 0) {
			return false;
		}

		for (int i = 0; i < array1.length; i++) {
			if (indexOf(array2, array1[i]) > -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �ж���������ĳ����Ƿ�һ��
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean isSameLength(Object[] array1, Object[] array2) {
		if (array1 == null && array2 == null)
			return true;
		if (array1 == null && array2 != null && array2.length == 0)
			return true;
		if (array2 == null && array1 != null && array1.length == 0)
			return true;
		if (array1 != null && array2 != null && array1.length == array2.length)
			return true;

		return false;
	}

	/**
	 * ��ȡ����ĳ��ȣ���Ӧ���� ������������ ���磺 getLength((Object)(int[]{....}))
	 * 
	 * @param array
	 * @return
	 */
	public static int getLength(Object array) {
		if (array == null)
			return 0;

		return Array.getLength(array);
	}

	/**
	 * ����������Ƿ����ĳ�����������򷵻ض�Ӧ�������±꣬��û�з���-1
	 * 
	 * @param <T>
	 * @param array
	 * @param objectToFind
	 * @return
	 */
	public static <T> int indexOf(T[] array, T objectToFind) {
		return indexOf(array, objectToFind, 0);
	}

	/**
	 * ����������Ƿ����ĳ�����������򷵻ض�Ӧ�������±꣬��û�з���-1
	 * 
	 * @param array
	 * @param element
	 * @return
	 */
	public static int indexOf(int[] array, int element) {
		return indexOf(array, element, 0);
	}

	/**
	 * ��ָ�����±꿪ʼ������������Ƿ����ĳ�����������򷵻ض�Ӧ�������±꣬���Ҳ������� -1
	 * 
	 * @param <T>
	 * @param array
	 * @param objectToFind
	 * @param startIndex
	 * @return
	 */
	public static <T> int indexOf(T[] array, T objectToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		if (objectToFind == null) {
			for (int i = startIndex; i < array.length; i++) {
				if (array[i] == null) {
					return i;
				}
			}

		} else {
			for (int i = startIndex; i < array.length; i++) {
				if (objectToFind.equals(array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * ��ָ�����±꿪ʼ������������Ƿ����ĳ�����������򷵻ض�Ӧ�������±꣬���Ҳ������� -1
	 * 
	 * @param array
	 * @param element
	 * @param startIndex
	 * @return
	 */
	public static int indexOf(int[] array, int element, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (array[i] == element) {
				return i;
			}
		}

		return INDEX_NOT_FOUND;
	}

	/**
	 * ���ԭ�������и�Ԫ�أ��򷵻�ԭ���� ���û�У������������������µ�Ԫ�أ������µ�����
	 * 
	 * @param array
	 * @param element
	 * @return
	 */
	public static <T> T[] addElement(T[] array, T... elements) {
		if (isEmpty(elements))
			return array;

		if (isEmpty(array)) {
			return elements;
		}
		List<T> list = new ArrayList<T>();

		for (T element : elements) {
			if (indexOf(array, element) == INDEX_NOT_FOUND)
				list.add(element);
		}

		T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + list.size());
		System.arraycopy(array, 0, newArray, 0, array.length);

		System.arraycopy(list.toArray(), 0, newArray, array.length/* + 1 */, list.size());

		return newArray;
	}

	/**
	 * ���ԭ�������и�Ԫ�أ��򷵻�ԭ���� ���û�У������������������µ�Ԫ�أ������µ�����
	 * 
	 * @param array
	 * @param element
	 * @return
	 */
	public static int[] addElement(int[] array, int element) {

		if (isEmpty(array))
			return new int[] { element };
		if (indexOf(array, element) != INDEX_NOT_FOUND) {
			return array;
		}

		int[] newArray = new int[array.length + 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length - 1] = element;

		return newArray;
	}

	/**
	 * ����������Ӳ�����ԭ�����е�Ԫ�ء�
	 * 
	 * @param array
	 * @param elems
	 * @return
	 */
	public static int[] addElement(int[] array, int[] elems) {
		if (isEmpty(array))
			return elems;
		if (isEmpty(elems))
			return array;

		List<Integer> intList = new ArrayList<Integer>();
		for (int i = 0; i < elems.length; i++) {
			if (indexOf(array, elems[i]) == INDEX_NOT_FOUND) {
				intList.add(Integer.valueOf(elems[i]));
			}
		}
		int[] newArray = new int[array.length + intList.size()];
		for (int i = 0; i < newArray.length; i++) {
			if (i < array.length) {
				newArray[i] = array[i];
			} else {
				newArray[i] = intList.get(i - array.length);
			}
		}
		return newArray;
	}

	/**
	 * ��������ɾ����ָ��index��Ԫ�أ����µ����顣
	 * 
	 * @param <T>
	 * @param array
	 * @param index
	 * @return
	 */
	public static <T> T[] remove(T[] array, int index) {
		int length = getLength(array);
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}

		Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
		System.arraycopy(array, 0, result, 0, index);
		if (index < length - 1) {
			System.arraycopy(array, index + 1, result, index, length - index - 1);
		}

		return (T[]) result;
	}

	/**
	 * ��������ɾ��ָ����Ԫ�أ�ʹ��equals�ж�
	 * 
	 * @param <T>
	 * @param array
	 * @param element
	 * @return
	 */
	public static <T> T[] remove(T[] array, T element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * ɾ�������е�null����
	 * 
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> T[] removeNullElements(T[] array) {
		if (isEmpty(array))
			return array;
		List<Object> list = new ArrayList<Object>();

		for (T obj : array) {
			if (obj != null)
				list.add(obj);
		}
		T[] returnArray = list.toArray((T[]) Array.newInstance(array.getClass().getComponentType(), list.size()));
		return returnArray;
	}

	/**
	 * ȥ��array�����е�element������equals()�����ж�
	 * 
	 * @param <T>
	 * @param array
	 * @param element
	 * @return
	 */
	public static <T> T[] removeElements(T[] array, T element) {
		if (isEmpty(array))
			return array;
		List<T> list = new ArrayList<T>();
		if (element == null) {
			for (T obj : array) {
				if (obj != null) {
					list.add(obj);
				}
			}
		} else {
			for (T obj : array) {
				if (!obj.equals(element)) {
					list.add(obj);
				}
			}
		}
		return list.toArray((T[]) Array.newInstance(array.getClass().getComponentType(), list.size()));
	}

	public static <T> T[] removeElements(T[] array, T[] subArray) throws RuleBaseException {
		if (getLength(array) < getLength(subArray))
			throw new RuleBaseException("�������Ԫ�ظ�����ԭ����࣬�޷�ɾ��");

		if (isEmpty(subArray))
			return array;
		// �������ж� �����鳤���Ƿ񳬹�ԭ���飬���ԭ����Ϊ�գ����ֱ�ӷ���
		if (isEmpty(array))
			return array;

		T[] newArray = clone(array);

		for (T element : subArray) {
			int index = indexOf(newArray, element);
			if (index != INDEX_NOT_FOUND) {
				newArray = remove(newArray, index);
			} else {
				throw new RuleBaseException("�������а���ԭ������û�е�Ԫ�أ��޷�ɾ��");
			}
		}

		return newArray;

	}

	/**
	 * �����������������ϲ���һ�𣬽�null���߿�ֵ��
	 * 
	 * <pre>
	 *  ���磺
	 * 
	 * 		String[] s1 = new String[]{&quot;11&quot;,&quot;12&quot;,&quot;13&quot;};
	 * 		String[] s2 = new String[0];
	 * 		String[] s3 = new String[]{&quot;31&quot;,&quot;32&quot;,&quot;33&quot;};
	 * 		String[] s4 = new String[]{&quot;41&quot;,&quot;42&quot;,&quot;43&quot;};
	 * 		String[] s5 = null;
	 * 		String[] s6 = new String[]{&quot;51&quot;,&quot;52&quot;,&quot;53&quot;};
	 * 
	 * 		ArrayUtils.mergeArrays(s1,s2,s3,s4,s5,s6) ����11��12��13��31��32��33��41��42��43��51��52��53
	 * 
	 * 
	 * </pre>
	 * 
	 * @param <T>
	 *            ��������
	 * @param arrays
	 *            ������������
	 * @return
	 */
	public static <T> T[] mergeArrays(T[]... arrays) {
		if (isEmpty(arrays))
			return null;
		int length = 0;
		for (T[] array : arrays) {
			if (!isEmpty(array))
				length = getLength(array) + length;
		}
		Object[] mergedArray = (Object[]) Array.newInstance(arrays.getClass().getComponentType().getComponentType(),
				length);
		int pos = 0;
		for (T[] array : arrays) {
			if (!isEmpty(array))
				System.arraycopy(array, 0, mergedArray, pos, getLength(array));
			pos = pos + getLength(array);
		}

		return (T[]) mergedArray;
	}

	public static <T> T[] mergeArrays(T[] arrays1, T[] arrays2) {
		if (isEmpty(arrays1) && isEmpty(arrays2)) {
			return null;
		} else if (isEmpty(arrays1)) {
			return arrays2;
		} else if (isEmpty(arrays2)) {
			return arrays1;
		}

		int length = getLength(arrays1) + getLength(arrays2);
		Object[] mergedArray = (Object[]) Array.newInstance(arrays1.getClass().getComponentType(), length);
		int pos = 0;
		System.arraycopy(arrays1, 0, mergedArray, pos, getLength(arrays1));
		pos = pos + getLength(arrays1);
		System.arraycopy(arrays2, 0, mergedArray, pos, getLength(arrays2));
		return (T[]) mergedArray;
	}

	/**
	 * �ҳ����������е���ͬԪ��
	 * 
	 * @param <T>
	 * @param array
	 * @param subArray
	 * @return
	 * @author heyy1 2009-03-12
	 */
	public static <T> T[] getSameData(T[] array, T[] subArray) {
		if (isEmpty(array) || isEmpty(subArray)) {
			return null;
		}
		List<T> newData = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				for (int j = 0; j < subArray.length; j++) {
					if (subArray[j] != null)
						if (array[i].equals(subArray[j])) {
							newData.add(array[i]);
						}
				}
			}
		}
		T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), newData.size());
		result = newData.toArray(result);
		return removeNullElements(result);
	}

	/**
	 * ����ת��ΪList
	 * 
	 * @param <T>
	 * @param array
	 * @return
	 * @author heyy1
	 */
	public static <T> List<T> changeToList(T[] array) {
		List<T> data = new ArrayList<T>();
		if (isEmpty(array)) {
			return null;
		}
		for (int i = 0; i < array.length; i++) {
			data.add(array[i]);
		}
		return data;
	}

	public synchronized static <T> T[] getEmptyArray(Class<T> clazz) {
		if (clazz == null)
			return null;
		Object emptyArray = emptyArrayCache.get(clazz);
		if (emptyArray == null) {
			emptyArray = Array.newInstance(clazz, 0);
			emptyArrayCache.put(clazz, emptyArray);
		}
		return (T[]) emptyArray;
	}

	/**
	 * ��������ĵ�һ��Ԫ��
	 * 
	 * @param <T>
	 * @param array
	 * @return
	 * @author yanjq
	 */
	public static <T> T getFirstElem(T[] array) {
		return isEmpty(array) ? null : array[0];
	}

	/**
	 * ��ȡ�̶����ȵ����飬���Ȳ�����null
	 * 
	 * @param <T>
	 * @param array
	 * @param fixedLength
	 * @return
	 * @author yanjq
	 */
	public static <T> T[] getFixedLengthArray(T[] array, int fixedLength) {
		if (isEmpty(array))
			return null;
		int len = getLength(array);
		if (len == fixedLength) {
			return array;
		}
		T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), fixedLength);
		System.arraycopy(array, 0, result, 0, len > fixedLength ? fixedLength : len);
		return result;
	}

	/**
	 * ������ת����Ϊ����
	 * 
	 * @param <T>
	 * @param obj
	 * @param clazz
	 * @return
	 */
	public static <T> T[] convertToArray(Object obj, Class<T> clazz) {
		if (obj == null)
			return (T[]) Array.newInstance(clazz, 0);

		T[] objs = null;
		if (obj.getClass().isArray()) {
			objs = (T[]) obj;
		} else {
			objs = (T[]) Array.newInstance(clazz, 1);
			objs[0] = (T) obj;
		}

		return objs;
	}

	/**
	 * �����������ת��һ��
	 * 
	 * @param <T>
	 * @param objs
	 * @param clazz
	 * @return
	 */
	public static <T> T[] convertArrayType(Object[] objs, Class<T> clazz) {
		if (objs == null) {
			return (T[]) Array.newInstance(clazz, 0);
		}

		if (objs.getClass().getComponentType().equals(clazz)) {
			return (T[]) objs;
		}

		T[] result = (T[]) Array.newInstance(clazz, objs.length);

		for (int i = 0; i < objs.length; i++) {
			result[i] = (T) objs[i];
		}

		return result;
	}

	/**
	 * �����Map��Collecitonת��Ϊ���List��Map�� ��ԭ��Key��ͬ��ֵ�ռ���һ��List�С��õ��µĴ��List��Map��
	 * Ŀǰ����QuerySession��ѯ������μ�AssetImpl.queryRelationItemValues(...)
	 * 
	 * @param <K>
	 * @param <V>
	 * @param list
	 * @return
	 * @author yanjq
	 */

	public static <K, V> Map<K, List<V>> convertCollectionMapToMapList(Collection<Map<K, V>> list) {
		if (list == null || list.size() == 0)
			return Collections.emptyMap();

		Map<K, List<V>> rm = new HashMap<K, List<V>>();

		for (Map<K, V> m : list) {
			for (Map.Entry<K, V> entry : m.entrySet()) {
				List<V> valueList = rm.get(entry.getKey());
				if (valueList == null) {
					valueList = new ArrayList<V>();
					rm.put(entry.getKey(), valueList);
				}
				valueList.add(entry.getValue());
			}
		}

		return rm;
	}
}