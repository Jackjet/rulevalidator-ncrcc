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

@RuleDefinition(catalog = CatalogEnum.METADATA, executePeriod=ExecutePeriod.DEPLOY, executeLayer=ExecuteLayer.BUSICOMP, description = "Ԫ���ݿ����У���������ʵ������Ϊ�����ֶ�", relatedIssueId = "550", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "liangqq", memo = "Ԫ���ݿ����У���������ʵ������Ϊ�����ֶΡ������ڿ�������ģ���ǣ������ֶβ�����ʾ�ڽ����ϣ���ϵͳ�Զ�ά�����������û�����Ƿ�����������ϵͳ���쳣", solution = "���Խ����º��飬���Ǽ�ֵ���� ���Ԫ���ݹ����޸ģ����������Զ���������")
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
				"Ԫ���ݡ�%s ����������������Ϊ���أ������Ϲ���\n", "displayname"));
		return result;
	}

	private String bulidQuerySQL(List<String> mdids, List<String> pkFieldNames) {
		// ��ѯ����hided�ֶ�ΪY��Ԫ������Ϣ
		StringBuilder s = new StringBuilder(
				"select distinct c.displayname from md_property p inner join md_class c on p.classid = c.id where p.hided = 'N' and ");
		s.append(SQLQueryExecuteUtils.buildSqlForIn("p.classid", mdids.toArray(new String[0])));
		s.append(" and ");
		s.append(SQLQueryExecuteUtils.buildSqlForIn("p.name", pkFieldNames.toArray(new String[0])));
		return s.toString();
	}
}
