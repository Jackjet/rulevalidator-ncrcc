package com.yonyou.nc.codevalidator.resparser.bpfadaptor;

import ncmdp.model.Type;
import ncmdp.model.activity.Parameter;

import com.yonyou.nc.codevalidator.resparser.bpf.IParameter;
import com.yonyou.nc.codevalidator.resparser.md.IType;
import com.yonyou.nc.codevalidator.resparser.mdadaptor.TypeAdaptor;

public class ParameterAdaptor implements IParameter {
	
	private Parameter parameter;
	
	public ParameterAdaptor(Parameter parameter){
		this.parameter = parameter;
	}

	@Override
	public String getName() {
		return parameter.getName();
	}

	@Override
	public String getDisplayName() {
		return parameter.getDisplayName();
	}

	@Override
	public String getDescription() {
		return parameter.getDesc();
	}

	@Override
	public boolean isAggVO() {
		return parameter.isAggVO();
	}

	@Override
	public String getHelp() {
		return parameter.getHelp();
	}

	@Override
	public String getTypeStyle() {
		return parameter.getTypeStyle();
	}

	@Override
	public IType getParaType() {
		Type type = parameter.getParaType();
		return new TypeAdaptor(type);
	}

	@Override
	public String getParamDefClassName() {
		return parameter.getParamDefClassname();
	}

}
