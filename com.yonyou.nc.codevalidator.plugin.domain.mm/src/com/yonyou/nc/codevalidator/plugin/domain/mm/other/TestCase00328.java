package com.yonyou.nc.codevalidator.plugin.domain.mm.other;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapSet;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
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
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;

/**
 * @since 1.0
 * @version 1.0.0.0
 * @author wangfra
 * 
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.OTHERCONFIGFILE, subCatalog = SubCatalogEnum.OCF_UPM, description = "在upm中接口在public层，实现在private层。", specialParamDefine = { "本模块下upm文件中注册的统一的EJB名称" }, coder = "wangfra", relatedIssueId = "328")
public class TestCase00328 extends AbstractUpmQueryRuleDefinition {

	private static final String ERR1 = "注册的接口应位于PUBLIC端";  //错误信息分类
	private static final String ERR2 = "注册的实现类应位于PRIVATE端"; 
	@Override
	protected IRuleExecuteResult processUpmRules (
			IRuleExecuteContext ruleExecContext, List<UpmResource> resources) throws RuleBaseException {
	   
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
//		IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory
//				.getClassLoaderUtils();
		for (UpmResource upmResource : resources) {
			//依次检查每个UPM文件
			MapSet<String, String> errorInforResult=new MapSet<String, String>();
			List<UpmComponentVO> pubList = null;
			try {
				pubList = upmResource.getUpmModuleVo().getPubComponentVoList();
			} catch (ResourceParserException e1) {
				result.addResultElement(upmResource.getResourcePath(), String.format("Upm文件操作异常，具体信息: %s\n", e1.getMessage()));
			}
			List<String> classNameFilter = new ArrayList<String>();
			List<JavaClassResource> javaClzResources =  new ArrayList<JavaClassResource>();
			//依次检查UPM文件中注册的每个组件的信息
			for (UpmComponentVO vo : pubList) {
				String interfaceName = vo.getInterfaceName();//获取接口类名
				if(null != interfaceName){
					// 测试接口是否在public中
					classNameFilter.add(interfaceName);
					javaClzResources = this.getPublicJavaClassResource(ruleExecContext,classNameFilter);//根据类名加载到CC上对应的java文件，如果加载不到就不通过
					if(javaClzResources.size()<1){
						errorInforResult.put(ERR1, interfaceName);
//						errorInfoBuilder.append("接口" + interfaceName + "应该在public目录下\n");
					}
					classNameFilter.clear();
					javaClzResources.clear();
					
					// 测试实现类是否在private中
					String implementationName = vo.getImplementationName();
					classNameFilter.add(implementationName);
					javaClzResources = this.getPrivateJavaClassResource(ruleExecContext,classNameFilter);
					if(javaClzResources.size()<1){
						errorInforResult.put(ERR2, implementationName);
//						errorInfoBuilder.append("实现类" + implementationName + "应该在private目录下\n");
					}
					classNameFilter.clear();
					javaClzResources.clear();
				}

			}
			StringBuilder errorInfoBuilder = new StringBuilder();
			if (null !=errorInforResult.get(ERR1)) {
				errorInfoBuilder.append("upm文件["+upmResource.getResourceFileName()+"]中配置的接口"+ errorInforResult.get(ERR1) + "应该在public目录下\n");
			}
			if (null !=errorInforResult.get(ERR2)) {
				errorInfoBuilder.append("upm文件["+upmResource.getResourceFileName()+"]中配置的实现类"+ errorInforResult.get(ERR2) + "应该在private目录下\n");
			}
			if (errorInfoBuilder.toString().trim().length() > 0) {
				result.addResultElement(upmResource.getResourcePath(), errorInfoBuilder.toString());
			}
		}
			
		return result;
	}


	 /**
     * 获取公共模块.java的资源文件
     * 
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    private List<JavaClassResource> getPublicJavaClassResource(IRuleExecuteContext ruleExecContext, List<String> classNameFilterList)
            throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        javaResourceQuery.setRuntimeContext(ruleExecContext.getRuntimeContext());
        javaResourceQuery.setClassNameFilterList(classNameFilterList); 
        javaResourceQuery.setResPrivilege(JavaResPrivilege.PUBLIC);
        return ResourceManagerFacade.getResource(javaResourceQuery);

    }
    /**
     * 获取后台.java的资源文件
     * 
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    private List<JavaClassResource> getPrivateJavaClassResource(IRuleExecuteContext ruleExecContext, List<String> classNameFilterList)
    		throws RuleBaseException {
    	JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
    	javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
    	javaResourceQuery.setRuntimeContext(ruleExecContext.getRuntimeContext());
    	javaResourceQuery.setClassNameFilterList(classNameFilterList); 
    	javaResourceQuery.setResPrivilege(JavaResPrivilege.PRIVATE);
    	return ResourceManagerFacade.getResource(javaResourceQuery);
    }
}
