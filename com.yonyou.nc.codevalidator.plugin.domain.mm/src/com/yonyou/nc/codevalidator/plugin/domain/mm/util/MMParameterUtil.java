package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * MM�����Զ���������ع�����
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MMParameterUtil {

	
	private MMParameterUtil(){
		
	}
	/**
	 * ͨ�������Ͳ�����ȡ�ò�����ֵ
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
