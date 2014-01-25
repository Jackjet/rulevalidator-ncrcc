package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;

/**
 * ������ʾ������ڵ���Ϣ�ĳ���ڵ�����
 * @author mazhqa
 * @since V1.0
 */
public interface IResultTreeNode {
	
	/**
	 * �ڵ���ʾ������ʱ��Ӧ��ͼ��
	 * @return
	 */
	String getImageIcon();
	
	/**
	 * ���ڵ���ʾ�ı�
	 * @return
	 */
	String getDisplayText();
	
	/**
	 * @return
	 */
	List<IResultTreeNode> actualGetChildrenNode();
	
	/**
	 * �Ƿ����ӽڵ�
	 * @return
	 */
	boolean hasChildren();
	
	void setSessionRuleExecuteResult(SessionRuleExecuteResult sessionRuleExecResult);
	
	SessionRuleExecuteResult getSessionRuleExecuteResult();

	/**
	 * ��ö�Ӧ����ִ�н���б�
	 * @return
	 */
	List<IRuleExecuteResult> getRuleExecResultList();

}
