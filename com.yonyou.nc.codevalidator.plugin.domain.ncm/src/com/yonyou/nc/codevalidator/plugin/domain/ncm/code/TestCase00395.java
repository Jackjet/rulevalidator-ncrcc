package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.lang.reflect.Constructor;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonParamProcessUtils;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION,
		solution = "自定义异常类型需要提供含有Throwable参数的构造函数",
		description = "自定义异常类型没有提供含有Throwable参数的构造函数，导致在使用该异常类型时无法将原始异常信息传递出去，造成异常被吃掉的现象，不利于定位问题的最原始信息。",
		coder = "wumr3", specialParamDefine = { "" }, relatedIssueId = "395", repairLevel = RepairLevel.SUGGESTREPAIR,
		executePeriod = ExecutePeriod.COMPILE)
public class TestCase00395 extends AbstractJavaQueryRuleDefinition {

	private static final String BUSINESS_EXCEPTION = "nc.vo.pub.BusinessException";
	private static final String BUSINESSRUNTIME_EXCEPTION = "nc.vo.pub.BusinessRuntimeException";

	@Override
	protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext)
			throws ResourceParserException {
		JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
		javaResourceQuery.setContainExtraWrapper(CommonParamProcessUtils.getCodeContainsExtraFolder(ruleExecContext));
		javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		javaResourceQuery.setResPrivilege(JavaResPrivilege.PUBLIC);
		return javaResourceQuery;
	}

	/**
	 * 遍历继承了nc.vo.pub.BusinessException和nc.vo.pub.
	 * BusinessRuntimeException的自定义异常类 检测构造方法的参数类型，看是否含有Throwable类型
	 */
	@Override
	protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
			throws ResourceParserException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		JavaResourceQuery javaResourceQuery = getJavaResourceQuery(ruleExecContext);
		BusinessComponent businessComponent = javaResourceQuery.getBusinessComponent();

		for (JavaClassResource javaClassResource : resources) {
			try {
				String projectName = businessComponent.getProjectName();
				String className = javaClassResource.getJavaCodeClassName();
				boolean isNcException = ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(projectName,
						className, BUSINESS_EXCEPTION)
						|| ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(projectName, className,
								BUSINESSRUNTIME_EXCEPTION);
				if (isNcException) {
					boolean containThrowableParam = false;
					Constructor<?>[] constructors = ClassLoaderUtilsFactory.getClassLoaderUtils()
							.loadClass(projectName, className).getConstructors();
					for (Constructor<?> constructor : constructors) {
						Class<?>[] parameterTypes = constructor.getParameterTypes();
						for (Class<?> paramType : parameterTypes) {
							if (Throwable.class.isAssignableFrom(paramType)) {// 判断Throwable是否是其超类
								containThrowableParam = true;
								break;
							}
						}
						if (containThrowableParam) {
							break;
						}
					}
					if (!containThrowableParam) {
						result.addResultElement(javaClassResource.getResourcePath(), "自定义异常类型要提供含有Throwable参数的构造函数");
					}
				}
			} catch (RuleClassLoadException e) {
				throw new ResourceParserException(e);
			}
		}
		return result;
	}

}
