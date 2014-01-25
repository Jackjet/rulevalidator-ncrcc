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
 * ������Ϣ��ȡ�Ľӿ�ʵ�ֺ�����ӳ�䣨�ǵ���ʱ��ѡ��
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA,
		description = "������Ϣ��ȡ�Ľӿ�ʵ�ֺ�����ӳ��(�ǵ���ʱ��ѡ)", relatedIssueId = "107", subCatalog = SubCatalogEnum.MD_BASESETTING,
		coder = "lijbe", specialParamDefine = { "�ӿ�ӳ���ֶ�" }, memo = "�ýӿ�ÿ��������Ҫӳ������Բ�һ��,"
				+ "������������������Ҫ���Ľӿ�������,������Ҫ���[����ID],��ô�����о���Ӳ���[����ID],�������Լ�Ԫ�����е�������.",
		solution = "���Ԫ������������Ϣ��ȡ�Ľӿ�ʵ�ֺ�����ӳ��(�ǵ���ʱ��ѡ),�������û��ʵ�ָýӿ�,��ô����Ҫ��д����")
public class TestCase00107 extends AbstractMetadataResourceRuleDefinition {

	private static String PARAM_NAME = "�ӿ�ӳ���ֶ�";

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

		for (MetadataResource metadataResource : resources) {
			IMetadataFile metaDataFile = metadataResource.getMetadataFile();
			if (MMValueCheck.isEmpty(metaDataFile.getMainEntity())) {
				continue;
			}
			List<IBusiInterface> itfList = metaDataFile.getMainEntity().getBusiInterfaces();
			if (null == itfList || itfList.size() < 1
					|| !MmMetadataUtils.getBusiInterfaceNames(itfList).contains(MDRuleConstants.FLOWBIZ_INTERFACE_NAME)) {
				continue;
			}
			if (MmMetadataUtils.getBusiInterfaceNames(itfList).contains(MDRuleConstants.FLOWBIZ_INTERFACE_NAME)) {

				String[] mapProperties = ruleExecContext.getParameterArray(TestCase00107.PARAM_NAME);
				if (MMValueCheck.isEmpty(mapProperties)) {
					result.addResultElement(metadataResource.getResourcePath(), String.format(
							"Ԫ���ݡ� %s ��ʵ���˽ӿ� %s , ��������Ϊ�գ������������\n", metaDataFile.getMainEntity().getFullName(),
							MDRuleConstants.FLOWBIZ_INTERFACE_NAME));
				} else {
					if (mapProperties.length < 1) {
						mapProperties = MmMDConstants.FLOW_BUSI_ITFS;
					}
					if (!MmMetadataUtils.isMappingProperties(
							metaDataFile.getMainEntity().getBusiInterfaceAttributes(
									MDRuleConstants.FLOWBIZ_INTERFACE_NAME), mapProperties)) {
						result.addResultElement(metadataResource.getResourcePath(), String.format(
								"Ԫ���ݡ� %s ��, �ӿ�����ӳ��: %s , δ���ö�Ӧ����\n", metaDataFile.getMainEntity().getFullName(),
								MDRuleConstants.FLOWBIZ_INTERFACE_NAME));
					}
				}
			}

		}
		return result;

	}

}
