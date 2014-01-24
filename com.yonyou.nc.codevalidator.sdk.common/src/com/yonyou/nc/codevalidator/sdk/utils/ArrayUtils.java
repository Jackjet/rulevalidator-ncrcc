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
 * 数组处理的相关工具类，只要是从NC资产部门获得
 * @author mazhqa
 * @since V2.5
 */
@SuppressWarnings("unchecked")
public final class ArrayUtils {

	/**
	 * 无法在数组中找到元素时的index值
	 */
	private final static int INDEX_NOT_FOUND = -1;

	/**
	 * 缓存空数据的Map，如果需要限制个数，可考虑LRU缓存。
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
	 * 复制数组
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
	 * 判断两个数组是否一致
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
	 * 判断数组中的值是否一致（顺序可以不一样）
	 * 
	 * @param <T>
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static <T> boolean equalsValue(T[] array1, T[] array2) {
		if (array1 == null && array2 == null)
			return true;
		// 保证了长度一致，如果其中一个的值在另外一个数组中都能找到，那么就是一样的。
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
	 * 判断数组中是否存在相同的值
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
	 * 判断两个数值中是否存在相同的值
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
	 * 判断两个数组的长度是否一致
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
	 * 获取数组的长度，可应用于 基本类型数组 比如： getLength((Object)(int[]{....}))
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
	 * 在数组查找是否包含某个对象，如有则返回对应的数组下标，如没有返回-1
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
	 * 在数组查找是否包含某个对象，如有则返回对应的数组下标，如没有返回-1
	 * 
	 * @param array
	 * @param element
	 * @return
	 */
	public static int indexOf(int[] array, int element) {
		return indexOf(array, element, 0);
	}

	/**
	 * 从指定的下标开始，在数组查找是否包含某个对象，如有则返回对应的数组下标，如找不到返回 -1
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
	 * 从指定的下标开始，在数组查找是否包含某个对象，如有则返回对应的数组下标，如找不到返回 -1
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
	 * 如果原数组中有该元素，则返回原数组 如果没有，则在数组基础上添加新的元素，返回新的数组
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
	 * 如果原数组中有该元素，则返回原数组 如果没有，则在数组基础上添加新的元素，返回新的数组
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
	 * 在数组中添加不存在原数组中的元素。
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
	 * 在数组中删除掉指定index的元素，并新的数组。
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
	 * 在数组中删除指定的元素，使用equals判断
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
	 * 删除数组中的null对象
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
	 * 去掉array中所有的element。调用equals()方法判断
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
			throw new RuleBaseException("子数组的元素个数比原数组多，无法删除");

		if (isEmpty(subArray))
			return array;
		// 由于先判断 子数组长度是否超过原数组，如果原数组为空，则可直接返回
		if (isEmpty(array))
			return array;

		T[] newArray = clone(array);

		for (T element : subArray) {
			int index = indexOf(newArray, element);
			if (index != INDEX_NOT_FOUND) {
				newArray = remove(newArray, index);
			} else {
				throw new RuleBaseException("子数组中包含原数组中没有的元素，无法删除");
			}
		}

		return newArray;

	}

	/**
	 * 将多个引用类型数组合并在一起，将null或者空值。
	 * 
	 * <pre>
	 *  比如：
	 * 
	 * 		String[] s1 = new String[]{&quot;11&quot;,&quot;12&quot;,&quot;13&quot;};
	 * 		String[] s2 = new String[0];
	 * 		String[] s3 = new String[]{&quot;31&quot;,&quot;32&quot;,&quot;33&quot;};
	 * 		String[] s4 = new String[]{&quot;41&quot;,&quot;42&quot;,&quot;43&quot;};
	 * 		String[] s5 = null;
	 * 		String[] s6 = new String[]{&quot;51&quot;,&quot;52&quot;,&quot;53&quot;};
	 * 
	 * 		ArrayUtils.mergeArrays(s1,s2,s3,s4,s5,s6) 返回11、12、13、31、32、33、41、42、43、51、52、53
	 * 
	 * 
	 * </pre>
	 * 
	 * @param <T>
	 *            引用类型
	 * @param arrays
	 *            引用类型数组
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
	 * 找出两个数组中的相同元素
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
	 * 数组转换为List
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
	 * 返回数组的第一个元素
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
	 * 截取固定长度的数组，长度不够则补null
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
	 * 将对象转换成为数组
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
	 * 将数组的类型转换一下
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
	 * 將存放Map的Colleciton转化为存放List的Map。 将原先Key相同的值收集到一个List中。得到新的存放List的Map。
	 * 目前用在QuerySession查询关联项，参见AssetImpl.queryRelationItemValues(...)
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