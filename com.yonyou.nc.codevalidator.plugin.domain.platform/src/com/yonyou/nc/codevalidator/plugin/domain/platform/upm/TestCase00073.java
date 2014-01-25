package com.yonyou.nc.codevalidator.plugin.domain.platform.upm;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * 判断UPM文件中的实现类是否存在默认的构造器
 * 
 * @author mazhqa
 * 
 */
@RuleDefinition(catalog = CatalogEnum.OTHERCONFIGFILE, description = " NC ejb upm中配置的实现类必须有无参数的默认构造类 ", memo = "",
		solution = "", specialParamDefine = { "" }, subCatalog = SubCatalogEnum.OCF_UPM, coder = "mazhqa",
		relatedIssueId = "73", executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.COMPILE)
public class TestCase00073 extends AbstractUpmQueryRuleDefinition {

	@Override
	protected UpmResourceQuery getUpmResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery query = new UpmResourceQuery();
		query.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return query;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
			throws ResourceParserException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
		BusinessComponent businessComponent = ruleExecContext.getBusinessComponent();
		for (UpmResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			try {
				UpmModuleVO upmModuleVO = resource.getUpmModuleVo();
				List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
				if (pubComponentVoList != null) {
					for (UpmComponentVO upmComponentVO : pubComponentVoList) {
						String implementationName = upmComponentVO.getImplementationName();
						try {
							@SuppressWarnings("rawtypes")
							Class implementationClass = classLoaderUtils.loadClass(businessComponent.getProjectName(),
									implementationName);
							implementationClass.getConstructor();
						} catch (RuleClassLoadException e) {
							noteBuilder.append(String.format("类型未加载到: %s", e.getMessage()));
						} catch (SecurityException e) {
							noteBuilder.append(String.format("类型未加载到: %s", e.getMessage()));
						} catch (NoSuchMethodException e) {
							noteBuilder.append(String.format("类型: %s无缺省构造函数！", e.getMessage()));
						}
					}
				}
			} catch (ResourceParserException e1) {
				noteBuilder.append(String.format("Upm文件操作异常，具体信息: %s", e1.getMessage()));
			} finally {
				if (noteBuilder.toString().trim().length() != 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}

}
