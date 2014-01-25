package com.yonyou.nc.codevalidator.plugin.domain.platform.md;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.MDRuleConstants;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.METADATA, executePeriod = ExecutePeriod.CHECKOUT,
		executeLayer = ExecuteLayer.BUSICOMP,
		description = "��һ������������ɸ��໥��ϵ��ʵ�壨ֻ��һ����ʵ�壩������ʵ���б�ʾ������ʵ���ϵ�������Ե�����Ϊ����ʵ������ƣ��ֶ�����Ϊ��ʵ���������", relatedIssueId = "456",
		subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "suncheng3",
		memo = "��ʵ���б�ʾ������ʵ���ϵ�������Ե�����Ϊ����ʵ������ƣ��ֶ�����Ϊ��ʵ���������", solution = "��ʵ���б�ʾ������ʵ���ϵ�������Ե�����ӦΪ����ʵ������ƣ��ֶ�����ӦΪ��ʵ���������")
public class TestCase00456 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		// ��ȡ���е�Ԫ����
		if (resources != null && resources.size() > 0) {
			for (MetadataResource metadataResource : resources) {
				StringBuilder noteBuilder = new StringBuilder();
				IMetadataFile metafile = metadataResource.getMetadataFile();

				// ��ȡԪ���ݶ�Ӧ������ʵ��
				IEntity mainEntity = metafile.getMainEntity();
				// ��ȡ��ʵ���Ӧ����������
				if (mainEntity != null) {
					List<IAttribute> attributes = mainEntity.getAttributes();
					// �����������Զ�Ӧ��map
					Map<String, IAttribute> attnameAttributeMap = new HashMap<String, IAttribute>();
					for (IAttribute attribute : attributes) {
						// ���Ϊ����������map
						if (MDRuleConstants.TYPE_STYLE_REF.equalsIgnoreCase(attribute.getTypeStyle())) {
							attnameAttributeMap.put(attribute.getName(), attribute);
						}
					}
					// ��ȡԪ���ݶ�Ӧ������ʵ��
					List<IEntity> entities = metafile.getAllEntities();
					for (IEntity entity : entities) {
						// �����������ʵ����Ч��ȥ����ʵ��
						if (entity.getId() != mainEntity.getId()) {
							String entityName = entity.getDisplayName();
							if (attnameAttributeMap.containsKey(entityName)) {
								IAttribute attribute = attnameAttributeMap.get(entityName);
								if (attribute.getFieldName().equals(mainEntity.getKeyAttribute().getFieldName()) == false) {
									noteBuilder.append(mainEntity.getFullClassName()).append("��")
											.append(attribute.getDisplayName()).append("���Ե��ֶ���������Ӧ����ʵ��")
											.append(entity.getFullName()).append("������������/n");
								}
							} else {
								noteBuilder.append("��ʵ��").append(entity.getFullName()).append("����������ʵ��")
										.append(mainEntity.getFullClassName()).append("���������Ҳ���/n");
							}
						}
					}
					if (noteBuilder.toString().trim().length() > 0) {
						result.addResultElement(metadataResource.getResourcePath(), noteBuilder.toString());
					}
				}
			}
		}
		return result;
	}

}
