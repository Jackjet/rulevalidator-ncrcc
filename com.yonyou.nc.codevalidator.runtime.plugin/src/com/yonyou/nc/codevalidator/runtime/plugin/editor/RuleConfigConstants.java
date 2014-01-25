package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import java.util.Arrays;
import java.util.List;

/**
 * 规则配置的常量类
 * 
 * @author mazhqa
 * @since V2.3
 */
public final class RuleConfigConstants {

	public static final String EXECUTE_LEVEL_FIELDNAME = "执行级别";

	public static final List<RuleConfigColumnVO> RULE_CONFIG_MAIN_COLUMNS = Arrays.asList(new RuleConfigColumnVO[] {
			new RuleConfigColumnVO(EXECUTE_LEVEL_FIELDNAME, 100, IRuleConfigVOProperty.EXECUTE_LEVEL),
			new RuleConfigColumnVO("序号", 100, IRuleConfigVOProperty.SIMPLE_CLASSNAME),
			new RuleConfigColumnVO("规则类型", 100, IRuleConfigVOProperty.CATALOG_NAME),
			new RuleConfigColumnVO("规则子类型", 100, IRuleConfigVOProperty.SUB_CATALOG_NAME),
			new RuleConfigColumnVO("规则详情", 300, IRuleConfigVOProperty.DESCRIPTION),
			new RuleConfigColumnVO("特殊参数", 200, IRuleConfigVOProperty.SPECIAL_PARAMETERS),
			new RuleConfigColumnVO("执行阶段", 65, IRuleConfigVOProperty.EXECUTE_PERIOD_NAME),
			new RuleConfigColumnVO("执行层次", 70, IRuleConfigVOProperty.EXECUTE_LAYER_NAME),
			new RuleConfigColumnVO("备注", 300, IRuleConfigVOProperty.MEMO), });

	public static final List<RuleConfigColumnVO> RULE_MULTI_CONFIG_COLUMNS = Arrays.asList(new RuleConfigColumnVO[] {
			new RuleConfigColumnVO("序号", 150, IRuleConfigVOProperty.SIMPLE_CLASSNAME),
			new RuleConfigColumnVO("规则类型", 80, IRuleConfigVOProperty.CATALOG_NAME),
			new RuleConfigColumnVO("规则子类型", 80, IRuleConfigVOProperty.SUB_CATALOG_NAME),
			new RuleConfigColumnVO("规则详情", 300, IRuleConfigVOProperty.DESCRIPTION),
			new RuleConfigColumnVO("执行阶段", 65, IRuleConfigVOProperty.EXECUTE_PERIOD_NAME),
			new RuleConfigColumnVO("执行层次", 70, IRuleConfigVOProperty.EXECUTE_LAYER_NAME),
			new RuleConfigColumnVO("负责人", 80, IRuleConfigVOProperty.CODER),
			new RuleConfigColumnVO("备注", 150, IRuleConfigVOProperty.MEMO), });

	private RuleConfigConstants() {

	}

}
