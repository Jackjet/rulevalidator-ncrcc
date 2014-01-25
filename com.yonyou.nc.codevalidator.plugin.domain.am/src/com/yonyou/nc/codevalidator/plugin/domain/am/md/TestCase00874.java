package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
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

/**
 * 检查列表下单据号要有超链接
 * 
 * @author ZG
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "检查列表下单据号要有超链接", relatedIssueId = "874",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.DEPLOY, repairLevel = RepairLevel.SUGGESTREPAIR)
public class TestCase00874 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		List<String> mainidlist = new ArrayList<String>();
		for (MetadataResource resource : resources) {
			// 获取元数据文件
			IMetadataFile metadataFile = resource.getMetadataFile();
			if (metadataFile.containsMainEntity()) {
				mainidlist.add(metadataFile.getMainEntity().getId());
			}
		}
		if (!mainidlist.isEmpty()) {
			String sql = "SELECT  b.BILL_TEMPLETCAPTION ,c.ITEMKEY "
					+ "FROM  pub_billtemplet b, pub_billtemplet_b c ,SM_FUNCREGISTER s "
					+ "where b.PK_BILLTEMPLET = c.PK_BILLTEMPLET " + "and b.nodecode = s.FUNCODE "
					+ "and  c.LISTHYPERLINKFLAG = 'N' " + "AND ( c.ITEMKEY = 'bill_number' or c.ITEMKEY = 'bill_code')"
					+ "and c.SHOWFLAG = 1 " + "and s.FUNCODE in (" + "select funcode from sm_funcregister where "
					+ SQLQueryExecuteUtils.buildSqlForIn("mdid", mainidlist.toArray(new String[mainidlist.size()]))
					+ " )";
			DataSet mainds = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
			ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
			result.setDataSet(mainds);
			result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
					"单据模版名为：%s 的 %s 字段没有设置为超链接，请检查！", "BILL_TEMPLETCAPTION", "ITEMKEY"));
			return result;
		} else {
			return new SuccessRuleExecuteResult(getIdentifier());
		}
	}

}
