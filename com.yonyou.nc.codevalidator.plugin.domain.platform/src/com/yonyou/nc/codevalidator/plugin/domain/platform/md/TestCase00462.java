package com.yonyou.nc.codevalidator.plugin.domain.platform.md;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
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

@RuleDefinition(catalog = CatalogEnum.METADATA, executePeriod = ExecutePeriod.DEPLOY, executeLayer = ExecuteLayer.BUSICOMP, description = "检查参照中的metadatatypename字段与元数据实体名称是否相同", relatedIssueId = "462", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "yuanchh", memo = "参照中的metadatatypename需与元数据实体名相同", solution = "修改参照中的metadatatypename字段与元数据实体名相同")
public class TestCase00462 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		// ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		StringBuilder noteBuilder = new StringBuilder();
		// 获取所有的元数据空间名.实体名
		List<String> fields = new ArrayList<String>();
		if (resources != null && resources.size() > 0) {
			for (MetadataResource metadataResource : resources) {
				List<IEntity> entities = metadataResource.getMetadataFile()
						.getAllEntities();
				for (IEntity entity : entities) {
					if (!StringUtils.isBlank(entity.getFullName())) {
						fields.add(entity.getFullName());
					}
				}
			}
			// 查询出所有的参数数据。
			String sql = String
					.format("select pk_refinfo,modulename,metadatatypename from bd_refinfo where modulename='%s'",
							ruleExecContext.getBusinessComponent().getModule());
			DataSet dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(),
					ruleExecContext.getRuntimeContext());
			if (!dataSet.isEmpty()) {
				for (DataRow row : dataSet.getRows()) {
					String modulename = (String) row.getValue("modulename");
					String pk_refinfo = (String) row.getValue("pk_refinfo");
					if (StringUtils.isBlank(modulename)) {
						noteBuilder.append("PK_REFINFO=" + pk_refinfo
								+ "的modulename为空,不符合规则!\n");
						continue;
					}
					String metadatatypename = (String) row
							.getValue("metadatatypename");
					if (StringUtils.isBlank(metadatatypename)) {
						noteBuilder.append("参照PK_REFINFO=" + pk_refinfo
								+ "的metadatatypename为空,不符合规则!\n");
						continue;
					}
					String fullname = modulename + "." + metadatatypename;
					if (fields.contains(fullname)) {
						continue;
					} else {
						noteBuilder
								.append("参照PK_REFINFO=" + pk_refinfo
										+ "的空间名.实体名【" + fullname
										+ "】在元数据中不存在,不符合规则!\n");
					}
				}
			}
		}
		return noteBuilder.toString().trim().length() > 0 ? new ErrorRuleExecuteResult(
				getIdentifier(), noteBuilder.toString())
				: new SuccessRuleExecuteResult(getIdentifier());
	}
}
