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
	 * �жϸ��ַ����Ƿ�Ϊnull��ֻ�����ո�
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
	 * �жϸ��ַ����Ƿ�Ϊ��
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
	 * �ж��ַ���str���Ƿ���������ַ�
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
	 * �ж��ַ���str���Ƿ�ȫ�����������ַ�
	 * 
	 * @param str
	 *            - ����ַ���strΪ�գ�����false
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
	 * �ж�һ���ַ����Ƿ����ַ��������е�һ����
	 * 
	 * @param str
	 * @param strs
	 * @return
	 * @author taorz1
	 * @time 2010-11-28 ����08:49:20
	 */
	public static boolean isElement(String str, String[] strs) {
		if (strs != null)
			for (int i = 0; i < strs.length; i++)
				if (isNcStringNotEmpty(str) && str.equals(strs[i]))
					return true;
		return false;
	}

	/**
	 * �ж�һ���ַ����Ƿ����ַ��������е��κ�һ����
	 * 
	 * @�������ڣ�(00-8-31 11:06:16)
	 */
	public static boolean isNotElement(String str, String[] strArray) {
		return !isElement(str, strArray);
	}

	/**
	 * nc���еķ�ʽ����~��Ϊ�ǿ�ֵ��
	 * 
	 * @param str
	 * @return
	 * @author taorz1
	 * @time 2011-3-15 ����11:50:20
	 */
	public static boolean isNcStringEmpty(String str) {
		return str == null || str.length() == 0 || NC_DEFAULT_NULL_VALUE.equals(str);
	}

	/**
	 * nc���еķ�ʽ����~��Ϊ�ǿ�ֵ��
	 * 
	 * @param str
	 * @return
	 * @author taorz1
	 * @time 2011-3-15 ����11:51:51
	 */
	public static boolean isNcStringNotEmpty(String str) {
		return str != null && (str.length() > 0 && !NC_DEFAULT_NULL_VALUE.equals(str));
	}

	/**
	 * ��String����������㡣
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 * @author taorz1
	 * @time 2011-4-7 ����09:48:31
	 */
	public static String[] subArray(String[] strs1, String[] strs2) {
		// �ռ���
		if (strs2 == null || strs2.length == 0)
			return strs1;
		// �ռ���
		if (strs1 == null || strs1.length == 0)
			return strs1;

		List<String> result = new ArrayList<String>();
		for (String str1 : strs1) {
			// ���strs1��Ԫ���ڼ���strs2��
			if (!isElement(str1, strs2)) {
				result.add(str1);
			}
		}

		return result.toArray(new String[0]);
	}

	/**
	 * ��������: ������ַ����Ƿ����������ʽ�����Ƿ������������ʽƥ������ַ���
	 * 
	 * @param regex
	 *            ������ʽ
	 * @param input
	 *            ������ַ���
	 * @return true: ƥ��; false����ƥ��
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
	 * ȥ���ظ����ַ�����
	 * 
	 * @param orginStrs
	 * @return
	 * @author taorz1
	 * @time 2011-5-21 ����02:17:13
	 */
	public static String[] getUniqueStrings(String[] orginStrs) {
		// ���˵��ظ������ݡ�
		Set<String> set = new HashSet<String>();
		if (orginStrs != null) {
			for (String orginStr : orginStrs) {
				set.add(orginStr);
			}
		}
		return set.toArray(new String[0]);
	}

	/**
	 * ʹ�÷ָ���Ϊ���鹹���ض��ַ���������:������ΪbuildNumberSeqString(new Integer[]{1,2,3}, " ->")
	 * �����ַ����������� "1 ->2 ->3"
	 * 
	 * @param objs
	 *            �������
	 * @param spliterStr
	 *            �ָ���
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
	 * ʹ�÷ָ���Ϊ���鹹���ض��ַ������ϵ����ţ� ���磺new String[]{"a","b","c"} ---> 'a', 'b', 'c'
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
	 * �ж��ַ���string�Ƿ���������ַ�
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
