package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.resource.MetaResType;

/**
 * �����Ԫ���ݲ�ѯ�����壬�����BPF�ļ�
 * @author mazhqa
 * @since V2.1
 */
public abstract class AbstractMetaProcessResourceRuleDefinition extends AbstractMetadataResourceRuleDefinition {

	protected final MetaResType getMetaResType(){
		return MetaResType.BPF;
	}

}
