package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;

public class ExecuteUnitUtils {

	private ExecuteUnitUtils() {

	}
	
	/**
	 * 根据模块名称或业务组件所对应的所有元数据
	 * @param businessComponent
	 * @return
	 * @throws RuleBaseRuntimeException - 当执行单元数GlobalExecuteUnit时
	 */
	public static String getMetadataFolderPath(BusinessComponent businessComponent) {
		if (businessComponent instanceof GlobalExecuteUnit) {
			throw new RuleBaseRuntimeException("当执行全局规则时无法获取对应的元数据文件夹!");
		}
		return String.format("%s/METADATA", businessComponent.getBusinessComponentPath());
	}

}
