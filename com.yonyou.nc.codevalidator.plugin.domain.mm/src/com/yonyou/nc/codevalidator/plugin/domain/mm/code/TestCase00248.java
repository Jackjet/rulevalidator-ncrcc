package com.yonyou.nc.codevalidator.plugin.domain.mm.code;


import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;

/**
 * 打印精度处理类实现IBeforePrintDataProcess接口
 * @author gaojf
 *
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY,catalog = CatalogEnum.JAVACODE, description = "检查打印精度处理类实现IBeforePrintDataProcess接口", memo = "", solution = "", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "248", coder = "gaojf")
public class TestCase00248 extends AbstractJavaQueryRuleDefinition{

	@Override
	protected JavaResourceQuery getJavaResourceQuery(
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
        return javaResourceQuery;
	}

	@Override
	protected IRuleExecuteResult processJavaRules(
			IRuleExecuteContext ruleExecContext,
			List<JavaClassResource> resources) throws RuleBaseException {

        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		List<JavaClassResource> javaclasses = new ArrayList<JavaClassResource>();
		for(JavaClassResource javaClassResource:resources){
			if(javaClassResource.getJavaCodeClassName().indexOf(".scale")!=-1&&
					javaClassResource.getResourceFileName().toLowerCase().indexOf("print")!=-1){
				javaclasses.add(javaClassResource);
			}
		}
		for(JavaClassResource javaClassResource:javaclasses){
				String className = javaClassResource.getJavaCodeClassName();
				Class<?> loadClass = ClassLoaderUtilsFactory
				.getClassLoaderUtils().loadClass(
						ruleExecContext.getBusinessComponent()
								.getProjectName(), className);			
				
				Class<?>[] classes = loadClass.getInterfaces();
				for(Class<?> z:classes){
					if(z.getSimpleName().equals("IBeforePrintDataProcess"))
					return result;
				}
				result.addResultElement(className, "没有实现接口IBeforePrintDataProcess.");			
		}
	
		return result;
	}

}
