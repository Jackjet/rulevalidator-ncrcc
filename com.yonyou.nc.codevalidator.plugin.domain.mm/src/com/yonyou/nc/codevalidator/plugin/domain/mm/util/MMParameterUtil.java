package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * MM领域自动化参数相关工具类
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MMParameterUtil {

	
	private MMParameterUtil(){
		
	}
	/**
	 * 通过参数和参数名取得参数的值
	 * 
	 * @param paramProperty
	 * @param itfFields
	 * @return
	 */
	public static String[] getParameterValues(Properties paramProperty,
			String[] paramNames) {
		if (MMValueCheck.isEmpty(paramNames)) {
			return new String[] {};
		}
		List<String> params = new ArrayList<String>();
		for (String param : paramNames) {
			if (null != paramProperty.getProperty(param)
					&& !"null".equals(paramProperty.getProperty(param)
							.trim())
					&& paramProperty.getProperty(param).length() > 0) {
				params.add(paramProperty.getProperty(param));
			}

		}
		return params.toArray(new String[params.size()]);
	}
}
