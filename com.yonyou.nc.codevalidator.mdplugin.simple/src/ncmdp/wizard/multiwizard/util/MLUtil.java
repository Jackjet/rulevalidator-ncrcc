package ncmdp.wizard.multiwizard.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MLUtil {

	/**
	 * 
	 */
	public MLUtil() {
		super();
		// TODO 自动生成构造函数存根
	}

	/**
	 * 将gb编码转换为unicode
	 * 创建日期：(2001-8-11 15:46:09)
	 * @return java.lang.String
	 * @param src java.lang.String
	 */
	public static String gb2Unicode(String src) {
		src = spaceToNull(src);
		if (src == null)
			return null;
		char[] c = src.toCharArray();
		int n = c.length;
		byte[] b = new byte[n];
		for (int i = 0; i < n; i++)
			b[i] = (byte) c[i];
		return new String(b);
	}

	/**
	 * 将空格转换为null
	 * 创建日期：(2001-9-15 11:07:41)
	 * @return java.lang.String
	 * @param str java.lang.String
	 */
	protected static String spaceToNull(String str) {
		if (str == null || str.length() == 0)
			return null;
		else
			return str.trim();
	}

	public static String uniCode2Gb(String src) {
		byte[] b = src.getBytes();
		int n = b.length;
		char[] c = new char[n];
		for (int i = 0; i < n; i++)
			c[i] = (char) ((short) b[i] & 0xff);
		return new String(c);
	}

	public static final boolean containsChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) { return true; }
		}
		return false;
	}

	// GENERAL_PUNCTUATION 判断中文的“号    
	// CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号    
	// HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号    
	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) { return true; }
		return false;
	}

	public static final boolean containsEN(String strName) {
		Pattern pattern = Pattern.compile("[a-zA-Z]");
		Matcher mar = pattern.matcher(strName);
		return mar.find();
	}

	public static final boolean containsOthers(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!isChinese(c) && !Character.isLetterOrDigit(c)) { return true; }
		}
		return false;
	}
}
