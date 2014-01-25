package com.yonyou.nc.codevalidator.rule.vo;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleConfigContext;

/**
 * ����Ĺ������ý����������ʵ��ִ��ʱ����ִ�еĹ�������
 * @author mazhqa
 * @since V2.6
 */
public interface IRuleCheckConfiguration extends Comparable<IRuleCheckConfiguration>{
	
	int GLOBAL_PRIORITY = 10;
	int MODULE_PRIORITY = 20;
	int BUSICOMP_PRIORITY = 30;

	CommonParamConfiguration getCommonParamConfiguration();

	List<RuleItemConfigVO> getRuleItemConfigVoList();
	
	/**
	 * �õ�merge�����ȼ������ںϲ�����ʱ�Ը����ȼ��Ĺ�������Ϊ׼
	 * @return
	 */
	int getPriority();
	
	/**
	 * ���ù���ִ�е����ȼ�
	 * @param priority
	 */
	void setPriority(int priority);
	
	/**
	 * �����������õ�����ת���ɶ�Ӧ����������
	 * @return
	 */
	List<IRuleConfigContext> toRuleConfigContexts();
	
	/**
	 * �ϲ��������ã������ͬ���Ա������е���������
	 * @param ruleCheckConfiguration
	 */
	void addRuleCheckConfiguration(IRuleCheckConfiguration ruleCheckConfiguration);
	
}
