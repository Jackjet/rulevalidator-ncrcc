package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.resparser.resource.MetaResType;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * 元数据资源query，可以设置检查的业务组件名称和元数据资源类型
 * @author mazhqa
 * @since V1.0
 */
public class MetadataResourceQuery extends AbstractResourceQuery {

	private BusinessComponent businessComponent;
	/**
	 * 设置的元数据资源类型(ALL)
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
