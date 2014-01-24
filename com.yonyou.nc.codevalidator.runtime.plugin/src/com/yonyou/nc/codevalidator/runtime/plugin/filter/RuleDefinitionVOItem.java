package com.yonyou.nc.codevalidator.runtime.plugin.filter;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

public class RuleDefinitionVOItem implements IFilteredItem {

	private final RuleDefinitionAnnotationVO ruleDefinitionVo;

	public RuleDefinitionVOItem(RuleDefinitionAnnotationVO ruleDefinitionVo) {
		this.ruleDefinitionVo = ruleDefinitionVo;
	}

	public RuleDefinitionAnnotationVO getRuleDefinitionVO() {
		return ruleDefinitionVo;
	}

	@Override
	public int compareTo(IFilteredItem o) {
		if (o instanceof RuleDefinitionVOItem) {
			RuleDefinitionVOItem otherItem = (RuleDefinitionVOItem) o;
			return ruleDefinitionVo.compareTo(otherItem.ruleDefinitionVo);
		}
		return 0;
	}

	@Override
	public String getItemText() {
		return ruleDefinitionVo.getDescription();
	}

	@Override
	public String getItemDetailText() {
		return ruleDefinitionVo.getMemo();
	}

	@Override
	public String getImagePath() {
		return null;
	}

	@Override
	public String getDetailImagePath() {
		return null;
	}

	@Override
	public String toString() {
		return String.format("%s - %s", ruleDefinitionVo.getRuleDefinitionIdentifier(), ruleDefinitionVo.getDescription());
	}

	@Override
	public String getIdentifier() {
		return ruleDefinitionVo.getRuleDefinitionIdentifier();
	}

}
