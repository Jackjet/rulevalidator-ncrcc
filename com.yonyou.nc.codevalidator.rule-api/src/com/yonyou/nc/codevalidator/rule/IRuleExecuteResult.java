package com.yonyou.nc.codevalidator.rule;

/**
 * 规则执行的结果，规则执行结果中包含如下的信息：
 * <li>规则执行是否成功</li>
 * <li>规则执行结果和备注</li>
 * <li>规则标识符</li>
 * <li>规则执行上下文</li>
 * @author mazhqa
 * @since V1.O
 */
/**
 * @author mazhqa
 *
 */
public interface IRuleExecuteResult {
	
//	/**
//	 * 规则执行是否成功
//	 * @return
//	 * @see #getRuleExecuteStatus()
//	 */
//	@Deprecated
//	boolean isSuccess();
//	
	/**
	 * 规则执行的结果状态
	 * @return
	 */
	RuleExecuteStatus getRuleExecuteStatus();
	
	/**
	 * 规则运行结果
	 * @return
	 */
	String getResult();
	
	/**
	 * 运行结果详细信息：备注
	 * @return
	 */
	String getNote();
	
	void setRuleDefinitionIdentifier(String ruleDefinitionIdentifier);
	
	/**
	 * 
	 * @return
	 */
	String getRuleDefinitionIdentifier();
	
	/**
	 * 得到规则执行的上下文，在每次执行后会自动进行set赋值操作
	 * <p>
	 * 规则配置执行的上下文用于显示执行的规则信息
	 * @return
	 */
	IRuleExecuteContext getRuleExecuteContext();
	
	void setRuleExecuteContext(IRuleExecuteContext ruleExecuteContext);
	
	/**
	 * 得到该结果运行时所在的业务组件
	 * @return
	 */
	BusinessComponent getBusinessComponent();
	
	void setBusinessComponent(BusinessComponent businessComponent);
}
