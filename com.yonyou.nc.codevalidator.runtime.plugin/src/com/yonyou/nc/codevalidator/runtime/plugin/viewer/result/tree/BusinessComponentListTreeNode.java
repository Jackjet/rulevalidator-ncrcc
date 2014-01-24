package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.ICompositeExecuteUnit;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;

public class BusinessComponentListTreeNode extends AbstractFilteredResultTreeNode {

	private BusinessComponent businessComponent;

	public BusinessComponentListTreeNode(BusinessComponent businessComponent) {
		this.businessComponent = businessComponent;
	}

	@Override
	public List<IResultTreeNode> getChildrenNodes() {
		List<IResultTreeNode> result = new ArrayList<IResultTreeNode>();
		if (businessComponent instanceof ICompositeExecuteUnit) {
			ICompositeExecuteUnit compositeExecuteUnit = (ICompositeExecuteUnit) businessComponent;
			List<BusinessComponent> subBusinessComponentList = compositeExecuteUnit.getSubBusinessComponentList();
			for (BusinessComponent businessComponent : subBusinessComponentList) {
				result.add(new BusinessComponentListTreeNode(businessComponent));
			}
		}
		return result;
	}

	@Override
	public boolean hasChildren() {
		return businessComponent instanceof ICompositeExecuteUnit;
	}

	@Override
	public String getDisplayText() {
		return businessComponent.getDisplayBusiCompName();
	}

	@Override
	public String getImageIcon() {
		return "/images/busicomp.png";
	}

	@Override
	protected boolean filterResult(IRuleExecuteResult result) {
		return result.getBusinessComponent().equals(businessComponent);
	}

}
