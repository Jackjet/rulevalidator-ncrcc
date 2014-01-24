package com.yonyou.nc.codevalidator.rule.executor;

import java.util.Map;

import com.yonyou.nc.codevalidator.rule.IRuleDefinition;

/**
 * 将原有的查询规则执行器的静态类更改为声明式服务，实现更加符合OSGi的标准
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleExecutorFactory {
	
	/**
	 * 规则标识符到规则定义的映射
	 * @return
	 */
	Map<String, IRuleDefinition> getIdentifierToDefinitionMap();
	

}
