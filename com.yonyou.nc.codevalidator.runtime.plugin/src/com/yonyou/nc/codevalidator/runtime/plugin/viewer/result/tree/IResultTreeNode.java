package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;

/**
 * 用于显示结果树节点信息的抽象节点配置
 * @author mazhqa
 * @since V1.0
 */
public interface IResultTreeNode {
	
	/**
	 * 节点显示在树上时对应的图标
	 * @return
	 */
	String getImageIcon();
	
	/**
	 * 树节点显示文本
	 * @return
	 */
	String getDisplayText();
	
	/**
	 * @return
	 */
	List<IResultTreeNode> actualGetChildrenNode();
	
	/**
	 * 是否有子节点
	 * @return
	 */
	boolean hasChildren();
	
	void setSessionRuleExecuteResult(SessionRuleExecuteResult sessionRuleExecResult);
	
	SessionRuleExecuteResult getSessionRuleExecuteResult();

	/**
	 * 获得对应规则执行结果列表
	 * @return
	 */
	List<IRuleExecuteResult> getRuleExecResultList();

}
