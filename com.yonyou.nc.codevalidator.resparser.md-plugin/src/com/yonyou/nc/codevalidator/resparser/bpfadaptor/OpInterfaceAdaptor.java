package com.yonyou.nc.codevalidator.resparser.bpfadaptor;

import java.util.ArrayList;
import java.util.List;

import ncmdp.model.Type;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;

import com.yonyou.nc.codevalidator.resparser.bpf.IOpInterface;
import com.yonyou.nc.codevalidator.resparser.bpf.IOperation;
import com.yonyou.nc.codevalidator.resparser.md.IType;
import com.yonyou.nc.codevalidator.resparser.mdadaptor.TypeAdaptor;

public class OpInterfaceAdaptor implements IOpInterface {
	
	private OpInterface opInterface;
	
	public OpInterfaceAdaptor(OpInterface opInterface){
		this.opInterface = opInterface;
	}

	@Override
	public String getID() {
		return opInterface.getId();
	}

	@Override
	public String getVersion() {
		return opInterface.getVersion();
	}

	@Override
	public String getResId() {
		return opInterface.getResid();
	}

	@Override
	public String getInterfaceName() {
		return opInterface.getFullClassName();
	}

	@Override
	public String getImplementationClassName() {
		return opInterface.getImplClsName();
	}

	@Override
	public IType getOwnType() {
		Type type = opInterface.getOwnType();
		return new TypeAdaptor(type);
	}

	@Override
	public String getExtendTag() {
		return opInterface.getExtendTag();
	}

	@Override
	public boolean isSingleton() {
		return opInterface.isSingleton();
	}

	@Override
	public boolean isBusiActivity() {
		return opInterface.isBusiActivity();
	}

	@Override
	public boolean isBusiOperation() {
		return opInterface.isBusiOperation();
	}

	@Override
	public boolean isAuthorization() {
		return opInterface.isAuthorization();
	}

	@Override
	public boolean isRemote() {
		return opInterface.isRemote();
	}

	@Override
	public List<IOperation> getOperations() {
		List<IOperation> result = new ArrayList<IOperation>();
		List<Operation> operations = opInterface.getOperations();
		if(operations != null && operations.size() > 0){
			for (Operation operation : operations) {
				result.add(new OperationAdaptor(operation));
			}
		}
		return result;
	}

	@Override
	public String getDisplayName() {
		return opInterface.getDisplayName();
	}

	@Override
	public String getName() {
		return opInterface.getName();
	}

}
