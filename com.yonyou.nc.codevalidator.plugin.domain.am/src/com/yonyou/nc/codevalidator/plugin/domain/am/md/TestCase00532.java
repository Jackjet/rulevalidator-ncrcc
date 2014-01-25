package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.ScopeEnum;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA,
		description = "项目实体方面实现流程信息回写接口的，此接口的实现类必须为nc.impl.pm.common.PMFlowBizImpl", relatedIssueId = "532",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", scope = ScopeEnum.AM,
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00532 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		// 检查结果
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// 获取元数据文件
			IMetadataFile metadataFile = resource.getMetadataFile();
			if (metadataFile.containsMainEntity()) {
				IEntity mainentity = metadataFile.getMainEntity();
				String mainsql = "select a.DISPLAYNAME,b.BIZITFIMPCLASSNAME " + "from md_class a,md_bizitfmap b  "
						+ "where a.DEFAULTTABLENAME like 'pm_%' " + "and a.id = b.classid "
						+ "and b.BIZINTERFACEID =  '" + mainentity.getId() + "'" + "and a.id = '" + mainentity.getId()
						+ "'";
				DataSet ds = SQLQueryExecuteUtils.executeQuery(mainsql, ruleExecContext.getRuntimeContext());
				List<DataRow> maindatarow = ds.getRows();
				if (!ds.isEmpty() && !maindatarow.isEmpty()) {
					for (DataRow dr : maindatarow) { // 遍历属性，寻找自定义项 包括主实体和子实体
						String bitItfClassName = (String) dr.getValue("BIZITFIMPCLASSNAME");
						if (StringUtils.isNcStringNotEmpty(bitItfClassName)
								&& !bitItfClassName.equals("nc.impl.pm.common.PMFlowBizImpl")) { // 主实体
							String displayName = (String) dr.getValue("DISPLAYNAME");
							result.addResultElement(resource.getResourcePath(), displayName
									+ " 实现的流程信息回写接口的实现类不是PMFlowBizImpl！ 请检查！");
						}
					}
				}
			}
		}
		return result;
	}

}
