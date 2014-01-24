package com.yonyou.nc.codevalidator.sdk.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

	public static final String SPLITTOKEN = ",";
	public static final String VALUETOKEN = "=";
	public static final String NC_DEFAULT_NULL_VALUE = "~";

	private StringUtils() {

	}

	/**
	 * 判断该字符串是否为null或只包含空格
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断该字符串是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		if (str == null || str.trim().length() == 0) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串str中是否包含中文字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containsChineseCharacter(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		for (int i = 0; i < str.length(); i++) {
			if (String.valueOf(str.charAt(i)).matches("[\\u4e00-\\u9fa5]+")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串str中是否全部都是中文字符
	 * 
	 * @param str
	 *            - 如果字符串str为空，返回false
	 * @return
	 */
	public static boolean isAllChineseCharacter(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!String.valueOf(str.charAt(i)).matches("[\\u4e00-\\u9fa5]+")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断一个字符串是否是字符串数组中的一个。
	 * 
	 * @param str
	 * @param strs
	 * @return
	 * @author taorz1
	 * @time 2010-11-28 下午08:49:20
	 */
	public static boolean isElement(String str, String[] strs) {
		if (strs != null)
			for (int i = 0; i < strs.length; i++)
				if (isNcStringNotEmpty(str) && str.equals(strs[i]))
					return true;
		return false;
	}

	/**
	 * 判断一个字符串是否不是字符串数组中的任何一个。
	 * 
	 * @创建日期：(00-8-31 11:06:16)
	 */
	public static boolean isNotElement(String str, String[] strArray) {
		return !isElement(str, strArray);
	}

	/**
	 * nc特有的方式，将~认为是空值。
	 * 
	 * @param str
	 * @return
	 * @author taorz1
	 * @time 2011-3-15 上午11:50:20
	 */
	public static boolean isNcStringEmpty(String str) {
		return str == null || str.length() == 0 || NC_DEFAULT_NULL_VALUE.equals(str);
	}

	/**
	 * nc特有的方式，将~认为是空值。
	 * 
	 * @param str
	 * @return
	 * @author taorz1
	 * @time 2011-3-15 上午11:51:51
	 */
	public static boolean isNcStringNotEmpty(String str) {
		return str != null && (str.length() > 0 && !NC_DEFAULT_NULL_VALUE.equals(str));
	}

	/**
	 * 将String数组做差集运算。
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 * @author taorz1
	 * @time 2011-4-7 上午09:48:31
	 */
	public static String[] subArray(String[] strs1, String[] strs2) {
		// 空集合
		if (strs2 == null || strs2.length == 0)
			return strs1;
		// 空集合
		if (strs1 == null || strs1.length == 0)
			return strs1;

		List<String> result = new ArrayList<String>();
		for (String str1 : strs1) {
			// 如果strs1中元素在集合strs2中
			if (!isElement(str1, strs2)) {
				result.add(str1);
			}
		}

		return result.toArray(new String[0]);
	}

	/**
	 * 方法功能: 输入的字符串是否包含正则表达式，即是否存在与正则表达式匹配的子字符串
	 * 
	 * @param regex
	 *            正则表达式
	 * @param input
	 *            输入的字符串
	 * @return true: 匹配; false：不匹配
	 */
	public static boolean isSubMatchRegex(String regex, String input) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		boolean isMatch = false;
		if (m.find())
			isMatch = true;
		return isMatch;
	}

	/**
	 * 去掉重复的字符串。
	 * 
	 * @param orginStrs
	 * @return
	 * @author taorz1
	 * @time 2011-5-21 下午02:17:13
	 */
	public static String[] getUniqueStrings(String[] orginStrs) {
		// 过滤掉重复的数据。
		Set<String> set = new HashSet<String>();
		if (orginStrs != null) {
			for (String orginStr : orginStrs) {
				set.add(orginStr);
			}
		}
		return set.toArray(new String[0]);
	}

	/**
	 * 使用分隔符为数组构造特定字符串，例如:当参数为buildNumberSeqString(new Integer[]{1,2,3}, " ->")
	 * 下面字符串将被返回 "1 ->2 ->3"
	 * 
	 * @param objs
	 *            待分项集合
	 * @param spliterStr
	 *            分隔符
	 * @return
	 */
	public static String buildStringUseSpliter(Object[] objs, String spliterStr) {
		if (objs != null && spliterStr != null) {
			if (objs.length == 1) {
				return objs[0].toString();
			} else {
				StringBuffer strBuff = new StringBuffer();
				for (int i = 0; i < objs.length; i++) {
					if (i == 0) {
						strBuff.append(objs[0] == null ? "" : objs[0].toString());
					} else {
						strBuff.append(spliterStr + (objs[i] == null ? "" : objs[i].toString()));
					}
				}
				return strBuff.toString();
			}
		}
		return "Error Sequence";

	}

	/**
	 * 使用分隔符为数组构造特定字符串加上单引号， 例如：new String[]{"a","b","c"} ---> 'a', 'b', 'c'
	 * 
	 * @return
	 */
	public static String buildStringUseSpliterWithQuotes(Object[] values, String spliterStr) {
		if (values == null || values.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int length = values.length;
		for (int i = 0; i < length; i++) {
			sb.append("'");
			sb.append(values[i]);
			sb.append("'");
			sb.append(spliterStr);
		}
		length = sb.length();
		sb.deleteCharAt(length - 1);

		return sb.toString();
	}

	/**
	 * 判断字符串string是否包含特殊字符
	 * 
	 * @param string
	 * @return
	 */
	public static boolean containSpecialCharacter(String string) {
		char[] chararray = string.toCharArray();
		boolean flag = false;
		if (string != null && string.length() > 0) {
			for (char c : chararray) {
				if ((c >= 65 && c <= 81) || (c >= 97 && c <= 123) || (c + "").equals("_")) {
				} else {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

}
