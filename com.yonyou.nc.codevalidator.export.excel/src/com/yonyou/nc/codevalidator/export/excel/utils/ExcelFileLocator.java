package com.yonyou.nc.codevalidator.export.excel.utils;

@Deprecated
public class ExcelFileLocator {

	private ExcelFileLocator() {

	}

	/**
	 * �������excel�ļ�
	 * 
	 * @param businessCompPath
	 *            ҵ������ļ���·��
	 * @return
	 */
	public static String getOutFile(String businessCompPath) {
		String prefix = businessCompPath.substring(0, businessCompPath.lastIndexOf("."));
		String suffix = businessCompPath.substring(businessCompPath.lastIndexOf("."));

		return prefix + "_" + System.currentTimeMillis() + suffix;
	}

}
