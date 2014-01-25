package com.yonyou.nc.codevalidator.plugin.domain.mm.md.itfmapping;

import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMetadataUtils;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IBusiInterface;
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
 * 元数据实体必须实现IOrgInfo接口
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, description = "元数据实体必须实现IOrgInfo接口", solution = "元数据实体必须实现IOrgInfo接口", relatedIssueId = "106", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "lijbe")
public class TestCase00106 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

		for (MetadataResource metadataResource : resources) {
			IMetadataFile metaDataFile = metadataResource.getMetadataFile();
			if(MMValueCheck.isEmpty(metaDataFile.getMainEntity())){
				continue;
			}
			List<IBusiInterface> itfList = metaDataFile.getMainEntity()
					.getBusiInterfaces();
			if (null == itfList
					|| itfList.size() < 1
					|| !MmMetadataUtils.getBusiInterfaceNames(itfList)
							.contains(MDRuleConstants.ORGINFO_NAME)) {
				result.addResultElement(metadataResource.getResourcePath(),
						"元数据【" + metaDataFile.getMainEntity().getFullName()
								+ "】实体必须实现IOrgInfo接口，请检查元数据中实体是否实现了该接口\n");
				continue;
			}
			if (MmMetadataUtils.getBusiInterfaceNames(itfList)
					.contains(MDRuleConstants.ORGINFO_NAME)) {
				if (!MmMetadataUtils.isMappingProperties(
						metaDataFile.getMainEntity()
								.getBusiInterfaceAttributes(
										MDRuleConstants.ORGINFO_NAME),
						MmMDConstants.ORG_INFOS)) {
					result.addResultElement(metadataResource.getResourcePath(),
							String.format("元数据【: %s 】接口属性映射: %s ,未配置对应属性\n",
									metaDataFile.getMainEntity()
											.getFullName(),
									MDRuleConstants.ORGINFO_NAME));
				}
			}

		}
		return result;

	}

}
