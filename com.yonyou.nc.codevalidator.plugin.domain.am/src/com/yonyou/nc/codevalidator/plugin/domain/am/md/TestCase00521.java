package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.plugin.domain.am.consts.MDRuleConstants;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * @author xiepch
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "单据类型单据号交易类型编码字段及长度检查", relatedIssueId = "521",
		subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "xiepch", repairLevel = RepairLevel.SUGGESTREPAIR,
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00521 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			IMetadataFile metadataFile = resource.getMetadataFile();
			if (metadataFile.containsMainEntity() && metadataFile.isBill()) {
				IEntity mainEntity = metadataFile.getMainEntity();
				// 通过业务接口获取相应的单据号和单据类型
				Map<String, IAttribute> nameToAttributeMap = mainEntity
						.getBusiInterfaceAttributes(MDRuleConstants.FlowBizItf_INTERFACE_NAME);
				if (nameToAttributeMap == null || nameToAttributeMap.size() == 0) {
					continue;
				}
				// 备注信息
				StringBuilder noteString = new StringBuilder();
				IAttribute billcode = nameToAttributeMap.get("单据号");
				IAttribute billtype = nameToAttributeMap.get("单据类型");
				IAttribute transitype = nameToAttributeMap.get("交易类型");
				noteString.append(getBillCodeResult(billcode));
				noteString.append(getBillTypeResult(billtype));
				noteString.append(getTransactionTypeResult(transitype));

				if (noteString.toString().length() > 0) {
					String prefix = mainEntity.getDisplayName() + ":(注意：如发现提示中文名称和随后字段不匹配请检查业务接口属性映射配置)    \n";
					result.addResultElement(resource.getResourcePath(), prefix + noteString.toString());
				}
			}
		}
		return result;
	}

	private String getTransactionTypeResult(IAttribute transitype) {
		// 交易类型
		StringBuilder result = new StringBuilder();
		if (transitype == null) {
			result.append("流程信息获取回写业务接口属性映射未配置交易类型    \n");
		} else {
			if (!transitype.getFieldType().equals("varchar")) {
				result.append("交易类型" + transitype.getName() + "字段的字段类型不为varchar \n");
			}
			if (!transitype.getLength().equals("30")) {
				result.append("交易类型" + transitype.getName() + "长度不等于30 \n");
			}
		}
		return result.toString();
	}

	private String getBillTypeResult(IAttribute billtype) {
		// 单据类型
		StringBuilder noteString = new StringBuilder();
		if (billtype == null) {
			noteString.append("流程信息获取回写业务接口属性映射未配置单据类型!\n");
		} else {
			if (!billtype.getFieldType().equals("varchar")) {
				noteString.append(" 单据类型" + billtype.getName() + "字段的字段类型不为varchar\n");
			}
			if (!billtype.getLength().equals("4")) {
				noteString.append(" 单据类型" + billtype.getName() + "长度不等于4\n");
			}
		}
		return noteString.toString();
	}

	private String getBillCodeResult(IAttribute billcode) {
		// 单据号
		StringBuilder result = new StringBuilder();
		if (billcode == null) {
			result.append("流程信息获取回写业务接口属性映射未配置单据号\n");
		} else {
			if (!billcode.getFieldType().equals("varchar")) {
				result.append("单据号" + billcode.getName() + "字段类型不为varchar\n");
			}
			if (!billcode.getLength().equals("40")) {
				result.append("单据号" + billcode.getName() + "字段长度不为40\n");
			}
		}
		return result.toString();
	}
}
