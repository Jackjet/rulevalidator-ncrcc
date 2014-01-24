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
 * Ԫ����ʵ�����ʵ��IOrgInfo�ӿ�
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, description = "Ԫ����ʵ�����ʵ��IOrgInfo�ӿ�", solution = "Ԫ����ʵ�����ʵ��IOrgInfo�ӿ�", relatedIssueId = "106", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "lijbe")
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
						"Ԫ���ݡ�" + metaDataFile.getMainEntity().getFullName()
								+ "��ʵ�����ʵ��IOrgInfo�ӿڣ�����Ԫ������ʵ���Ƿ�ʵ���˸ýӿ�\n");
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
							String.format("Ԫ���ݡ�: %s ���ӿ�����ӳ��: %s ,δ���ö�Ӧ����\n",
									metaDataFile.getMainEntity()
											.getFullName(),
									MDRuleConstants.ORGINFO_NAME));
				}
			}

		}
		return result;

	}

}