package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import java.util.Arrays;
import java.util.List;

/**
 * ���������ĳ����࣬���ڱ��Ļ���
 * @author mazhqa
 * @since V2.8
 */
public class RuleResultConstants {
	
	public static final List<RuleResultColumnVO> RULE_RESULT_COLUMNS = Arrays.asList(
			new RuleResultColumnVO("״̬", 20, IRuleResultVOProperty.RULE_EXECUTE_STATUS),
			new RuleResultColumnVO("ִ�м���", 80, IRuleResultVOProperty.RULE_EXECUTE_LEVEL),
			new RuleResultColumnVO("�����ʶ", 120, IRuleResultVOProperty.RULE_IDENTIFIER),
			new RuleResultColumnVO("ִ�е�Ԫ", 150, IRuleResultVOProperty.EXECUTE_UNIT),
			new RuleResultColumnVO("��������", 180, IRuleResultVOProperty.RULE_CATALOG),
			new RuleResultColumnVO("����ִ����ϸ���", 300, IRuleResultVOProperty.RESULT_NOTE)
			);
	
	private RuleResultConstants() {
		
	}

}
