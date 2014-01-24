package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import ncmdp.model.CanZhao;
import ncmdp.model.Entity;
import ncmdp.model.ValueObject;

import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IReference;

public class ReferenceAdaptor implements IReference {
	
	private final CanZhao reference;
	
	public ReferenceAdaptor(CanZhao reference){
		this.reference = reference;
	}

	@Override
	public boolean isDefault() {
		return reference.isDefault();
	}

	@Override
	public String getName() {
		return reference.getName();
	}

	@Override
	public IEntity getEntity() {
		ValueObject referenceVo = reference.getVo();
		if (referenceVo instanceof Entity) {
			Entity entity = (Entity) referenceVo;
			return new EntityAdaptor(entity);
		}
		return null;
	}

}
