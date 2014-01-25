package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import java.util.HashMap;

import ncmdp.model.Accessor;
import ncmdp.model.Accessor.AccProp;

import com.yonyou.nc.codevalidator.resparser.md.IAccessor;

public class AccessorAdaptor implements IAccessor {
	
	private Accessor accessor;
	
	public AccessorAdaptor(Accessor accessor) {
		this.accessor = accessor;
	}

	@Override
	public String getAccessorType() {
		return accessor.getDisplayName();
	}

	@Override
	public String getAccessorTypeFullName() {
		return accessor.getClsFullName();
	}
	
	@Override
	public String getAccessorWrapperClassName() {
		HashMap<String, AccProp> propmap = accessor.getPropmap();
		if(propmap != null && propmap.containsKey(Accessor.WRAPCLSNAME)) {
			return propmap.get(Accessor.WRAPCLSNAME).getValue();
		}
		return null;
	}
	

}
