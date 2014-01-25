package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import java.util.ArrayList;
import java.util.List;

import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;

import com.yonyou.nc.codevalidator.resparser.md.IBusiInterface;
import com.yonyou.nc.codevalidator.resparser.md.IBusiInterfaceAttribute;

public class BusiInterfaceAdaptor implements IBusiInterface {

	private BusinInterface businInterface;

	public BusiInterfaceAdaptor(BusinInterface businInterface) {
		this.businInterface = businInterface;
	}

	@Override
	public String getFullClassName() {
		return businInterface.getFullClassName();
	}

	@Override
	public List<IBusiInterfaceAttribute> getBusiInterfaceAttributes() {
		List<IBusiInterfaceAttribute> result = new ArrayList<IBusiInterfaceAttribute>();
		List<BusiItfAttr> busiItAttrs = businInterface.getBusiItAttrs();
		if (busiItAttrs != null && busiItAttrs.size() > 0) {
			for (BusiItfAttr busiItfAttr : busiItAttrs) {
				result.add(new BusiInterfaceAttributeAdaptor(busiItfAttr));
			}
		}
		return result;
	}

}
