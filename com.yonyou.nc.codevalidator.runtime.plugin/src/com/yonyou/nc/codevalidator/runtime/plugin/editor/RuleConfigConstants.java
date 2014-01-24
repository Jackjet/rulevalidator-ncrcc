package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import java.util.Arrays;
import java.util.List;

/**
 * �������õĳ�����
 * 
 * @author mazhqa
 * @since V2.3
 */
public final class RuleConfigConstants {

	public static final String EXECUTE_LEVEL_FIELDNAME = "ִ�м���";

	public static final List<RuleConfigColumnVO> RULE_CONFIG_MAIN_COLUMNS = Arrays.asList(new RuleConfigColumnVO[] {
			new RuleConfigColumnVO(EXECUTE_LEVEL_FIELDNAME, 100, IRuleConfigVOProperty.EXECUTE_LEVEL),
			new RuleConfigColumnVO("���", 100, IRuleConfigVOProperty.SIMPLE_CLASSNAME),
			new RuleConfigColumnVO("��������", 100, IRuleConfigVOProperty.CATALOG_NAME),
			new RuleConfigColumnVO("����������", 100, IRuleConfigVOProperty.SUB_CATALOG_NAME),
			new RuleConfigColumnVO("��������", 300, IRuleConfigVOProperty.DESCRIPTION),
			new RuleConfigColumnVO("�������", 200, IRuleConfigVOProperty.SPECIAL_PARAMETERS),
			new RuleConfigColumnVO("ִ�н׶�", 65, IRuleConfigVOProperty.EXECUTE_PERIOD_NAME),
			new RuleConfigColumnVO("ִ�в��", 70, IRuleConfigVOProperty.EXECUTE_LAYER_NAME),
			new RuleConfigColumnVO("��ע", 300, IRuleConfigVOProperty.MEMO), });

	public static final List<RuleConfigColumnVO> RULE_MULTI_CONFIG_COLUMNS = Arrays.asList(new RuleConfigColumnVO[] {
			new RuleConfigColumnVO("���", 150, IRuleConfigVOProperty.SIMPLE_CLASSNAME),
			new RuleConfigColumnVO("��������", 80, IRuleConfigVOProperty.CATALOG_NAME),
			new RuleConfigColumnVO("����������", 80, IRuleConfigVOProperty.SUB_CATALOG_NAME),
			new RuleConfigColumnVO("��������", 300, IRuleConfigVOProperty.DESCRIPTION),
			new RuleConfigColumnVO("ִ�н׶�", 65, IRuleConfigVOProperty.EXECUTE_PERIOD_NAME),
			new RuleConfigColumnVO("ִ�в��", 70, IRuleConfigVOProperty.EXECUTE_LAYER_NAME),
			new RuleConfigColumnVO("������", 80, IRuleConfigVOProperty.CODER),
			new RuleConfigColumnVO("��ע", 150, IRuleConfigVOProperty.MEMO), });

	private RuleConfigConstants() {

	}

}
