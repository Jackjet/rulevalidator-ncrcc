package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "设备卡片、物料、客户、供应商等字段是否注册超链接", relatedIssueId = "882",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "zhangnane", repairLevel = RepairLevel.SUGGESTREPAIR,
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00882 extends AbstractMetadataResourceRuleDefinition {

	private static final List<String> SUPER_LINK_FIELDS = Arrays.asList("设备档案", "物料", "物料(多版本)", "客户档案", "供应商档案");

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		List<String> fieldFullnames = new ArrayList<String>();
		for (MetadataResource resource : resources) {
			IMetadataFile metadatafile = resource.getMetadataFile();
			List<IEntity> entities = metadatafile.getAllEntities();
			for (IEntity entity : entities) {
				List<IAttribute> attributes = entity.getAttributes();
				for (IAttribute attribute : attributes) {
					String refname = attribute.getRefModuleName();
					if (SUPER_LINK_FIELDS.contains(refname)) {
						fieldFullnames.add(entity.getFullName() + '.' + attribute.getFieldName());
					}
				}
			}
		}
		if (!fieldFullnames.isEmpty()) {
			StringBuilder sql = new StringBuilder(
					"select pub_billtemplet.bill_templetcaption templetname,metadataproperty propertyname from pub_billtemplet_b ");
			sql.append("left join  pub_billtemplet on pub_billtemplet_b.pk_billtemplet = pub_billtemplet.pk_billtemplet ");
			sql.append(" where ");
			sql.append(SQLQueryExecuteUtils.buildSqlForIn("metadataproperty",
					fieldFullnames.toArray(new String[fieldFullnames.size()])));
			sql.append(" and pk_billtypecode not like 'CP_%' and bill_templetcaption not like '%移动模板%' ");
			sql.append("and hyperlinkflag = 'N' and showflag = '1' and  pub_billtemplet.pk_corp like '@@@@'");
			DataSet dataset = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());
			ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
			result.setDataSet(dataset);
			result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
					"建议单据模板中以下字段添加超链接: 模板名称：%s 字段编码：%s", "templetname", "propertyname"));
			return result;
		} else {
			return new SuccessRuleExecuteResult(getIdentifier());
		}
	}

}
