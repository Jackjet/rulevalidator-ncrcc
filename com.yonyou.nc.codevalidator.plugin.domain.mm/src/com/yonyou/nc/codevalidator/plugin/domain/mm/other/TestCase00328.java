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
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.OTHERCONFIGFILE, subCatalog = SubCatalogEnum.OCF_UPM, description = "��upm�нӿ���public�㣬ʵ����private�㡣", specialParamDefine = { "��ģ����upm�ļ���ע���ͳһ��EJB����" }, coder = "wangfra", relatedIssueId = "328")
public class TestCase00328 extends AbstractUpmQueryRuleDefinition {

	private static final String ERR1 = "ע��Ľӿ�Ӧλ��PUBLIC��";  //������Ϣ����
	private static final String ERR2 = "ע���ʵ����Ӧλ��PRIVATE��"; 
	@Override
	protected IRuleExecuteResult processUpmRules (
			IRuleExecuteContext ruleExecContext, List<UpmResource> resources) throws RuleBaseException {
	   
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
//		IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory
//				.getClassLoaderUtils();
		for (UpmResource upmResource : resources) {
			//���μ��ÿ��UPM�ļ�
			MapSet<String, String> errorInforResult=new MapSet<String, String>();
			List<UpmComponentVO> pubList = null;
			try {
				pubList = upmResource.getUpmModuleVo().getPubComponentVoList();
			} catch (ResourceParserException e1) {
				result.addResultElement(upmResource.getResourcePath(), String.format("Upm�ļ������쳣��������Ϣ: %s\n", e1.getMessage()));
			}
			List<String> classNameFilter = new ArrayList<String>();
			List<JavaClassResource> javaClzResources =  new ArrayList<JavaClassResource>();
			//���μ��UPM�ļ���ע���ÿ���������Ϣ
			for (UpmComponentVO vo : pubList) {
				String interfaceName = vo.getInterfaceName();//��ȡ�ӿ�����
				if(null != interfaceName){
					// ���Խӿ��Ƿ���public��
					classNameFilter.add(interfaceName);
					javaClzResources = this.getPublicJavaClassResource(ruleExecContext,classNameFilter);//�����������ص�CC�϶�Ӧ��java�ļ���������ز����Ͳ�ͨ��
					if(javaClzResources.size()<1){
						errorInforResult.put(ERR1, interfaceName);
//						errorInfoBuilder.append("�ӿ�" + interfaceName + "Ӧ����publicĿ¼��\n");
					}
					classNameFilter.clear();
					javaClzResources.clear();
					
					// ����ʵ�����Ƿ���private��
					String implementationName = vo.getImplementationName();
					classNameFilter.add(implementationName);
					javaClzResources = this.getPrivateJavaClassResource(ruleExecContext,classNameFilter);
					if(javaClzResources.size()<1){
						errorInforResult.put(ERR2, implementationName);
//						errorInfoBuilder.append("ʵ����" + implementationName + "Ӧ����privateĿ¼��\n");
					}
					classNameFilter.clear();
					javaClzResources.clear();
				}

			}
			StringBuilder errorInfoBuilder = new StringBuilder();
			if (null !=errorInforResult.get(ERR1)) {
				errorInfoBuilder.append("upm�ļ�["+upmResource.getResourceFileName()+"]�����õĽӿ�"+ errorInforResult.get(ERR1) + "Ӧ����publicĿ¼��\n");
			}
			if (null !=errorInforResult.get(ERR2)) {
				errorInfoBuilder.append("upm�ļ�["+upmResource.getResourceFileName()+"]�����õ�ʵ����"+ errorInforResult.get(ERR2) + "Ӧ����privateĿ¼��\n");
			}
			if (errorInfoBuilder.toString().trim().length() > 0) {
				result.addResultElement(upmResource.getResourcePath(), errorInfoBuilder.toString());
			}
		}
			
		return result;
	}


	 /**
     * ��ȡ����ģ��.java����Դ�ļ�
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
     * ��ȡ��̨.java����Դ�ļ�
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
