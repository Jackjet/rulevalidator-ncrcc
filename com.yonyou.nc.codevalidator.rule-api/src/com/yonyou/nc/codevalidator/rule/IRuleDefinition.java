package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 通用的规则定义，任何的规则都要从该接口派生
 * <p>
 * 对于规则在执行容器中都是单例的，要确保规则是无状态对象，不要在其中定义任何成员变量
 * 
 * @author clamaa
 * @since V1.0
 */
public interface IRuleDefinition extends IIdentifier {

	/**
	 * 使用执行上下文环境执行规则，并返回执行结果
	 * 
	 * @param ruleExecContext
	 *            -
	 * @return
	 * @throws RuleExecuteException
	 */
	IRuleExecuteResult actualExecute(IRuleExecuteContext ruleExecContext) throws RuleBaseException;

	/**
	 * 得到依赖的creator对象，在规则执行前，执行的一些初始化操作 可以去取在CreatorConstants中的常量
	 * 
	 * @return
	 */
	String[] getDependentCreator();

}
