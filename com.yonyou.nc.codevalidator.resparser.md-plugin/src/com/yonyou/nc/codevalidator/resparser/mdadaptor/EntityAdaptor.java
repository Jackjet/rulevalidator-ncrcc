package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ncmdp.model.AggregationRelation;
import ncmdp.model.Attribute;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;
import ncmdp.model.CanZhao;
import ncmdp.model.Connection;
import ncmdp.model.Entity;
import ncmdp.model.Relation;

import com.yonyou.nc.codevalidator.resparser.md.IAccessor;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IBusiInterface;
import com.yonyou.nc.codevalidator.resparser.md.IEntityTargetConnection;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IReference;
import com.yonyou.nc.codevalidator.resparser.md.IRelation;

public class EntityAdaptor implements IEntity {

	private final Entity entity;

	public EntityAdaptor(Entity entity) {
		this.entity = entity;
	}

	@Override
	public String getId() {
		return entity.getId();
	}

	@Override
	public String getDisplayName() {
		return entity.getDisplayName();
	}

	@Override
	public List<IAttribute> getAttributes() {
		List<IAttribute> result = new ArrayList<IAttribute>();
		List<Attribute> props = entity.getProps();
		if (props != null && props.size() > 0) {
			for (Attribute attribute : props) {
				result.add(new AttributeAdaptor(attribute));
			}
		}
		return result;
	}

	@Override
	public List<IBusiInterface> getBusiInterfaces() {
		List<IBusiInterface> result = new ArrayList<IBusiInterface>();
		List<BusinInterface> busiItfs = entity.getBusiItfs();
		if (busiItfs != null && busiItfs.size() > 0) {
			for (BusinInterface businInterface : busiItfs) {
				result.add(new BusiInterfaceAdaptor(businInterface));
			}
		}
		return result;
	}

	@Override
	public String getFullClassName() {
		return entity.getFullClassName();
	}

	// @Override
	// public Map<String, IAttribute[]> getBusiattrAttrExtendMap() {
	// Map<String, IAttribute[]> result = new HashMap<String, IAttribute[]>();
	// HashMap<String, Attribute[]> busiattrAttrExtendMap =
	// entity.getBusiattrAttrExtendMap();
	// for (Map.Entry<String, Attribute[]> entry :
	// busiattrAttrExtendMap.entrySet()) {
	// String key = entry.getKey();
	// Attribute[] attributesValue = entry.getValue();
	//
	// IAttribute[] newAttributesValue = new IAttribute[attributesValue.length];
	// for (int i = 0; i < newAttributesValue.length; i++) {
	// newAttributesValue[i] = attributesValue[i] == null ? null : new
	// AttributeAdaptor(attributesValue[i]);
	// }
	// result.put(key, newAttributesValue);
	// }
	//
	// return result;
	// }

	@Override
	public Map<String, IAttribute> getBusiInterfaceAttributes(String busiInterfaceName) {
		Map<String, IAttribute> result = new HashMap<String, IAttribute>();
		List<BusinInterface> busiItfs = entity.getBusiItfs();
		BusinInterface currentBusiInterface = null;
		for (BusinInterface businInterface : busiItfs) {
			if (businInterface.getFullClassName().equalsIgnoreCase(busiInterfaceName)) {
				currentBusiInterface = businInterface;
				break;
			}
		}
		if (currentBusiInterface == null) {
			return result;
		}
		Map<String, String> attrIdToNameMap = new HashMap<String, String>();
		for (BusiItfAttr busiItfAttr : currentBusiInterface.getBusiItAttrs()) {
			attrIdToNameMap.put(busiItfAttr.getId(), busiItfAttr.getDisplayName());
		}
		HashMap<String, Attribute[]> busiattrAttrExtendMap = entity.getBusiattrAttrExtendMap();

		for (Map.Entry<String, String> entry : attrIdToNameMap.entrySet()) {
			Attribute[] attributes = busiattrAttrExtendMap.get(entry.getKey());
			result.put(entry.getValue(), attributes == null || attributes[0] == null ? null : new AttributeAdaptor(attributes[0]));
		}
		return result;
	}

	@Override
	public String getFullName() {
		return entity.getGraph().getNameSpace()+"."+entity.getName();
	}

	@Override
	public String getExtendTag() {
		return entity.getExtendTag();
	}

	@Override
	public String getTableName() {
		entity.getSourceConnections();
		entity.getTargetConnections();
		return entity.getTableName();
	}

	@Override
	public IAttribute getKeyAttribute() {
		return new AttributeAdaptor(entity.getKeyAttribute());
	}

	@Override
	public List<IRelation> getSourceRelations() {
		List<Connection> connections = entity.getSourceConnections();
		List<IRelation> relations = new ArrayList<IRelation>();
		for(Connection connection : connections){
			if(connection instanceof Relation){
				relations.add(new RelationAdaptor((Relation)connection));
			}
		}
		return relations;
	}

	@Override
	public List<IRelation> getTargetRelations() {
		List<Connection> connections = entity.getTargetConnections();
		List<IRelation> relations = new ArrayList<IRelation>();
		for(Connection connection : connections){
			if(connection instanceof Relation){
				relations.add(new RelationAdaptor((Relation)connection));
			}
		}
		return relations;
	}

	@Override
	public List<IReference> getReferences() {
		List<IReference> result = new ArrayList<IReference>();
		ArrayList<CanZhao> alCanzhao = entity.getAlCanzhao();
		if(alCanzhao != null && alCanzhao.size() > 0){
			for (CanZhao canzhao : alCanzhao) {
				result.add(new ReferenceAdaptor(canzhao));
			}
		}
		return result;
	}

	@Override
	public IAccessor getAccessor() {
		return new AccessorAdaptor(entity.getAccess());
	}

	@Override
	public List<IEntityTargetConnection> getAggreConnections() {
		List<Connection> targetConnections = entity.getSourceConnections();
		List<IEntityTargetConnection> result = new ArrayList<IEntityTargetConnection>();
		for (Connection connection : targetConnections) {
			if (connection instanceof AggregationRelation && connection.getTarget() instanceof Entity) {
				AggregationRelation aggRelation = (AggregationRelation) connection;
				result.add(new EntityTargetConnectionAdaptor(aggRelation));
			}
		}
		return result;
	}

}
