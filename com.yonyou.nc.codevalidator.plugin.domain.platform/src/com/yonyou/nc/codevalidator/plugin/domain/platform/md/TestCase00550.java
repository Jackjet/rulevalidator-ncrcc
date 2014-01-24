package com.yonyou.nc.codevalidator.plugin.domain.platform.md;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

@RuleDefinition(catalog = CatalogEnum.METADATA, executePeriod=ExecutePeriod.DEPLOY, executeLayer=ExecuteLayer.BUSICOMP, description = "元数据开发中，建议设置实体主键为隐藏字段", relatedIssueId = "550", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "liangqq", memo = "元数据开发中，建议设置实体主键为隐藏字段。这样在开发单据模板是，主键字段不会显示在界面上，有系统自动维护，避免了用户输入非法主键而导致系统抛异常", solution = "可以进行事后检查，但是价值不大； 如果元数据工具修改，加入主键自动隐藏属性")
public class TestCase00550 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		if (resources == null || resources.size() <= 0) {
			return new SuccessRuleExecuteResult(getIdentifier());
		}
		List<String> mdids = new ArrayList<String>();
		List<String> pkFieldNames = new ArrayList<String>();

		for (MetadataResource metadataResource : resources) {
			List<IEntity> entities = metadataResource.getMetadataFile()
					.getAllEntities();
			for (IEntity entity : entities) {
				if (!StringUtils.isBlank(entity.getId())) {
					mdids.add(entity.getId());
					pkFieldNames.add(entity.getKeyAttribute().getFieldName());
				}
			}
		}
		String sql = bulidQuerySQL(mdids, pkFieldNames);
		DataSet dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(),
				ruleExecContext.getRuntimeContext());
		ScriptRuleExecuteResult result = checkResult(dataSet);
		return result;
	}

	private ScriptRuleExecuteResult checkResult(DataSet dataSet) {
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(dataSet);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
				"元数据【%s 】的主键建议设置为隐藏，不符合规则！\n", "displayname"));
		return result;
	}

	private String bulidQuerySQL(List<String> mdids, List<String> pkFieldNames) {
		// 查询所有hided字段为Y的元数据信息
		StringBuilder s = new StringBuilder(
				"select distinct c.displayname from md_property p inner join md_class c on p.classid = c.id where p.hided = 'N' and ");
		s.append(SQLQueryExecuteUtils.buildSqlForIn("p.classid", mdids.toArray(new String[0])));
		s.append(" and ");
		s.append(SQLQueryExecuteUtils.buildSqlForIn("p.name", pkFieldNames.toArray(new String[0])));
		return s.toString();
	}
}
