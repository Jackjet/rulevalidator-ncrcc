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
		solution = "�Զ����쳣������Ҫ�ṩ����Throwable�����Ĺ��캯��",
		description = "�Զ����쳣����û���ṩ����Throwable�����Ĺ��캯����������ʹ�ø��쳣����ʱ�޷���ԭʼ�쳣��Ϣ���ݳ�ȥ������쳣���Ե������󣬲����ڶ�λ�������ԭʼ��Ϣ��",
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
	 * �����̳���nc.vo.pub.BusinessException��nc.vo.pub.
	 * BusinessRuntimeException���Զ����쳣�� ��⹹�췽���Ĳ������ͣ����Ƿ���Throwable����
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
							if (Throwable.class.isAssignableFrom(paramType)) {// �ж�Throwable�Ƿ����䳬��
								containThrowableParam = true;
								break;
							}
						}
						if (containThrowableParam) {
							break;
						}
					}
					if (!containThrowableParam) {
						result.addResultElement(javaClassResource.getResourcePath(), "�Զ����쳣����Ҫ�ṩ����Throwable�����Ĺ��캯��");
					}
				}
			} catch (RuleClassLoadException e) {
				throw new ResourceParserException(e);
			}
		}
		return result;
	}

}
