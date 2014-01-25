package com.yonyou.nc.codevalidator.resparser.bpfadaptor;

import java.util.ArrayList;
import java.util.List;

import ncmdp.model.Type;
import ncmdp.model.activity.Operation;
import ncmdp.model.activity.Parameter;

import com.yonyou.nc.codevalidator.resparser.bpf.IOperation;
import com.yonyou.nc.codevalidator.resparser.bpf.IParameter;
import com.yonyou.nc.codevalidator.resparser.md.IType;
import com.yonyou.nc.codevalidator.resparser.mdadaptor.TypeAdaptor;

public class OperationAdaptor implements IOperation {
	
	private Operation operation;
	
	public OperationAdaptor(Operation operation){
		this.operation = operation;
	}

	@Override
	public String getID() {
		return operation.getId();
	}

	@Override
	public String getName() {
		return operation.getName();
	}

	@Override
	public String getDisplayName() {
		return operation.getDisplayName();
	}

	@Override
	public String getDescription() {
		return operation.getDescription();
	}

	@Override
	public String getTypeStyle() {
		return operation.getTypeStyle();
	}

	@Override
	public String getTransKind() {
		return operation.getTransKind();
	}

	@Override
	public String getVisibility() {
		return operation.getVisibility();
	}

	@Override
	public String getMethodException() {
		return operation.getMethodException();
	}

	@Override
	public IType getReturnType() {
		Type returnType = operation.getReturnType();
		return new TypeAdaptor(returnType);
	}

	@Override
	public boolean isBusiActivity() {
		return operation.isBusiActivity();
	}

	@Override
	public boolean isAggVOReturn() {
		return operation.isAggVOReturn();
	}

	@Override
	public List<IParameter> getParameters() {
		List<IParameter> result = new ArrayList<IParameter>();
		ArrayList<Parameter> paras = operation.getParas();
		if(paras != null && paras.size() > 0){
			for (Parameter parameter : paras) {
				result.add(new ParameterAdaptor(parameter));
			}
		}
		return result;
	}

}
