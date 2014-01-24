package com.yonyou.nc.codevalidator.resparser.defaultrule;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.MetadataResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetaResType;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

/**
 * 抽象的元数据查询规则定义，默认检查BMF文件，如果仅检查BPF或两者都检查，覆盖getMetaResType方法
 * @author mazhqa
 * @since V1.0
 */
public abstract class AbstractMetadataResourceRuleDefinition extends AbstractRuleDefinition {

	@Override
	public final IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		MetadataResourceQuery metadataResourceQuery = new MetadataResourceQuery();
		metadataResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		metadataResourceQuery.setMetaResType(getMetaResType());
		List<MetadataResource> resources = ResourceManagerFacade.getResource(metadataResourceQuery);
		return processResources(resources, ruleExecContext);
	}

	protected abstract IRuleExecuteResult processResources(List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException;
	
	protected MetaResType getMetaResType(){
		return MetaResType.BMF;
	}

	/**
	 * 该元数据实体中是否包含子实体
	 * @param metadataFile
	 * @return
	 */
	protected boolean containMultiEntity(IMetadataFile metadataFile) {
		return metadataFile.getAllEntities().size() > 1;
	}
	
}
