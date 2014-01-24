package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.resource.MetaResType;

/**
 * 抽象的元数据查询规则定义，仅检查BPF文件
 * @author mazhqa
 * @since V2.1
 */
public abstract class AbstractMetaProcessResourceRuleDefinition extends AbstractMetadataResourceRuleDefinition {

	protected final MetaResType getMetaResType(){
		return MetaResType.BPF;
	}

}
