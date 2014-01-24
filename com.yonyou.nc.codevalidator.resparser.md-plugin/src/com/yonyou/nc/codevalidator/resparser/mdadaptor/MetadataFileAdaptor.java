package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import java.util.ArrayList;
import java.util.List;

import ncmdp.model.Cell;
import ncmdp.model.Entity;
import ncmdp.model.JGraph;

import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;

public class MetadataFileAdaptor implements IMetadataFile {

	private JGraph jgraph;

	public MetadataFileAdaptor(JGraph jgraph) {
		this.jgraph = jgraph;
	}

	@Override
	public IEntity getMainEntity() {
		Entity mainEntity = jgraph.getMainEntity();
		return mainEntity == null ? null : new EntityAdaptor(mainEntity);
	}

	@Override
	public List<IEntity> getAllEntities() {
		List<IEntity> result = new ArrayList<IEntity>();
		List<Cell> entities = jgraph.getCellsByClass(Entity.class);
		if (entities != null && entities.size() > 0) {
			for (Cell entity : entities) {
				result.add(new EntityAdaptor((Entity) entity));
			}
		}
		return result;
	}

	@Override
	public String getNamespace() {
		return jgraph.getNameSpace();
	}

	@Override
	public String getCodeStyle() {
		return jgraph.getGenCodeStyle();
	}

	@Override
	public String getResId() {
		return jgraph.getResid();
	}

	@Override
	public String getResModuleName() {
		return jgraph.getResModuleName();
	}

	@Override
	public String getName() {
		return jgraph.getName();
	}

	@Override
	public boolean isPreLoad() {
		return jgraph.isPreLoad();
	}

	@Override
	public String getOwnModule() {
		return jgraph.getOwnModule();
	}

	@Override
	public String getDisplayName() {
		return jgraph.getDisplayName();
	}

	@Override
	public String getHyName() {
		return jgraph.getCreateIndustry();
	}

	@Override
	public boolean isIncrementDevelopMode() {
		return jgraph.isbizmodel();
	}

	@Override
	public String getExtendTag() {
		return jgraph.getExtendTag();
	}
	
	@Override
	public boolean isBill() {
		//TODO: 不确定是否该这么做来判断是否单据
		return getMainEntity().getExtendTag().indexOf("BDMODE") == -1;
	}
	
	@Override
	public boolean isDoc() {
		// TODO 不确定是否该这么做来判断是否档案
		return getMainEntity().getExtendTag().indexOf("BDMODE") != -1;
	}
	
	@Override
	public boolean containsMainEntity() {
		return getMainEntity() != null;
	}

}
