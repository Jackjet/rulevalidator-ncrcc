package com.yonyou.nc.codevalidator.plugin.domain.am.upm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractAopQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.AopResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.aop.AopAspectVO;
import com.yonyou.nc.codevalidator.sdk.aop.AopModuleVO;

@RuleDefinition(catalog = CatalogEnum.OTHERCONFIGFILE, description = "检测AOP文件中日志注册重复", relatedIssueId = "869",
		subCatalog = SubCatalogEnum.OCF_AOP, coder = "zhangnane")
public class TestCase00869 extends AbstractAopQueryRuleDefinition {

	@Override
	protected IRuleExecuteResult processAopRules(IRuleExecuteContext ruleExecuteContext, List<AopResource> aopResources)
			throws RuleBaseException {
		Map<String, List<String>> repetKeyToItrInfo = new HashMap<String, List<String>>();
		StringBuilder noteBuilder = new StringBuilder();
		if (aopResources != null && aopResources.size() > 0) {
			for (AopResource aopResource : aopResources) {
				AopModuleVO aopmoduleVO = aopResource.getAopModuleVo();
				List<AopAspectVO> aopAspects = aopmoduleVO.getAopAspectVoList();
				for (AopAspectVO aopAspect : aopAspects) {
					String repetKey = aopAspect.getComponentInterface() + "," + aopAspect.getImplemenationClass();
					List<String> ItrInfo = repetKeyToItrInfo.get(repetKey);
					if (ItrInfo == null || ItrInfo.size() == 0) {
						ItrInfo = new ArrayList<String>();
						ItrInfo.add(aopResource.getResourceFileName());
						repetKeyToItrInfo.put(repetKey, ItrInfo);
					} else {
						ItrInfo.add(aopResource.getResourceFileName());
					}
				}

			}
		}
		Iterator<Entry<String, List<String>>> repetKeyIter = repetKeyToItrInfo.entrySet().iterator();
		while (repetKeyIter.hasNext()) {
			Entry<String, List<String>> repetKeytemp = repetKeyIter.next();
			List<String> list = repetKeytemp.getValue();
			if (list.size() > 1) {
				noteBuilder.append(String.format("\n接口重复注册：%s ", repetKeytemp.getKey()));
				for (String l : list) {
					noteBuilder.append(String.format("位置：%s", l));
				}
			}
		}
		return noteBuilder.toString().equals("") ? new SuccessRuleExecuteResult(getIdentifier())
				: new ErrorRuleExecuteResult(getIdentifier(), noteBuilder.toString());
	}

}
