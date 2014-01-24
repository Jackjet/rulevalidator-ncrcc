package com.yonyou.nc.codevalidator.resparser.resource.utils;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;

/**
 * ͨ�ò�������Ĺ�����
 * @author mazhqa
 * @since V2.3
 */
public final class CommonParamProcessUtils {
	
	/**
	 * �õ�������CommonRuleParams.CODECONTAINEXTRAFOLDER ���Ƿ����������ļ���
	 * @param ruleExecuteContext
	 * @return
	 */
	public static boolean getCodeContainsExtraFolder(IRuleExecuteContext ruleExecuteContext){
		String codeContainsExtraFolder = ruleExecuteContext.getParameter(CommonRuleParams.CODECONTAINEXTRAFOLDER);
		return codeContainsExtraFolder == null ?  false : codeContainsExtraFolder.equalsIgnoreCase("true");
	}

}
