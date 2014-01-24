package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.printtemplet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util.SQLBuilder;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "��ӡģ�����Ҫ���ϱ�����ı������", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, coder = "wangfra", relatedIssueId = "145")
public class TestCase00145 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition{

	private static final String ERR1 = "�ڵ�δ�����ӡģ��";  //������Ϣ����
	private static final String ERR2 = "�ڵ����Ĵ�ӡģ�岻����"; 
	private static final String ERR3 = "ģ�����Ϊ��"; 
	private static final String ERR4 = "ģ����벻���Ϲ淶"; 
	
    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] nodecodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
    	MapSet<String, String> funnodeTempletMapSet=getPrintTemplateIDOfFunnodes(ruleExecContext);
    	Map<String,String> ctemplateidCodeMap=getAllPrintTemplate(ruleExecContext);
        if (MMValueCheck.isEmpty(nodecodes)) {
        	  Logger.warn("��ǰ�����ܽڵ����Ϊ�գ���������й��ܽڵ㣡");
        	  nodecodes=(String[]) funnodeTempletMapSet.keySet().toArray();
        }
    
        Set<String> ctemplateidOfNodeSet = null;
        MapSet<String, String> detailInforResult=new MapSet<String, String>();
    	// ���μ��ÿ���ڵ�
        for (String nodecode : nodecodes) {
            // ��øýڵ�����ϵͳ��ӡģ��
        	ctemplateidOfNodeSet = funnodeTempletMapSet.get(nodecode);
        	
        	//����˽ڵ�δ�����ӡģ�壬����������ѭ���������һ���ڵ�
            if (null ==ctemplateidOfNodeSet || ctemplateidOfNodeSet.isEmpty()) {
            	detailInforResult.put(ERR1, nodecode);
                continue;
            }
            
            //����ýڵ����Ĵ�ӡģ���� pub_print_template ����δ�鵽����˵���ýڵ�����˲����ڵĴ�ӡģ��
            if (!ctemplateidCodeMap.keySet().containsAll(ctemplateidOfNodeSet) ) {
            	detailInforResult.put(ERR2, nodecode);
            }
            
            //ͨ������Ĳ�����ȡ�ýڵ����Ĳ����Ǵ��ڵĴ�ӡģ��
            ctemplateidOfNodeSet.retainAll(ctemplateidCodeMap.keySet());
            //����ӡģ��ı����Ƿ���Ϲ淶
            for (String ctemplateid : ctemplateidOfNodeSet) {
            	if(null ==ctemplateidCodeMap.get(ctemplateid)){//ģ�����Ϊ��
            		detailInforResult.put(ERR3, nodecode);
            	}
            	if (!ctemplateidCodeMap.get(ctemplateid).startsWith(nodecode)) {//ģ����벻���Ϲ淶
            		   detailInforResult.put(ERR4, nodecode);
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
                + "�Ľڵ���δ�����ӡģ�壡�����Ƿ�Ҫ���䣡\n");
        }
        if(null !=detailInforResult.get(ERR2)&&detailInforResult.size()>0){
            result.addResultElement(ERR2, "���ܽڵ����Ϊ" + detailInforResult.get(ERR2)
                    + "�Ľڵ�������Ĵ�ӡģ�岻���ڣ�\n");
         }
        if(null !=detailInforResult.get(ERR3)&&detailInforResult.size()>0){
            result.addResultElement(ERR3, "���ܽڵ����Ϊ" + detailInforResult.get(ERR3)
                + "�Ľڵ�Ĵ�ӡģ�����Ϊ�գ�\n");
        }
        if(null !=detailInforResult.get(ERR4)&&detailInforResult.size()>0){
            result.addResultElement(ERR4, "���ܽڵ����Ϊ" + detailInforResult.get(ERR4)
                    + "�Ľڵ�Ĵ�ӡģ����벻���Ϲ淶��\n");
         }
		return result;
	}

	/**
	 * �����ݿ�� pub_systemplate_base �в�ѯÿ���ڵ�������Ĵ�ӡģ�塣���ֶ�Ӧ��ϵ����MapSet�����С�
	 * @param ruleExecContext
	 * @return
	 * @throws ResourceParserException
	 */
	private MapSet<String, String> getPrintTemplateIDOfFunnodes(IRuleExecuteContext ruleExecContext)throws ResourceParserException {
		   DataSet printTemplates = this.executeQuery(ruleExecContext, this.getAllPrintTemplateIDSql());
		   MapSet<String, String> funcodeTempletMapSet = new MapSet<String, String>();
		     for (DataRow dataRow : printTemplates.getRows()) {
	            funcodeTempletMapSet.put( (String) dataRow.getValue("funnode"), (String) dataRow.getValue("templateid"));
	         }
		     
		return funcodeTempletMapSet;
	}
	/**
	 * �����ݿ�� pub_print_template �в�ѯ���д�ӡģ�塣��ctemplateid��ģ�����vtemplatecode�Ķ�Ӧ��ϵ����Map�����С�
	 * @param ruleExecContext
	 * @return
	 * @throws ResourceParserException
	 */
	private Map<String,String> getAllPrintTemplate(IRuleExecuteContext ruleExecContext)throws ResourceParserException {
		DataSet printTemplates = this.executeQuery(ruleExecContext, this.getAllTemplatesSql());
		Map<String,String> ctemplateidCodeMap = new HashMap<String,String>();
		for (DataRow dataRow : printTemplates.getRows()) {
			ctemplateidCodeMap.put((String) dataRow.getValue("ctemplateid"), (String) dataRow.getValue("vtemplatecode"));
		}
		return ctemplateidCodeMap;
	}
	
	  /**
     * �����ѯÿ���ڵ�������Ĵ�ӡģ���SQL
     * @return
     */
    private String getAllPrintTemplateIDSql() {
    	SQLBuilder result = new SQLBuilder();
        result.append("select funnode,templateid from pub_systemplate_base s where isnull(s.dr,0) = 0 and s.tempstyle = 3  ");
        return result.toString();
    }
    
    /**
     * �����ѯ���д�ӡģ���SQL
     * @return
     */
    private String getAllTemplatesSql() {
        SQLBuilder sql = new SQLBuilder();
        sql.append(" select ctemplateid,vtemplatecode from pub_print_template where  isnull(dr,0)=0 and pk_corp='@@@@'");
        return sql.toString();
    }
}
