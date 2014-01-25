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
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.ScopeEnum;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "主子单据第一个页签元数据名字必须是bodyvos", relatedIssueId = "489",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", scope = ScopeEnum.AM)
public class TestCase00489 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			IMetadataFile metadataFile = resource.getMetadataFile();
			if(metadataFile.containsMainEntity()) {
				//TODO: 是否单据模板上第一个页签名称为bodyvos,  这是由单据保证的而不是元数据保证的
				IEntity mainEntity = metadataFile.getMainEntity();
				if (containMultiEntity(metadataFile)) {
					List<IAttribute> entityattlist = mainEntity.getAttributes(); // 实体属性集
					// List<String> fieldNamelist = new ArrayList<String>();
					String firstArrayFieldName = null;
					for (IAttribute att : entityattlist) {
						if (att.getTypeStyle().equals(MDRuleConstants.TYPE_STYLE_ARRAY)) {
							firstArrayFieldName = att.getName();
							break;
						}
					}
					if (firstArrayFieldName != null) {
						// StringBuilder notebuilder = new StringBuilder();
						if (!firstArrayFieldName.equals("bodyvos")) {
							//
							// } else {
							// notebuilder.append();
							result.addResultElement(resource.getResourcePath(), "主实体中第一个子实体字段名称是" + firstArrayFieldName
									+ ", 而不为bodyvos，请检查更正！");
							// }
						}
					}
				}
			}
		}
		return result;
	}
	
}
