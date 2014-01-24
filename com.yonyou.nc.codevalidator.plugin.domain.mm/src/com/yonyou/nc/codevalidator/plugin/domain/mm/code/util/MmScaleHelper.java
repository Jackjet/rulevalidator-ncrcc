package com.yonyou.nc.codevalidator.plugin.domain.mm.code.util;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

public class MmScaleHelper {

	private MmScaleHelper() {
    }

    private static MmScaleHelper helper = new MmScaleHelper();

    public static MmScaleHelper getInstance() {
        return MmScaleHelper.helper;
    }
    
    /**
     * �õ���Ŀ¼�����о����ļ���dirΪһ�����й��˵��ַ���
     * 
     * @param context
     * @return
     * @throws RuleBaseException 
     */
    public List<JavaClassResource> getPrintListFile(IRuleExecuteContext ruleExecContext,String dir) throws RuleBaseException {
    	JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
		List<JavaClassResource> javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);		
		List<JavaClassResource> classlist = this.getJavaPackMap(javaResourceList,dir);
	
        return classlist;
    }
    
    /**
     * ��Դ�ļ����������ӳ��
     * @param javaclasses
     * @return
     */
	private List<JavaClassResource> getJavaPackMap(List<JavaClassResource> javaclasses,String path){
	

	List<JavaClassResource> list = new ArrayList<JavaClassResource>();
	for(JavaClassResource javaClassResource:javaclasses){		
		int temp = javaClassResource.getJavaCodeClassName().indexOf(".scale.");
		if(temp!=-1&&javaClassResource.getJavaCodeClassName().indexOf(path)!=-1){
		list.add(javaClassResource);
		}
	}	
	return list;
}
}
