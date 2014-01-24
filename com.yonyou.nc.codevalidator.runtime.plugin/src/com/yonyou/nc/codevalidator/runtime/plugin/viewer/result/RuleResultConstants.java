package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import java.util.Arrays;
import java.util.List;

/**
 * 规则结果集的常量类，用于表格的绘制
 * @author mazhqa
 * @since V2.8
 */
public class RuleResultConstants {
	
	public static final List<RuleResultColumnVO> RULE_RESULT_COLUMNS = Arrays.asList(
			new RuleResultColumnVO("状态", 20, IRuleResultVOProperty.RULE_EXECUTE_STATUS),
			new RuleResultColumnVO("执行级别", 80, IRuleResultVOProperty.RULE_EXECUTE_LEVEL),
			new RuleResultColumnVO("规则标识", 120, IRuleResultVOProperty.RULE_IDENTIFIER),
			new RuleResultColumnVO("执行单元", 150, IRuleResultVOProperty.EXECUTE_UNIT),
			new RuleResultColumnVO("规则类型", 180, IRuleResultVOProperty.RULE_CATALOG),
			new RuleResultColumnVO("规则执行详细结果", 300, IRuleResultVOProperty.RESULT_NOTE)
			);
	
	private RuleResultConstants() {
		
	}

}
