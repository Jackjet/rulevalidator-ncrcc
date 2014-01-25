package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

/**
 * 单独的修改层次条件树节点
 * @author mazhqa
 * @since V2.6
 */
public class SingleRepairLevelConditionTreeNode extends AbstractSingleConditionTreeNode {
	
	private final RepairLevel repairLevel;
	
	public SingleRepairLevelConditionTreeNode(RepairLevel repairLevel) {
		super();
		this.repairLevel = repairLevel;
	}

	@Override
	public String getDisplayText() {
		return repairLevel.getName();
	}

	@Override
	public String getImageIcon() {
		return null;
	}

	@Override
	public List<IConfigTreeNode> actualGetChildrenNode() {
		return Collections.emptyList();
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	protected boolean fulfillCondition(RuleDefinitionAnnotationVO ruleDefinitionVO) {
		return repairLevel.equals(ruleDefinitionVO.getRepairLevel());
	}

}
