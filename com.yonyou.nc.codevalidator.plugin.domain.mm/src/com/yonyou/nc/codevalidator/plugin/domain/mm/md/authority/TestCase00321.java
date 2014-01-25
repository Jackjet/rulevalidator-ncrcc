package com.yonyou.nc.codevalidator.plugin.domain.mm.md.authority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ���Ԫ��������Ҫ֧������Ȩ�޵��ֶ��Ƿ�֧��������Ȩ��
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, description = "���Ԫ��������Ҫ֧������Ȩ�޵��ֶ��Ƿ�֧��������Ȩ��,�Ƿ�ѡ��Ԫ���������е�ʹ��Ȩ", relatedIssueId = "321", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "lijbe", specialParamDefine = { "֧������Ȩ�޵��ֶ���" }, memo = "�ڲ�����¼����Ҫ֧������Ȩ�޵��ֶ���,����#�ֿ�,Ĭ�ϼ��materialvid�ֶ��Ƿ�֧��������Ȩ��",solution="���Ԫ��������Ҫ֧������Ȩ�޵��ֶ��Ƿ�֧��������Ȩ��,���Ƿ�ѡ��Ԫ�����еġ�ʹ��Ȩ������.")
public class TestCase00321 extends AbstractMetadataResourceRuleDefinition {

	private static String PARAM_NAME = "֧������Ȩ�޵��ֶ���";

	private static String DEFAULT_CHECK_FIELD = "materialvid";

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

		String[] concreteValues = ruleExecContext
				.getParameterArray(TestCase00321.PARAM_NAME);

		for (MetadataResource metadataResource : resources) {
			List<IEntity> entities = metadataResource.getMetadataFile()
					.getAllEntities();
			List<String> fields = new ArrayList<String>();
			for (IEntity entity : entities) {
				if (MMValueCheck.isEmpty(concreteValues)) {
					concreteValues = this.getAttributeByFieldName(entity,
							TestCase00321.DEFAULT_CHECK_FIELD);

				}
				if (MMValueCheck.isEmpty(concreteValues)) {
					continue;
				}
				List<IAttribute> attrs = entity.getAttributes();
				for (IAttribute iAttribute : attrs) {
					if (Arrays.asList(concreteValues).contains(
							iAttribute.getFieldName())
							&& !iAttribute.isAccessPower()) {
						fields.add(iAttribute.getDisplayName());
					}
				}
				if (fields.size() > 0) {
					result.addResultElement(metadataResource.getResourcePath(),
							String.format("Ԫ���ݡ� %s �������ֶ�: %s û��֧������Ȩ��,����! \n",
									entity.getFullName(), fields));
				}
			}
		}

		return result;
	}

	private String[] getAttributeByFieldName(IEntity entity, String fieldName) {

		List<String> fields = new ArrayList<String>();

		List<IAttribute> attrs = entity.getAttributes();

		for (IAttribute iAttribute : attrs) {
			if (iAttribute.getFieldName().contains(fieldName)) {
				fields.add(iAttribute.getFieldName());
			}
		}

		return fields.toArray(new String[fields.size()]);
	}

}
