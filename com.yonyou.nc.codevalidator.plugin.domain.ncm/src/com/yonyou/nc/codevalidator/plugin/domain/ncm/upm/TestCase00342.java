package com.yonyou.nc.codevalidator.plugin.domain.ncm.upm;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

@RuleDefinition(
		catalog = CatalogEnum.OTHERCONFIGFILE,
		description = "upm 事务配置项 tx",
		solution = "建议校验upm文件中部署的接口事务管理方式，如果配置事务管理方式为BMT或者NONE方式时，则应提示开发人员这个选项带来的影响，以避免开发人员误写事务配置项带来隐藏的事务问题，不易在单元测试环境中发现",
		relatedIssueId = "00342", subCatalog = SubCatalogEnum.OCF_UPM, coder = "wumr3",
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00342 extends AbstractUpmQueryRuleDefinition {

	protected UpmResourceQuery getUpmResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery query = new UpmResourceQuery();
		query.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return query;
	}

	@Override
	protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
			throws ResourceParserException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (UpmResource upmResource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			UpmModuleVO upmModuleVO = upmResource.getUpmModuleVo();
			List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
			if (pubComponentVoList != null) {
				for (UpmComponentVO upmComponentVO : pubComponentVoList) {
					if (!upmComponentVO.getTx().equals("CMT")) {
						noteBuilder.append(String.format("upm文件中有组件的事务管理方式是%s", upmComponentVO.getTx()));
					}
				}
			}
			if (noteBuilder.toString().trim().length() > 0) {
				result.addResultElement(upmResource.getResourcePath(), noteBuilder.toString());
			}
		}
		return result;
	}

}
