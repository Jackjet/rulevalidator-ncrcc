package com.yonyou.nc.codevalidator.resparser.executeresult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.impl.ResourceResultElement;

/**
 * 资源规则的执行结果，一般用于除脚本规则执行结果显示
 * @author mazhqa
 * @since V1.0
 */
public class ResourceRuleExecuteResult extends AbstractRuleExecuteResult {

	private List<ResourceResultElement> resultElementList = new ArrayList<ResourceResultElement>();

	public void addResultElement(String resourcePath, String errorDetail) {
		ResourceResultElement resultElement = new ResourceResultElement(resourcePath, errorDetail);
		resultElementList.add(resultElement);
	}

	public void addResultElement(ResourceResultElement resultElement) {
		resultElementList.add(resultElement);
	}

	public List<ResourceResultElement> getResultElementList() {
		return Collections.unmodifiableList(resultElementList);
	}

	@Override
	public String getNote() {
		if (resultElementList == null || resultElementList.isEmpty()) {
			return RESULT_SUCCESS;
		}
		StringBuilder result = new StringBuilder();
		for (ResourceResultElement resultElement : resultElementList) {
			result.append(String.format("资源：%s, \n错误详细信息：\n%s\n", resultElement.getResourcePath(),
					resultElement.getErrorDetail()));
		}
		return result.toString();
	}

	@Override
	public RuleExecuteStatus getRuleExecuteStatus() {
		return resultElementList == null || resultElementList.size() == 0 ? RuleExecuteStatus.SUCCESS : RuleExecuteStatus.FAIL;
	}

}
