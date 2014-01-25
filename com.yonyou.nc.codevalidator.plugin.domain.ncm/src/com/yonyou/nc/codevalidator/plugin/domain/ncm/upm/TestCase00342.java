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
		description = "upm ���������� tx",
		solution = "����У��upm�ļ��в���Ľӿ��������ʽ����������������ʽΪBMT����NONE��ʽʱ����Ӧ��ʾ������Ա���ѡ�������Ӱ�죬�Ա��⿪����Ա��д����������������ص��������⣬�����ڵ�Ԫ���Ի����з���",
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
						noteBuilder.append(String.format("upm�ļ�����������������ʽ��%s", upmComponentVO.getTx()));
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
