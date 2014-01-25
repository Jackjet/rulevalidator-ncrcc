package com.yonyou.nc.codevalidator.resparser.bpfadaptor;

import java.util.ArrayList;
import java.util.List;

import ncmdp.model.Type;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.BusiOperation;

import com.yonyou.nc.codevalidator.resparser.bpf.IBusiActivity;
import com.yonyou.nc.codevalidator.resparser.bpf.IBusiOperator;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.md.IType;
import com.yonyou.nc.codevalidator.resparser.mdadaptor.TypeAdaptor;

public class BusiActivityAdaptor implements IBusiActivity {
	
	private final IMetaProcessFile metaProcessFile;
	private final BusiActivity busiActivity;
	
	public BusiActivityAdaptor(BusiActivity busiActivity, IMetaProcessFile metaProcessFile){
		this.busiActivity = busiActivity;
		this.metaProcessFile = metaProcessFile;
	}

	@Override
	public String getVersion() {
		return busiActivity.getVersion();
	}

	@Override
	public String getResId() {
		return busiActivity.getResid();
	}

	@Override
	public String getName() {
		return busiActivity.getName();
	}

	@Override
	public String getDisplayName() {
		return busiActivity.getDisplayName();
	}

	@Override
	public String getDescription() {
		return busiActivity.getDesc();
	}

	@Override
	public String getExtendTag() {
		return busiActivity.getExtendTag();
	}

	@Override
	public IType getOwnType() {
		Type ownType = busiActivity.getOwnType();
		return ownType == null ? null : new TypeAdaptor(ownType);
	}

	@Override
	public boolean isAuthorization() {
		return busiActivity.isAuthorization();
	}

	@Override
	public boolean isService() {
		return busiActivity.isService();
	}

	@Override
	public List<IBusiOperator> getBusiOperators() {
		List<IBusiOperator> result = new ArrayList<IBusiOperator>();
		List<BusiOperation> busiOp = busiActivity.getBusiOp();
		if(busiOp != null && busiOp.size() > 0){
			for (BusiOperation busiOperation : busiOp) {
				result.add(new BusiOperatorAdaptor(busiOperation, metaProcessFile));
			}
		}
		return result;
	}

}
