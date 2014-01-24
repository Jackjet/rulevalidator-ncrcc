package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;

public class ExecuteUnitUtils {

	private ExecuteUnitUtils() {

	}
	
	/**
	 * ����ģ�����ƻ�ҵ���������Ӧ������Ԫ����
	 * @param businessComponent
	 * @return
	 * @throws RuleBaseRuntimeException - ��ִ�е�Ԫ��GlobalExecuteUnitʱ
	 */
	public static String getMetadataFolderPath(BusinessComponent businessComponent) {
		if (businessComponent instanceof GlobalExecuteUnit) {
			throw new RuleBaseRuntimeException("��ִ��ȫ�ֹ���ʱ�޷���ȡ��Ӧ��Ԫ�����ļ���!");
		}
		return String.format("%s/METADATA", businessComponent.getBusinessComponentPath());
	}

}
