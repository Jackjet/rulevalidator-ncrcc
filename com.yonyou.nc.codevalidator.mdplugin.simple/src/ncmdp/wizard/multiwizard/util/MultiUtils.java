package ncmdp.wizard.multiwizard.util;

import java.io.File;
import java.io.IOException;

import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPLogger;

public class MultiUtils {

	public static final String SIMP_TAG = "simpchn";

	public static final String TRAD_TAG = "tradchn";

	public static final String ENG_TAG = "english";

	public static final String[] CHARSETNAME = { "GBK", "unicode", "UTF-16", "UTF-8", "GB2312" };

	public static String getEngrFilePath(String simFilePath) {
		//D:\1\lang\simpchn\101201\101201res.properties
		return simFilePath.replace("\\" + SIMP_TAG + "\\", "\\" + ENG_TAG + "\\");
	}

	public static String getTradrFilePath(String simFilePath) {
		return simFilePath.replace("\\" + SIMP_TAG + "\\", "\\" + TRAD_TAG + "\\");
	}

	public static void creatFileOrDirs(String filePath) {
		File newFile = new File(filePath);
		if (!isFileString(filePath)) {
			if (!newFile.exists()) {
				newFile.mkdirs();
			}
		} else {// 文件
			if (filePath.contains("\\")) {
				filePath = filePath.replace("\\", "/");
			}
			int i = filePath.lastIndexOf("/");
			// 创建父目录
			String parentPath = filePath.substring(0, i);
			File parentDir = new File(parentPath);
			if (!parentDir.exists()) {
				parentDir.mkdirs();
			}
			if (newFile.exists()) {
				newFile.delete();
			}
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}
	}

	private static boolean isFileString(String filePath) {
		if (StringUtil.isEmptyWithTrim(filePath))
			return false;
		if (filePath.endsWith("/") || filePath.endsWith("\\"))
			return false;
		if (filePath.contains("."))
			return true;
		return false;
	}

}
