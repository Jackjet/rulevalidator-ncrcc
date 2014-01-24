package com.yonyou.nc.codevalidator.plugin.domain.mm.md.filed;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDConstants;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * <li>
 * Ԫ�����ֶε���ʾ�����в��ܰ������������ݣ��磺������ID��PK��OID��VID�������ӱ����Ϣ��</li>
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, solution="Ԫ�����ֶε���ʾ�����в��ܰ�������������(�磺������ID��PK��OID��VID�������ӱ����Ϣ)",
description = "Ԫ�����ֶε���ʾ�����в��ܰ�������������(�磺������ID��PK��OID��VID�������ӱ����Ϣ)", relatedIssueId = "94", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "lijbe")
public class TestCase00094 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ResourceRuleExecuteResult resourceRuleResult = new ResourceRuleExecuteResult();
		if (resources != null && resources.size() > 0) {
			for (MetadataResource resource : resources) {
				IMetadataFile metadataFile = resource.getMetadataFile();
				List<IEntity> allEntities = metadataFile.getAllEntities();
				StringBuilder result = new StringBuilder();
				for (IEntity entity : allEntities) {

					List<String> logicAttribs = getEntityShowNames(entity);
					if (logicAttribs.size() > 0) {
						result.append(String
								.format("ʵ�塾%s ����Ӧ�ֶ��б�:%s ,���ڵ���ʾ�����а�������������(�磺������ID��PK��OID��VID�������ӱ����Ϣ),���޸�!\n",
										entity.getFullName(), logicAttribs));
					}
				}
				if (result.toString().trim().length() > 0) {
					resourceRuleResult.addResultElement(
							resource.getResourcePath(), result.toString());
				}
			}
		}
		return resourceRuleResult;
	}

	/**
	 * �õ�ʵ����в����Ϲ淶����ʾ��
	 * 
	 * @param entity
	 * @return
	 */
	private List<String> getEntityShowNames(IEntity entity) {
		List<String> result = new ArrayList<String>();
		List<IAttribute> attributeList = entity.getAttributes();
		String[] itemNames = MmMDConstants.NONSTANDARD_NAMES;
		for (IAttribute attribute : attributeList) {
			String attribDisplayName = attribute.getDisplayName();
			for (String banName : itemNames) {
				if (attribDisplayName.toLowerCase().contains(
						banName.toLowerCase())) {
					result.add(attribDisplayName);
					continue;
				}
			}
		}
		return result;
	}

}
