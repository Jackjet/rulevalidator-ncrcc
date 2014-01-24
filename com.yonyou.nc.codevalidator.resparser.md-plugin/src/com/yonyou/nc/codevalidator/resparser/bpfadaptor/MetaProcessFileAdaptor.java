package com.yonyou.nc.codevalidator.resparser.bpfadaptor;

import java.util.ArrayList;
import java.util.List;

import ncmdp.model.Cell;
import ncmdp.model.JGraph;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.OpInterface;

import com.yonyou.nc.codevalidator.resparser.bpf.IBusiActivity;
import com.yonyou.nc.codevalidator.resparser.bpf.IBusiOperator;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.bpf.IOpInterface;

public class MetaProcessFileAdaptor implements IMetaProcessFile {
	
	private JGraph jgraph;

	public MetaProcessFileAdaptor(JGraph jgraph) {
		this.jgraph = jgraph;
	}

	@Override
	public List<IBusiOperator> getBusiOperators() {
		List<IBusiOperator> result = new ArrayList<IBusiOperator>();
		List<Cell> cells = jgraph.getCellsByClass(BusiOperation.class);
		if(cells != null && cells.size() > 0){
			for (Cell cell : cells) {
				result.add(new BusiOperatorAdaptor((BusiOperation) cell, this));
			}
		}
		return result;
	}

	@Override
	public String getName() {
		return jgraph.getName();
	}

	@Override
	public String getDisplayName() {
		return jgraph.getDisplayName();
	}

	@Override
	public String getOwnModule() {
		return jgraph.getOwnModule();
	}

	@Override
	public String getNameSpace() {
		return jgraph.getNameSpace();
	}

	@Override
	public String getResModuleName() {
		return jgraph.getResModuleName();
	}

	@Override
	public String getResId() {
		return jgraph.getResid();
	}

	@Override
	public String getIndustry() {
		return jgraph.getIndustry().getName();
	}

	@Override
	public String getID() {
		return jgraph.getId();
	}

	@Override
	public List<IOpInterface> getOpInterfaces() {
		List<IOpInterface> result = new ArrayList<IOpInterface>();
		List<Cell> cells = jgraph.getCellsByClass(OpInterface.class);
		if(cells != null && cells.size() > 0){
			for (Cell cell : cells) {
				result.add(new OpInterfaceAdaptor((OpInterface) cell));
			}
		}
		return result;
	}

	@Override
	public String getExtendTag() {
		return jgraph.getExtendTag();
	}

	@Override
	public boolean isPreLoad() {
		return jgraph.isPreLoad();
	}

	@Override
	public boolean isIndustryIncrease() {
		return jgraph.isIndustryIncrease();
	}

	@Override
	public List<IBusiActivity> getBusiActivities() {
		List<IBusiActivity> result = new ArrayList<IBusiActivity>();
		List<Cell> cells = jgraph.getCellsByClass(BusiActivity.class);
		if(cells != null &&  cells.size() > 0){
			for (Cell cell : cells) {
				result.add(new BusiActivityAdaptor((BusiActivity)cell, this));
			}
		}
		return result;
	}

}
