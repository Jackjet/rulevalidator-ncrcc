package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import ncmdp.model.Attribute;

import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IType;

public class AttributeAdaptor implements IAttribute {

	private Attribute attribute;

	public AttributeAdaptor(Attribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public String getAccessStrategy() {
		return attribute.getAccessStrategy();
	}

	@Override
	public String getTypeStyle() {
		return attribute.getTypeStyle();
	}

	@Override
	public String getDisplayName() {
		return attribute.getDisplayName();
	}

	@Override
	public String getFieldName() {
		return attribute.getFieldName();
	}

	@Override
	public String getName() {
		return attribute.getName();
	}

	@Override
	public String getFieldType() {
		return attribute.getFieldType();
	}

	@Override
	public String getLength() {
		return attribute.getLength();
	}

	@Override
	public IType getType() {
		return new TypeAdaptor(attribute.getType());
	}

	@Override
	public boolean isAccessPower() {
		return attribute.isAccessPower();
	}

	@Override
	public String getAccessPowerGroup() {
		return attribute.getAccessPowerGroup();
	}

	@Override
	public String getRefModuleName() {
		return attribute.getRefModuleName();
	}

	@Override
	public boolean isDynamic() {
		return attribute.getIsDynamicAttr();
	}

}
