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
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "Ԫ���ݡ����ơ��͡��ֶ����ơ�����ͬ����", relatedIssueId = "614",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00614 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			IMetadataFile metadataFile = resource.getMetadataFile();
			List<IEntity> allEntities = metadataFile.getAllEntities();
			StringBuilder noteBuilder = new StringBuilder();
			for (IEntity entity : allEntities) { // ʵ�����
				List<IAttribute> entityattlist = entity.getAttributes(); // ʵ�����Լ�
				for (IAttribute ia : entityattlist) {
					if (!ia.getTypeStyle().trim().equals(MDRuleConstants.TYPE_STYLE_ARRAY)
							&& !ia.getFieldName().trim().equals(ia.getName().trim())) {
						noteBuilder.append("ʵ��:").append(
								entity.getDisplayName() + "����ʾ����Ϊ" + ia.getDisplayName() + "���ֶε����ƺ��ֶ����Ʋ�һ�£����⣡\n");
					}
				}
			}
			if (noteBuilder.toString().length() > 0) {
				result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
			}
		}
		return result;
	}

}
