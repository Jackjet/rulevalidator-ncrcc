package com.web.pub.fw.tools;

import java.math.BigDecimal;

/**
 * 精度处理器
 * 
 * @author lixln
 * 
 */
public class ScaleHelper {

	/**
	 * 精度处理，获取字符型数据
	 * 
	 * @param data
	 * @param scale
	 * @return
	 */
	public static String getString(BigDecimal data, int scale) {
		if (data != null && scale != -1) {
			// 有精度
			return data.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
		} else if (data != null && scale == -1) {
			// 没精度
			return data.toString();
		} else {
			// 数据异常
			throw new RuntimeException(String.format("数据异常，数值数据为%s，精度为%s",
					new Object[] { data, scale }));
		}
	}
}
