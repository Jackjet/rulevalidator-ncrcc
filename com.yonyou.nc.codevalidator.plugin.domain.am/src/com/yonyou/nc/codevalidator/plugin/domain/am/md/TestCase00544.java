package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;

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
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "���Ԫ������ʵ�������ӱ��ֶε��ֶ���������ʵ��pk�Ƿ�һ��", relatedIssueId = "544",
		subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "xiepch", repairLevel = RepairLevel.SUGGESTREPAIR,
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00544 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			StringBuffer noteString = new StringBuffer();
			IMetadataFile metadataFile = resource.getMetadataFile();
			if (metadataFile.containsMainEntity()) {
				IEntity entity = metadataFile.getMainEntity();
				noteString.append(entity.getDisplayName() + ":\n");
				// �ж��Ƿ��д�
				boolean judge = false;
				// ��ȡ����
				String keyAttributeName = entity.getKeyAttribute().getName();
				for (IAttribute attribute : entity.getAttributes()) {
					if (attribute.getTypeStyle().equals(MDRuleConstants.TYPE_STYLE_ARRAY)) {
						// ���бȽ�
						if (!keyAttributeName.equals(attribute.getFieldName())) {
							judge = true;
							noteString.append(attribute.getDisplayName() + ":" + attribute.getFieldName() + "\n");
						}
					}
				}
				if (judge) {
					result.addResultElement(resource.getResourcePath(), noteString.toString() + "������"
							+ keyAttributeName + "��һ�£�        \n");
				}
			}
		}
		return result;
	}

}
