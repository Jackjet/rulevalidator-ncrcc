package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * 检查后台实现类的接口方法边界必须有异常的marsh处理
 * 
 * @author qiaoyanga
 * @reporter
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE,
		description = "检查后台实现类的接口方法边界必须有异常的marsh处理", memo = "", solution = "",
		subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "259", coder = "qiaoyanga")
public class TestCase00259 extends AbstractRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		UpmResourceQuery upmResourceQuery = new UpmResourceQuery();
		upmResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
		for (UpmResource upmResource : upmResources) {
			UpmModuleVO upmModuleVo = upmResource.getUpmModuleVo();
			Map<String, String> serviceImplToItfClassName = this.getServiceImplClassName(upmModuleVo);
			List<String> serviceImplClassNameList = Arrays.asList(serviceImplToItfClassName.keySet().toArray(
					new String[0]));
			JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
			javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
			javaResourceQuery.setResPrivilege(JavaResPrivilege.PRIVATE);
			javaResourceQuery.setClassNameFilterList(serviceImplClassNameList);
			List<JavaClassResource> javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);

			for (final JavaClassResource javaClassResource : javaResourceList) {
				try {
					String itfName = serviceImplToItfClassName.get(javaClassResource.getJavaCodeClassName());
					Class<?> loadClass = ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(
							ruleExecContext.getBusinessComponent().getProjectName(), itfName);
					Method[] methods = loadClass.getMethods();
					final List<String> itfMethodnames = new ArrayList<String>();
					for (Method method : methods) {
						itfMethodnames.add(method.getName());
					}
					CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
					final StringBuilder noteBuilder = new StringBuilder();
					VoidVisitorAdapter<Void> visitor = new VoidVisitorAdapter<Void>() {
						@Override
						public void visit(MethodDeclaration n, Void v) {
							if (itfMethodnames.contains(n.getName())) {
								BlockStmt body = n.getBody();
								if (body == null) {
									return;
								}
								List<Statement> stmts = body.getStmts();
								if (stmts == null) {
									return;
								}
								boolean isContainsMarch = false;
								for (Statement stmt : stmts) {
									if (stmt.toString().contains("ExceptionUtils.marsh")) {
										isContainsMarch = true;
										break;
									}
								}
								if (!isContainsMarch) {
									noteBuilder.append(String.format("方法 %s 未包含 marsh异常处理!\n", n.getName()));
								}
							}
						}
					};
					visitor.visit(compilationUnit, null);
					if (noteBuilder.toString().length() > 0) {
						result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
					}
				} catch (ParseException e) {
					Logger.error(e.getMessage(), e);
				} catch (IOException e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}

		return result;
	}

	/**
	 * @param upmModuleVO
	 * @return Map<String, String>
	 */
	private Map<String, String> getServiceImplClassName(UpmModuleVO upmModuleVO) {
		Map<String, String> result = new HashMap<String, String>();
		List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
		if (pubComponentVoList != null && pubComponentVoList.size() > 0) {
			for (UpmComponentVO upmComponentVO : pubComponentVoList) {
				if (upmComponentVO.getInterfaceName() != null) {
					result.put(upmComponentVO.getImplementationName(), upmComponentVO.getInterfaceName());
				}
			}
		}
		List<UpmComponentVO> priComponentVoList = upmModuleVO.getPriComponentVoList();
		if (priComponentVoList != null && priComponentVoList.size() > 0) {
			for (UpmComponentVO upmComponentVO : priComponentVoList) {
				if (upmComponentVO.getInterfaceName() != null) {
					result.put(upmComponentVO.getImplementationName(), upmComponentVO.getInterfaceName());
				}
			}
		}
		return result;
	}

}
