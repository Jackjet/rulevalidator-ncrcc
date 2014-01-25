package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import ncmdp.model.BusiItfAttr;

import com.yonyou.nc.codevalidator.resparser.md.IBusiInterfaceAttribute;

public class BusiInterfaceAttributeAdaptor implements IBusiInterfaceAttribute {

	private BusiItfAttr busiItfAttr;

	public BusiInterfaceAttributeAdaptor(BusiItfAttr busiItfAttr) {
		this.busiItfAttr = busiItfAttr;
	}

	@Override
	public String getId() {
		return busiItfAttr.getId();
	}

	@Override
	public String getName() {
		return busiItfAttr.getName();
	}

}
