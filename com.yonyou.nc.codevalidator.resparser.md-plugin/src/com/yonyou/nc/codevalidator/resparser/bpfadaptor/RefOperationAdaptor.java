package com.yonyou.nc.codevalidator.resparser.bpfadaptor;

import java.util.List;

import ncmdp.model.activity.RefOperation;

import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.bpf.IOpInterface;
import com.yonyou.nc.codevalidator.resparser.bpf.IOperation;
import com.yonyou.nc.codevalidator.resparser.bpf.IRefOperation;

public class RefOperationAdaptor implements IRefOperation {
	
	private final RefOperation refOperation;
	private final IMetaProcessFile metaProcessFile;
	
	public RefOperationAdaptor(RefOperation refOperation, IMetaProcessFile metaProcessFile){
		this.refOperation = refOperation;
		this.metaProcessFile = metaProcessFile;
	}

	@Override
	public String getID() {
		return refOperation.getId();
	}

	@Override
	public String getName() {
		return refOperation.getName();
	}

	@Override
	public String getDisplayName() {
		return refOperation.getDisplayName();
	}

	@Override
	public IOpInterface getRefOptInterface() {
		List<IOpInterface> opInterfaces = metaProcessFile.getOpInterfaces();
		for (IOpInterface opInterface : opInterfaces) {
			if(opInterface.getID().equals(refOperation.getOpItfID())){
				return opInterface;
			}
		}
		return null;
	}

	@Override
	public IOperation getRefOperation() {
		IOpInterface optInterfaceID = getRefOptInterface();
		List<IOperation> operations = optInterfaceID.getOperations();
		for (IOperation operation : operations) {
			if(operation.getID().equals(refOperation.getId())){
				return operation;
			}
		}
		return null;
	}

}
