package com.yonyou.nc.codevalidator.rule;

/**
 * ����ִ�еĽ��������ִ�н���а������µ���Ϣ��
 * <li>����ִ���Ƿ�ɹ�</li>
 * <li>����ִ�н���ͱ�ע</li>
 * <li>�����ʶ��</li>
 * <li>����ִ��������</li>
 * @author mazhqa
 * @since V1.O
 */
/**
 * @author mazhqa
 *
 */
public interface IRuleExecuteResult {
	
//	/**
//	 * ����ִ���Ƿ�ɹ�
//	 * @return
//	 * @see #getRuleExecuteStatus()
//	 */
//	@Deprecated
//	boolean isSuccess();
//	
	/**
	 * ����ִ�еĽ��״̬
	 * @return
	 */
	RuleExecuteStatus getRuleExecuteStatus();
	
	/**
	 * �������н��
	 * @return
	 */
	String getResult();
	
	/**
	 * ���н����ϸ��Ϣ����ע
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
	 * �õ�����ִ�е������ģ���ÿ��ִ�к���Զ�����set��ֵ����
	 * <p>
	 * ��������ִ�е�������������ʾִ�еĹ�����Ϣ
	 * @return
	 */
	IRuleExecuteContext getRuleExecuteContext();
	
	void setRuleExecuteContext(IRuleExecuteContext ruleExecuteContext);
	
	/**
	 * �õ��ý������ʱ���ڵ�ҵ�����
	 * @return
	 */
	BusinessComponent getBusinessComponent();
	
	void setBusinessComponent(BusinessComponent businessComponent);
}
