package com.yonyou.nc.codevalidator.export.excel.utils;

@Deprecated
public class ExcelFileLocator {

	private ExcelFileLocator() {

	}

	/**
	 * 返回输出excel文件
	 * 
	 * @param businessCompPath
	 *            业务组件文件夹路径
	 * @return
	 */
	public static String getOutFile(String businessCompPath) {
		String prefix = businessCompPath.substring(0, businessCompPath.lastIndexOf("."));
		String suffix = businessCompPath.substring(businessCompPath.lastIndexOf("."));

		return prefix + "_" + System.currentTimeMillis() + suffix;
	}

}
