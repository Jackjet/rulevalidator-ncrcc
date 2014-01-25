package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.resparser.resource.MetaResType;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * Ԫ������Դquery���������ü���ҵ��������ƺ�Ԫ������Դ����
 * @author mazhqa
 * @since V1.0
 */
public class MetadataResourceQuery extends AbstractResourceQuery {

	private BusinessComponent businessComponent;
	/**
	 * ���õ�Ԫ������Դ����(ALL)
	 */
	private MetaResType metaResType = MetaResType.ALL;

	public MetaResType getMetaResType() {
		return metaResType;
	}

	public void setMetaResType(MetaResType metaResType) {
		this.metaResType = metaResType;
	}

	public BusinessComponent getBusinessComponent() {
		return businessComponent;
	}

	public void setBusinessComponent(BusinessComponent businessComponent) {
		this.businessComponent = businessComponent;
	}

}
