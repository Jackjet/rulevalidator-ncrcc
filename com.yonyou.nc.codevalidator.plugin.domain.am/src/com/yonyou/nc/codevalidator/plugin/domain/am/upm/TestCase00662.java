package com.yonyou.nc.codevalidator.plugin.domain.am.upm;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * upm定义的接口和实现类 前后不能有空格 否则was部署不成功
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.OTHERCONFIGFILE, description = "upm定义的接口和实现类 前后不能有空格 否则was部署不成功",
		memo = "upm定义的接口和实现类 前后不能有空格 否则was部署不成功", solution = "", subCatalog = SubCatalogEnum.OCF_UPM, coder = "ZG",
		relatedIssueId = "662", executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00662 extends AbstractUpmQueryRuleDefinition {

	@Override
	protected UpmResourceQuery getUpmResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery query = new UpmResourceQuery();
		query.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return query;
	}

	@Override
	protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
			throws ResourceParserException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (UpmResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			try {
				// 得到模块 相当于一个upm文件
				UpmModuleVO upmModuleVO = resource.getUpmModuleVo();
				// 得到模块下面的所有<接口——实现类>组
				// UpmComponentVO 为一对接口和实现类
				List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
				if (pubComponentVoList != null) {
					for (UpmComponentVO upmComponentVO : pubComponentVoList) {
						String interfaceName = upmComponentVO.getInterfaceName();
						String implementationName = upmComponentVO.getImplementationName();
						if (interfaceName.length() != interfaceName.trim().length()) {
							noteBuilder.append("接口：" + interfaceName + "在" + upmModuleVO.getModuleName()
									+ "-upm文件有有空格存在!\n");
						}
						if (implementationName.length() != implementationName.trim().length()) {
							noteBuilder.append("实现类：" + implementationName + "在" + upmModuleVO.getModuleName()
									+ "-upm文件有有空格存在!\n");
						}
					}
				}
			} catch (ResourceParserException e1) {
				noteBuilder.append(String.format("Upm文件操作异常，具体信息: %s\n", e1.getMessage()));
			} finally {
				if (noteBuilder.toString().trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}

}
