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
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "�������͵��ݺŽ������ͱ����ֶμ����ȼ��", relatedIssueId = "521",
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
				// ͨ��ҵ��ӿڻ�ȡ��Ӧ�ĵ��ݺź͵�������
				Map<String, IAttribute> nameToAttributeMap = mainEntity
						.getBusiInterfaceAttributes(MDRuleConstants.FlowBizItf_INTERFACE_NAME);
				if (nameToAttributeMap == null || nameToAttributeMap.size() == 0) {
					continue;
				}
				// ��ע��Ϣ
				StringBuilder noteString = new StringBuilder();
				IAttribute billcode = nameToAttributeMap.get("���ݺ�");
				IAttribute billtype = nameToAttributeMap.get("��������");
				IAttribute transitype = nameToAttributeMap.get("��������");
				noteString.append(getBillCodeResult(billcode));
				noteString.append(getBillTypeResult(billtype));
				noteString.append(getTransactionTypeResult(transitype));

				if (noteString.toString().length() > 0) {
					String prefix = mainEntity.getDisplayName() + ":(ע�⣺�緢����ʾ�������ƺ�����ֶβ�ƥ������ҵ��ӿ�����ӳ������)    \n";
					result.addResultElement(resource.getResourcePath(), prefix + noteString.toString());
				}
			}
		}
		return result;
	}

	private String getTransactionTypeResult(IAttribute transitype) {
		// ��������
		StringBuilder result = new StringBuilder();
		if (transitype == null) {
			result.append("������Ϣ��ȡ��дҵ��ӿ�����ӳ��δ���ý�������    \n");
		} else {
			if (!transitype.getFieldType().equals("varchar")) {
				result.append("��������" + transitype.getName() + "�ֶε��ֶ����Ͳ�Ϊvarchar \n");
			}
			if (!transitype.getLength().equals("30")) {
				result.append("��������" + transitype.getName() + "���Ȳ�����30 \n");
			}
		}
		return result.toString();
	}

	private String getBillTypeResult(IAttribute billtype) {
		// ��������
		StringBuilder noteString = new StringBuilder();
		if (billtype == null) {
			noteString.append("������Ϣ��ȡ��дҵ��ӿ�����ӳ��δ���õ�������!\n");
		} else {
			if (!billtype.getFieldType().equals("varchar")) {
				noteString.append(" ��������" + billtype.getName() + "�ֶε��ֶ����Ͳ�Ϊvarchar\n");
			}
			if (!billtype.getLength().equals("4")) {
				noteString.append(" ��������" + billtype.getName() + "���Ȳ�����4\n");
			}
		}
		return noteString.toString();
	}

	private String getBillCodeResult(IAttribute billcode) {
		// ���ݺ�
		StringBuilder result = new StringBuilder();
		if (billcode == null) {
			result.append("������Ϣ��ȡ��дҵ��ӿ�����ӳ��δ���õ��ݺ�\n");
		} else {
			if (!billcode.getFieldType().equals("varchar")) {
				result.append("���ݺ�" + billcode.getName() + "�ֶ����Ͳ�Ϊvarchar\n");
			}
			if (!billcode.getLength().equals("40")) {
				result.append("���ݺ�" + billcode.getName() + "�ֶγ��Ȳ�Ϊ40\n");
			}
		}
		return result.toString();
	}
}
