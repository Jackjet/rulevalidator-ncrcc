package com.yonyou.nc.codevalidator.plugin.domain.mm.md.itfmapping;

import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMetadataUtils;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IBusiInterface;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.MDRuleConstants;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 行号的接口实现和属性映射(非单据时可选)
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, description = "行号的接口实现和属性映射(非单据时可选)", relatedIssueId = "111", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "lijbe", solution = "支持行号功能的单据需要实现IRowNo接口,并映射属性", memo = "支持行号功能的单据需要实现IRowNo接口,并映射属性")
public class TestCase00111 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

		for (MetadataResource metadataResource : resources) {
			IMetadataFile metaDataFile = metadataResource.getMetadataFile();
			List<IEntity> entities = metaDataFile.getAllEntities();

			for (IEntity entity : entities) {

				List<IBusiInterface> itfList = entity.getBusiInterfaces();
				if (null == itfList
						|| itfList.size() < 1
						|| !MmMetadataUtils.getBusiInterfaceNames(itfList)
								.contains(MDRuleConstants.ROWNO_NAME)) {
					continue;

				}
				if (MmMetadataUtils.getBusiInterfaceNames(itfList).contains(
						MDRuleConstants.ROWNO_NAME)) {
					if (!MmMetadataUtils
							.isMappingProperties(
									entity.getBusiInterfaceAttributes(MDRuleConstants.ROWNO_NAME),
									new String[] { MmMDConstants.ROW_INFO })) {
						result.addResultElement(metadataResource
								.getResourcePath(), String.format(
								"元数据【 %s 】, 接口属性映射: %s ,未配置行号对应属性\n", entity.getFullName(),
								MDRuleConstants.ROWNO_NAME));
					}
				}

			}
		}
		return result;
	}

}
