package com.yonyou.nc.codevalidator.rule.vo;

/**
 * 规则真正执行时的检查配置，是需要从"业务组件 - 模块 - 全局"配置中获得
 * 
 * @author mazhqa
 * @since V2.6
 */
public class ConcreteRuleCheckConfiguration extends AbstractRuleCheckConfiguration {

	@Override
	public int getPriority() {
		throw new UnsupportedOperationException("运行时的规则配置，不能调用优先级的方法!");
	}

	@Override
	public void setPriority(int priority) {
		throw new UnsupportedOperationException("运行时的规则配置，不能调用优先级的方法!");
	}

}
