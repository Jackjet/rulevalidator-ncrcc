package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import ncmdp.model.Type;

import com.yonyou.nc.codevalidator.resparser.md.IType;

public class TypeAdaptor implements IType {
	
	private final Type type;
	
	public TypeAdaptor(Type type) {
		this.type = type;
	}

	@Override
	public String getDbType() {
		return type.getDbType();
	}

	@Override
	public String getDisplayName() {
		return type.getDisplayName();
	}

	@Override
	public String getLength() {
		return type.getLength();
	}

	@Override
	public String getName() {
		return type.getName();
	}

	@Override
	public String getPrecise() {
		return type.getPrecise();
	}

	@Override
	public String getTypeId() {
		return type.getTypeId();
	}

}
