package com.yonyou.nc.codevalidator.plugin.domain.mm.md.refinfo;

import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.IReference;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * <li>元数据实体在作为参照类型实体时，需要在主实体扩展标签属性加上DOC标签</li>
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA,
		description = "元数据实体在作为参照类型实体时，需要在主实体扩展标签属性加上DOC标签", solution = "元数据实体在作为参照类型实体时，需要在主实体扩展标签属性加上DOC标签",
		relatedIssueId = "72", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "lijbe")
public class TestCase00072 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		String extendTag = "";
		for (MetadataResource metadataResource : resources) {
			IMetadataFile metaDataFile = metadataResource.getMetadataFile();
			if (MMValueCheck.isEmpty(metaDataFile.getMainEntity())) {
				continue;
			}
			// 主实体扩展标签
			extendTag = metaDataFile.getMainEntity().getExtendTag();

			// 主实体提供的参照方式的集合
			List<IReference> referenceList = metaDataFile.getMainEntity().getReferences();

			if (null != referenceList && referenceList.size() > 0 && !extendTag.contains(MmMDConstants.EXTEND_TAG_DOC)) {
				result.addResultElement(metadataResource.getResourcePath(), "元数据【"
						+ metaDataFile.getMainEntity().getFullName() + "】在作为参照类型实体时，需要在主实体扩展标签属性加上DOC标签\n");
			}
		}
		return result;
	}

}
