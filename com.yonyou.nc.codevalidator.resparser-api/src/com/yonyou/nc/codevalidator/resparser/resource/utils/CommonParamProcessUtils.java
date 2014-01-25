package com.yonyou.nc.codevalidator.resparser.resource.utils;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;

/**
 * 通用参数处理的工具类
 * @author mazhqa
 * @since V2.3
 */
public final class CommonParamProcessUtils {
	
	/**
	 * 得到参数：CommonRuleParams.CODECONTAINEXTRAFOLDER 中是否包含额外的文件夹
	 * @param ruleExecuteContext
	 * @return
	 */
	public static boolean getCodeContainsExtraFolder(IRuleExecuteContext ruleExecuteContext){
		String codeContainsExtraFolder = ruleExecuteContext.getParameter(CommonRuleParams.CODECONTAINEXTRAFOLDER);
		return codeContainsExtraFolder == null ?  false : codeContainsExtraFolder.equalsIgnoreCase("true");
	}

}
