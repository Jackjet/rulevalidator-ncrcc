package com.yonyou.nc.codevalidator.plugin.domain.mm.other.upm;

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
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * ���UPM�ļ���ͬһģ����EJBname�Ƿ���ͬ
 * 
 * @author qiaoyang
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.OTHERCONFIGFILE, description = "���UPM�ļ���ͬһģ����EJBname�Ƿ���ͬ", memo = "",
		solution = "", subCatalog = SubCatalogEnum.OCF_UPM, coder = "qiaoyang",
		relatedIssueId = "129")
public class TestCase00129 extends AbstractUpmQueryRuleDefinition {

	@Override
	protected UpmResourceQuery getUpmResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery query = new UpmResourceQuery();
		query.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return query;
	}

	@Override
	protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
			throws ResourceParserException {
		String ejbName = ruleExecContext.getBusinessComponent().getModule().toUpperCase() + "_EJB";
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (UpmResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			try {
				UpmModuleVO upmModuleVo = resource.getUpmModuleVo();
				String moduleName = upmModuleVo.getModuleName();
				if (moduleName == null) {
					noteBuilder.append(" EJB�����ǿյ�");
					continue;
				}
				if (!ejbName.equals(moduleName.toUpperCase())) {
					noteBuilder.append(String.format("���� %s ����׼,Ӧ����%s ", moduleName, ejbName));
				}
			} catch (ResourceParserException e) {
				noteBuilder.append(e.getMessage());
			} finally {
				if (noteBuilder.toString().trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}
}
