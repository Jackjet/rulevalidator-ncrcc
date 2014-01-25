package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.authority;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util.SQLBuilder;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapSet;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * @since 1.0
 * @version 1.0.0.0
 * @author wangfra
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "���ָ���ļ���֧��ʹ��Ȩ�޵��ֶΣ����ϡ����Ϸ��ࡢ�������ĺͲ��š������ֶ�Ӧ����Ԫ����������", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, coder = "wangfra", relatedIssueId = "323")
public class TestCase00323 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition{

	
	private static final String ERR1 = "�ڵ�δ�����ѯģ��";  //������Ϣ����
	private static final String ERR2 = "�ڵ����Ĳ�ѯģ�岻����"; 
	private static final String ERR3 = "���ϵȲ�ѯ�����Ƿ�Ԫ��������"; 
    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] nodecodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        MapSet<String, String> funnodeTempletMapSet=getQueryTemplateIDOfFunnodes(ruleExecContext);
        MapList<String,Map<String,String>> ctemplateidCodeMap=getSpeciQueryCon(ruleExecContext);
    	
        if (MMValueCheck.isEmpty(nodecodes)) {
        	  Logger.warn("��ǰ�����ܽڵ����Ϊ�գ���������й��ܽڵ㣡");
        	  nodecodes= funnodeTempletMapSet.keySet().toArray(new String[funnodeTempletMapSet.keySet().size()]);
        }
    
        
        Set<String> ctemplateidOfNodeSet = null;
    	MapSet<String, String> detailInforResult=new MapSet<String, String>();
        // ���μ��ÿ���ڵ�
        for (String nodecode : nodecodes) {
            // ��øýڵ��Ӧ�����в�ѯģ��
        	ctemplateidOfNodeSet = funnodeTempletMapSet.get(nodecode);
        	
        	//����˽ڵ�δ�����ѯģ�壬����������ѭ���������һ���ڵ�
            if (null ==ctemplateidOfNodeSet || ctemplateidOfNodeSet.isEmpty()) {
            	detailInforResult.put(ERR1, nodecode);
                continue;
            }
            
            //����ýڵ����Ĳ�ѯģ��id�� pub_query_condition����δ�鵽����˵���ýڵ�����˲����ڵĲ�ѯģ��
            if (!ctemplateidCodeMap.keySet().containsAll(ctemplateidOfNodeSet) ) {
            	detailInforResult.put(ERR2, nodecode);
            }
            //ͨ������Ĳ�����ȡ�ýڵ����Ĳ����Ǵ��ڵĲ�ѯģ��
            ctemplateidOfNodeSet.retainAll(ctemplateidCodeMap.keySet());
            //����ѯģ������ϵ��ֶ��Ƿ���Ԫ��������
            for (String ctemplateid : ctemplateidOfNodeSet) {
            	for(Map<String, String> consuldcode_ifmd:ctemplateidCodeMap.get(ctemplateid)){
            		String a =(String)consuldcode_ifmd.values().toArray()[0];
            		   if (!"N".equals(a)) {
            			   detailInforResult.put(ERR3, nodecode);
                       }
            	}
            }
        }
        return processErrorInfor(result, detailInforResult);
    }

   
    /**
     * ���������ʾ��Ϣ
     * @param result
     * @param detailInforResult
     * @return
     */
	private ResourceRuleExecuteResult processErrorInfor(ResourceRuleExecuteResult result,
			MapSet<String, String> detailInforResult) {
		if(null !=detailInforResult.get(ERR1)&&detailInforResult.size()>0){
            result.addResultElement(ERR1, "���ܽڵ����Ϊ" + detailInforResult.get(ERR1)
                + "�Ľڵ���δ�����ѯģ�壡�����Ƿ�Ҫ���䣡\n");
        }
        if(null !=detailInforResult.get(ERR2)&&detailInforResult.size()>0){
            result.addResultElement(ERR2, "���ܽڵ����Ϊ" + detailInforResult.get(ERR2)
                    + "�Ľڵ�������Ĳ�ѯģ�岻���ڣ�\n");
         }
        if(null !=detailInforResult.get(ERR3)&&detailInforResult.size()>0){
            result.addResultElement(ERR3, "���ܽڵ����Ϊ" + detailInforResult.get(ERR3)
                + "�Ľڵ�,���ѯģ���е����ϡ����Ż�������Ӧ����ΪԪ����������\n");
        }
     
		return result;
	}

    /**
	 * �����ݿ�� pub_systemplate_base �в�ѯÿ���ڵ�������Ĳ�ѯģ�塣���ֶ�Ӧ��ϵ����MapSet�����С�
	 * @param ruleExecContext
	 * @return
	 * @throws ResourceParserException
	 */
	private MapSet<String, String> getQueryTemplateIDOfFunnodes(IRuleExecuteContext ruleExecContext)throws ResourceParserException {
		   DataSet printTemplates = this.executeQuery(ruleExecContext, this.getAllQueryTemplateIDSql());
		   MapSet<String, String> funcodeTempletMapSet = new MapSet<String, String>();
		     for (DataRow dataRow : printTemplates.getRows()) {
	            funcodeTempletMapSet.put( (String) dataRow.getValue("funnode"), (String) dataRow.getValue("templateid"));
	         }
		     
		return funcodeTempletMapSet;
	}
	/**
	 * �����ݿ�� pub_query_condition �в�ѯ���ϵ���������pk_templet��consult_code��if_notmdcondition�Ķ�Ӧ��ϵ����MapList�����С�
	 * @param ruleExecContext
	 * @return
	 * @throws ResourceParserException
	 */
	private MapList<String,Map<String,String>>  getSpeciQueryCon(IRuleExecuteContext ruleExecContext)throws ResourceParserException {
		DataSet printTemplates = this.executeQuery(ruleExecContext, this.getSpecifiedQueryConditonSql());
		MapList<String,Map<String,String>> ctemplateidCodeMap = new MapList<String,Map<String,String>>();
		 HashMap<String,String> hashMap =	new HashMap<String,String>();
		for (DataRow dataRow : printTemplates.getRows()) {
			 hashMap.put((String) dataRow.getValue("consult_code"),(String) dataRow.getValue("if_notmdcondition"));
			 @SuppressWarnings("unchecked")
			HashMap<String, String> tempMap = (HashMap<String, String>) hashMap.clone();
			ctemplateidCodeMap.put((String) dataRow.getValue("pk_templet"), tempMap);
			hashMap.clear();
		}
		return ctemplateidCodeMap;
	}
	
	  /**
     * ����ӹ��ܽڵ�Ĭ��ģ����в�ѯ��Ĭ�ϲ�ѯģ��ע������
     * @return
     */
    private String getAllQueryTemplateIDSql() {
    	SQLBuilder result = new SQLBuilder();
        result.append("select s.funnode,s.templateid from pub_systemplate_base s where isnull(s.dr,0) = 0 and s.tempstyle = 1 ");
        return result.toString();
    }
	
	
    private String getSpecifiedQueryConditonSql() {
    	SQLBuilder result = new SQLBuilder();
        result.append("select pq.pk_templet,pq.consult_code,pq.if_notmdcondition from  pub_query_condition pq where isnull(pq.dr,0) = 0 and (pq.consult_code like '����%' or  pq.consult_code like '����%'  or  pq.consult_code like 'UAP-��������%')");
        return result.toString();
    }
}
