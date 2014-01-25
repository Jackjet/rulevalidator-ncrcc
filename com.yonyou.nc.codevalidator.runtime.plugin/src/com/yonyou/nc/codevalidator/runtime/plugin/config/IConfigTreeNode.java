package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

/**
 * ���öԻ����е����ڵ㣬ÿ��ֵ��Ӧһ����������ڵ�
 * @author mazhqa
 * @since V2.6
 */
public interface IConfigTreeNode {
	
	/**
	 * �ڵ����ʾ����
	 * @return
	 */
	String getDisplayText();

	/**
	 * �ڵ�ͼ��
	 * @return
	 */
	String getImageIcon();

	/**
	 * �õ��ӽڵ�
	 * @return
	 */
	List<IConfigTreeNode> actualGetChildrenNode();

	/**
	 * �Ƿ����ӽڵ�
	 * @return
	 */
	boolean hasChildren();

	/**
	 * ���ݹ���ִ�е������ĶԹ���ļ��Ͻ��й���
	 * @param filterContext
	 * @return
	 */
	List<RuleDefinitionAnnotationVO> filterRuleDefinitions(RuleDefinitionFilterContext filterContext);
}
