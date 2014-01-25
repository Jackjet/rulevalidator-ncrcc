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
 * 流程信息获取的接口实现和属性映射（非单据时可选）
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA,
		description = "流程信息获取的接口实现和属性映射(非单据时可选)", relatedIssueId = "107", subCatalog = SubCatalogEnum.MD_BASESETTING,
		coder = "lijbe", specialParamDefine = { "接口映射字段" }, memo = "该接口每个单据所要映射的属性不一样,"
				+ "请在特殊参数中添加需要检查的接口属性名,例如需要检查[单据ID],那么参数中就添加参数[单据ID],而不是自己元数据中的属性名.",
		solution = "检查元数据中流程信息获取的接口实现和属性映射(非单据时可选),如果单据没有实现该接口,那么不需要填写参数")
public class TestCase00107 extends AbstractMetadataResourceRuleDefinition {

	private static String PARAM_NAME = "接口映射字段";

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
							"元数据【 %s 】实现了接口 %s , 参数不能为空，请输入参数。\n", metaDataFile.getMainEntity().getFullName(),
							MDRuleConstants.FLOWBIZ_INTERFACE_NAME));
				} else {
					if (mapProperties.length < 1) {
						mapProperties = MmMDConstants.FLOW_BUSI_ITFS;
					}
					if (!MmMetadataUtils.isMappingProperties(
							metaDataFile.getMainEntity().getBusiInterfaceAttributes(
									MDRuleConstants.FLOWBIZ_INTERFACE_NAME), mapProperties)) {
						result.addResultElement(metadataResource.getResourcePath(), String.format(
								"元数据【 %s 】, 接口属性映射: %s , 未配置对应属性\n", metaDataFile.getMainEntity().getFullName(),
								MDRuleConstants.FLOWBIZ_INTERFACE_NAME));
					}
				}
			}

		}
		return result;

	}

}
