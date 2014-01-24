package com.yonyou.nc.codevalidator.resparser.bpfadaptor;

import java.util.ArrayList;
import java.util.List;

import ncmdp.model.Type;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.RefOperation;

import com.yonyou.nc.codevalidator.resparser.bpf.IBusiOperator;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.bpf.IRefOperation;
import com.yonyou.nc.codevalidator.resparser.md.IType;
import com.yonyou.nc.codevalidator.resparser.mdadaptor.TypeAdaptor;

public class BusiOperatorAdaptor implements IBusiOperator {
	
	private final IMetaProcessFile metaProcessFile;
	private final BusiOperation busiOperation;
	
	public BusiOperatorAdaptor(BusiOperation busiOperation, IMetaProcessFile metaProcessFile){
		this.busiOperation = busiOperation;
		this.metaProcessFile = metaProcessFile;
	}

	@Override
	public String getName() {
		return busiOperation.getName();
	}

	@Override
	public String getDisplayName() {
		return busiOperation.getDisplayName();
	}

	@Override
	public boolean isAuthorization() {
		return busiOperation.isAuthorization();
	}

	@Override
	public boolean isBusiActivity() {
		return busiOperation.isBusiActivity();
	}

	@Override
	public boolean isNeedLog() {
		return busiOperation.isNeedLog();
	}

	@Override
	public String getLogType() {
		return busiOperation.getLogType();
	}

	@Override
	public String getID() {
		return busiOperation.getId();
	}

	@Override
	public String getResId() {
		return busiOperation.getResid();
	}

	@Override
	public String getExtendTag() {
		return busiOperation.getExtendTag();
	}

	@Override
	public String getDescription() {
		return busiOperation.getDesc();
	}

	@Override
	public IType getOwnType() {
		Type ownType = busiOperation.getOwnType();
		return new TypeAdaptor(ownType);
	}

	@Override
	public List<IRefOperation> getRefOperations() {
		List<IRefOperation> result = new ArrayList<IRefOperation>();
		List<RefOperation> refOperations = busiOperation.getOperations();
		for (RefOperation refOperation : refOperations) {
			result.add(new RefOperationAdaptor(refOperation, metaProcessFile));
		}
		return result;
	}

}
