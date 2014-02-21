package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * Ԫ����ö�ٱ������int���ʹ洢
 * 
 * @author ZG
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "Ԫ����ö�ٱ������int���ʹ洢", relatedIssueId = "57", subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", executePeriod = ExecutePeriod.CHECKOUT, executeLayer = ExecuteLayer.BUSICOMP)
public class TestCase00057 extends AbstractMetadataResourceRuleDefinition {

	private static final List<String> CHECK_TYPE_LIST = Arrays.asList("", "String", "UFID", "Integer", "UFDouble",
			"UFBoolean", "UFDate", "UFDate_begin", "UFDate_end", "UFLiteralDate", "UFDateTime", "UFTime", "BigDecimal",
			"UFMoney", "ͼƬ", "BLOB", "�Զ�����", "������", "��ע", "�����ı�", "������");

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			// �õ�Ԫ�����ļ�
			IMetadataFile metadataFile = resource.getMetadataFile();
			if (metadataFile.containsMainEntity()) {
				List<IEntity> entitylist = metadataFile.getAllEntities();// �������ʵ��
				for (IEntity entity : entitylist) {
					List<IAttribute> attributeList = entity.getAttributes(); // ��õ���ʵ����������
					List<String> violateAttributeNames = new ArrayList<String>();
					for (IAttribute attribute : attributeList) {
						if (attribute.getTypeStyle().equals(MDRuleConstants.TYPE_STYLE_SINGLE)
								&& !CHECK_TYPE_LIST.contains(attribute.getType().getDisplayName().trim())
								&& !attribute.getFieldType().equals("int")) {
							violateAttributeNames.add(attribute.getDisplayName());
						}
					}
					if (!violateAttributeNames.isEmpty()) {
						noteBuilder.append(String.format("ʵ��:%s �� ����Ϊ:%s ���ֶΣ�ö�٣����Ͳ���int������!\n",
								entity.getDisplayName(), violateAttributeNames.toString()));
					}
				}
				if (StringUtils.isNotBlank(noteBuilder.toString())) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}

}
