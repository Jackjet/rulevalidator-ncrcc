package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import ncmdp.model.Entity;
import ncmdp.model.Relation;

import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IRelation;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;

public class RelationAdaptor implements IRelation{
	private Relation relation;
	
	public RelationAdaptor(Relation relation){
		this.relation = relation;
	}

	@Override
	public IEntity getTarget() {
		return new EntityAdaptor((Entity) relation.getTarget());
	}

	@Override
	public IEntity getSource() {
		return new EntityAdaptor((Entity) relation.getSource());
	}

	@Override
	public IAttribute getSrcAttribute() {
		return new AttributeAdaptor(((Relation)relation).getSrcAttribute());
	}
	
}
