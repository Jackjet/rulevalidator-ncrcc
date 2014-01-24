package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonParamProcessUtils;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.PublicRuleDefinitionParam;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION,
		description = "����ӿڷ����еĲ����ͷ���ֵ�����ǿ����л���,�����ͷ���ֵ�޷����л��������紫��.", coder = "wumr3",
		solution = "����ӿڷ���Ҫʵ��java.io.Serializable", relatedIssueId = "425", executePeriod = ExecutePeriod.COMPILE)
@PublicRuleDefinitionParam(params = { CommonRuleParams.CODECONTAINEXTRAFOLDER })
public class TestCase00425 extends AbstractUpmQueryRuleDefinition {

	protected UpmResourceQuery getUpmResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery resourceQuery = new UpmResourceQuery();
		resourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return resourceQuery;

	}

	private static final String SEQUENCE = "java.io.Serializable";

	private static final List<String> EXCLUDE_CHECK_LIST = Arrays.asList(new String[] { "java.lang.Object",
			"java.util.Map", "java.util.Set", "java.util.List" });

	@Override
	protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
			throws ResourceParserException {
		UpmResourceQuery upmResourceQuery = getUpmResourceQuery(ruleExecContext);
		List<UpmResource> upmResources;
		List<String> upmInterfaceNames = new ArrayList<String>();

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		try {
			upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
			for (UpmResource upmResource : upmResources) {
				List<UpmComponentVO> upmComponentVOs = null;
				UpmModuleVO upmModuleVO = upmResource.getUpmModuleVo();
				upmComponentVOs = upmModuleVO.getPubComponentVoList();
				for (UpmComponentVO upmComponentVO : upmComponentVOs) {
					if (upmComponentVO.getInterfaceName() != null) {
						upmInterfaceNames.add(upmComponentVO.getInterfaceName());
					}
				}
			}
		} catch (RuleBaseException e) {
			throw new ResourceParserException(e);
		}
		JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
		javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		javaResourceQuery.setResPrivilege(JavaResPrivilege.SRC);
		javaResourceQuery.setContainExtraWrapper(CommonParamProcessUtils.getCodeContainsExtraFolder(ruleExecContext));
		javaResourceQuery.setClassNameFilterList(upmInterfaceNames);
		StringBuilder nodeBuilder = new StringBuilder();
		for (String interfaceName : upmInterfaceNames) {
			String projectName = javaResourceQuery.getBusinessComponent().getProjectName();
			Class<?> loadClass;
			try {
				loadClass = ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(projectName, interfaceName);
				Method[] declaredMethods = loadClass.getMethods();
				if (declaredMethods.length > 0) {
					for (Method method : declaredMethods) {
						Class<?>[] parameterTypes = method.getParameterTypes();// ���䵽��������
						for (Class<?> parameterType : parameterTypes) {
							while (parameterType.isArray()) {
								parameterType = parameterType.getComponentType();
							}
							String className = null;
							if (!parameterType.isPrimitive()) {
								className = parameterType.getName();
								if (!EXCLUDE_CHECK_LIST.contains(className)) {
									boolean isSEQUENCE = ClassLoaderUtilsFactory.getClassLoaderUtils()
											.isImplementedInterface(projectName, className, SEQUENCE);// �����������Ƿ��ǿ����л�
									if (!isSEQUENCE) {
										nodeBuilder.append(String.format("\r\n%s�ӿ�û�б����л�,%s��������%s�Ƿ����л���",
												interfaceName, method.getName(), className));
									}
								}
							}
						}
						String className = null;
						Class<?> realReturnClass = method.getReturnType();// ���䵽����ֵ����
						while (realReturnClass.isArray()) {
							realReturnClass = realReturnClass.getComponentType();
						}
						if (!realReturnClass.isPrimitive()) {
							className = realReturnClass.getName();
							if (!EXCLUDE_CHECK_LIST.contains(className)) {
								boolean isSEQUENCE = ClassLoaderUtilsFactory.getClassLoaderUtils()
										.isImplementedInterface(projectName, className, SEQUENCE);// �����������Ƿ��ǿ����л�
								if (!isSEQUENCE) {
									nodeBuilder.append(String.format("\r\n%s�ӿ�û�б����л�,%s��������ֵ%s�Ƿ����л���", interfaceName,
											method.getName(), className));
								}
							}
						}
					}
				}

			} catch (RuleClassLoadException e) {
				throw new ResourceParserException(e);
			}
		}
		if (nodeBuilder.length() > 0) {
			result.addResultElement(javaResourceQuery.getBusinessComponent().getDisplayBusiCompName(),
					nodeBuilder.toString());
		}
		return result;
	}
}
