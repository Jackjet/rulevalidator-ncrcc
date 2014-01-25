package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.PublicRuleDefinitionParam;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION,
		description = "重写equals方法后，应当重写hashcode方法。这样避免在使用hash表时，相同值的对象会有多个存储，造成数据混淆。", solution = "重写hashcode方法",
		coder = "wumr3", relatedIssueId = "416", executePeriod = ExecutePeriod.CHECKOUT)
@PublicRuleDefinitionParam(params = { CommonRuleParams.CODECONTAINEXTRAFOLDER })
public class TestCase00416 extends AbstractJavaQueryRuleDefinition {

	@Override
	protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext)
			throws ResourceParserException {
		JavaResourceQuery classResource = new JavaResourceQuery();
		classResource.setBusinessComponent(ruleExecContext.getBusinessComponent());
		classResource.setResPrivilege(JavaResPrivilege.PUBLIC);
		return classResource;
	}

	@Override
	protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
			throws ResourceParserException {
		JavaResourceQuery javaResourceQuery = getJavaResourceQuery(ruleExecContext);
		String projectName = javaResourceQuery.getBusinessComponent().getProjectName();
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (JavaClassResource classResource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			try {
				String className = classResource.getJavaCodeClassName();
				List<String> methodNames = new ArrayList<String>();
				Method[] methods = ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(projectName, className)
						.getDeclaredMethods();
				for (Method method : methods) {
					methodNames.add(method.getName());
				}
				// TODO: 重写原则要么全部重写，要么全部不重写
				if (methodNames.contains("equals")) {
					if (!methodNames.contains("hashCode")) {
						noteBuilder.append(String.format("%s类重写了equals方法但没有重写hashCode方法",
								classResource.getJavaCodeClassName()));
					}
				}
				if (methodNames.contains("hashCode")) {
					if (!methodNames.contains("equals")) {
						noteBuilder.append(String.format("%s类重写了hashCode方法但没有重写equals方法",
								classResource.getJavaCodeClassName()));
					}
				}
			} catch (SecurityException e) {
				throw new ResourceParserException(e);
			} catch (RuleClassLoadException e) {
				throw new ResourceParserException(e);
			}
			if (noteBuilder.length() > 0) {

				result.addResultElement(classResource.getResourcePath(), noteBuilder.toString());
			}
		}
		return result;
	}

}
