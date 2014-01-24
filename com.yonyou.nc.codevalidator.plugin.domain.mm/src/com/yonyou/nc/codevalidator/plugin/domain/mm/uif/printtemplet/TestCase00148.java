package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.printtemplet;

import java.util.HashSet;
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
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "ϵͳĬ��ģ�����Ƿ�ָ���˲����ڵĴ�ӡģ�塣", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, coder = "wangfra", relatedIssueId = "148")
public class TestCase00148 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition{

	private static final String ERR1 = "�ڵ�δ�����ӡģ��";  //������Ϣ����
	private static final String ERR2 = "�ڵ����Ĵ�ӡģ�岻����"; 
	
    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] nodecodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
    	MapSet<String, String> funnodeTempletMapSet=getPrintTemplateIDOfFunnodes(ruleExecContext);
    	Set<String> ctemplateidSet=getAllPrintTemplate(ruleExecContext);
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
            if (null !=ctemplateidSet && !ctemplateidSet.containsAll(ctemplateidOfNodeSet) ) {
            	detailInforResult.put(ERR2, nodecode);
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
	 * �����ݿ�� pub_print_template �в�ѯ���д�ӡģ�塣
	 * @param ruleExecContext
	 * @return
	 * @throws ResourceParserException
	 */
	private Set<String> getAllPrintTemplate(IRuleExecuteContext ruleExecContext)throws ResourceParserException {
		DataSet printTemplates = this.executeQuery(ruleExecContext, this.getAllTemplatesSql());
		Set<String> ctemplateidSet = new HashSet<String>();
		for (DataRow dataRow : printTemplates.getRows()) {
			ctemplateidSet.add((String) dataRow.getValue("ctemplateid"));
		}
		return ctemplateidSet;
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
        sql.append(" select ctemplateid from pub_print_template where  isnull(dr,0)=0 and pk_corp='@@@@'");
        return sql.toString();
    }
//	/**
//	 * �����нڵ���м��
//	 * @param ruleExecContext
//	 * @param result
//	 * @return
//	 * @throws ResourceParserException
//	 */
//	private IRuleExecuteResult checkAllNodes(IRuleExecuteContext ruleExecContext,
//			ResourceRuleExecuteResult result) throws ResourceParserException{
//	
//		 return checkSpecifiedNode(ruleExecContext, result, nodecodes);;
//	}
//	
	
//	private IRuleExecuteResult checkSpecifiedNode(
//			IRuleExecuteContext ruleExecContext,
//			ResourceRuleExecuteResult result, String[] nodecodes, MapSet<String, String> funnodeTempletMapSet, MapSet<String, String> funnodeTempletMapSet2)
//			throws ResourceParserException {
//		// ���μ��ÿ���ڵ�
//        for (String nodecode : nodecodes) {
//            // ��øýڵ�����ϵͳ��ӡģ��
//            DataSet printTemplates = this.executeQuery(ruleExecContext, this.getPrintTemplateIDSql(nodecode));
//            if (printTemplates.isEmpty()) {
//                result.addResultElement(nodecode, "��ǰ�ڵ���δ�����ӡģ�壡\n");
//                continue;
//            }
//            Set<String> templateidSet = new HashSet<String>();
//            for (DataRow printTemplet : printTemplates.getRows()) {
//                templateidSet.add((String) printTemplet.getValue("templateid"));
//            }
//            // �Է���Ĵ�ӡģ����м��
//            this.checkPrintTemplate(ruleExecContext, result, nodecode,
//                    templateidSet.toArray(new String[templateidSet.size()]));
//        }
//        return result;
//	}

//    private void checkPrintTemplate(IRuleExecuteContext ruleExecContext, ResourceRuleExecuteResult result,
//            String nodecode, String[] printTemplateIDs) throws ResourceParserException {
//        DataSet printTemplates = this.executeQuery(ruleExecContext, this.getTemplatesSql(printTemplateIDs));
//        if (printTemplates.isEmpty()) {
//            result.addResultElement(nodecode, "ϵͳĬ��ģ����ָ���˲����ڵĴ�ӡģ�壡\n");
//        }
//    }

//    /**
//     * ��ѯ�ڵ�����ϵͳ��ӡģ���id
//     * 
//     * @param funcode
//     * @return
//     */
//    private String getPrintTemplateIDSql(String funcode) {
//        SQLBuilder sql = new SQLBuilder();
//        sql.append("select templateid from pub_systemplate_base s where isnull(s.dr,0) = 0 and s.tempstyle = 3 and s.funnode =  '"
//                + funcode + "'");
//        return sql.toString();
//    }
//
//    /**
//     * ����id��ѯ����Ĵ�ӡģ��ı���
//     * 
//     * @param templateids
//     * @return
//     */
//    private String getTemplatesSql(String[] templateids) {
//        SQLBuilder sql = new SQLBuilder();
//        sql.append(" select ctemplateid from pub_print_template where ");
//        sql.append(" ctemplateid ", templateids);
//        sql.append(" and isnull(dr,0)=0 and pk_corp='@@@@'");
//        return sql.toString();
//    }
//    
  
}
