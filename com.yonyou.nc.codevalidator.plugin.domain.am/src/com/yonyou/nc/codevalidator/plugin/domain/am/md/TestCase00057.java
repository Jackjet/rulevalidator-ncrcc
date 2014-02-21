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
 * 元数据枚举必须采用int类型存储
 * 
 * @author ZG
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "元数据枚举必须采用int类型存储", relatedIssueId = "57", subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", executePeriod = ExecutePeriod.CHECKOUT, executeLayer = ExecuteLayer.BUSICOMP)
public class TestCase00057 extends AbstractMetadataResourceRuleDefinition {

	private static final List<String> CHECK_TYPE_LIST = Arrays.asList("", "String", "UFID", "Integer", "UFDouble",
			"UFBoolean", "UFDate", "UFDate_begin", "UFDate_end", "UFLiteralDate", "UFDateTime", "UFTime", "BigDecimal",
			"UFMoney", "图片", "BLOB", "自定义项", "自由项", "备注", "多语文本", "换算率");

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			// 得到元数据文件
			IMetadataFile metadataFile = resource.getMetadataFile();
			if (metadataFile.containsMainEntity()) {
				List<IEntity> entitylist = metadataFile.getAllEntities();// 获得所有实体
				for (IEntity entity : entitylist) {
					List<IAttribute> attributeList = entity.getAttributes(); // 获得单个实体所有属性
					List<String> violateAttributeNames = new ArrayList<String>();
					for (IAttribute attribute : attributeList) {
						if (attribute.getTypeStyle().equals(MDRuleConstants.TYPE_STYLE_SINGLE)
								&& !CHECK_TYPE_LIST.contains(attribute.getType().getDisplayName().trim())
								&& !attribute.getFieldType().equals("int")) {
							violateAttributeNames.add(attribute.getDisplayName());
						}
					}
					if (!violateAttributeNames.isEmpty()) {
						noteBuilder.append(String.format("实体:%s 的 名称为:%s 的字段（枚举）类型不是int，请检测!\n",
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
