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
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "系统默认模板中是否指定了不存在的打印模板。", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, coder = "wangfra", relatedIssueId = "148")
public class TestCase00148 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition{

	private static final String ERR1 = "节点未分配打印模板";  //错误信息分类
	private static final String ERR2 = "节点分配的打印模板不存在"; 
	
    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] nodecodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
    	MapSet<String, String> funnodeTempletMapSet=getPrintTemplateIDOfFunnodes(ruleExecContext);
    	Set<String> ctemplateidSet=getAllPrintTemplate(ruleExecContext);
        if (MMValueCheck.isEmpty(nodecodes)) {
        	  Logger.warn("当前规则功能节点参数为空，将检查所有功能节点！");
        	  nodecodes=(String[]) funnodeTempletMapSet.keySet().toArray();
        }
    
        Set<String> ctemplateidOfNodeSet = null;
        MapSet<String, String> detailInforResult=new MapSet<String, String>();
    	// 依次检查每个节点
        for (String nodecode : nodecodes) {
            // 获得该节点分配的系统打印模板
        	ctemplateidOfNodeSet = funnodeTempletMapSet.get(nodecode);
        	
        	//如果此节点未分配打印模板，则跳出本次循环，检查下一个节点
            if (null ==ctemplateidOfNodeSet || ctemplateidOfNodeSet.isEmpty()) {
            	detailInforResult.put(ERR1, nodecode);
                continue;
            }
            
            //如果该节点分配的打印模板在 pub_print_template 表中未查到，则说明该节点分配了不存在的打印模板
            if (null !=ctemplateidSet && !ctemplateidSet.containsAll(ctemplateidOfNodeSet) ) {
            	detailInforResult.put(ERR2, nodecode);
            }
        }
        
        return processErrorInfor(result, detailInforResult);
    }

    /**
     * 处理错误提示信息
     * @param result
     * @param detailInforResult
     * @return
     */
    private ResourceRuleExecuteResult processErrorInfor(ResourceRuleExecuteResult result,
    		MapSet<String, String> detailInforResult) {
    	if(null !=detailInforResult.get(ERR1)&&detailInforResult.size()>0){
            result.addResultElement(ERR1, "功能节点编码为" + detailInforResult.get(ERR1)
                + "的节点尚未分配打印模板！请检查是否要分配！\n");
        }
        if(null !=detailInforResult.get(ERR2)&&detailInforResult.size()>0){
            result.addResultElement(ERR2, "功能节点编码为" + detailInforResult.get(ERR2)
                    + "的节点所分配的打印模板不存在！\n");
         }
		return result;
	}
    
	/**
	 * 从数据库表 pub_systemplate_base 中查询每个节点所分配的打印模板。这种对应关系存在MapSet容器中。
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
	 * 从数据库表 pub_print_template 中查询所有打印模板。
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
     * 构造查询每个节点所分配的打印模板的SQL
     * @return
     */
    private String getAllPrintTemplateIDSql() {
    	SQLBuilder result = new SQLBuilder();
        result.append("select funnode,templateid from pub_systemplate_base s where isnull(s.dr,0) = 0 and s.tempstyle = 3  ");
        return result.toString();
    }
    
    /**
     * 构造查询所有打印模板的SQL
     * @return
     */
    private String getAllTemplatesSql() {
        SQLBuilder sql = new SQLBuilder();
        sql.append(" select ctemplateid from pub_print_template where  isnull(dr,0)=0 and pk_corp='@@@@'");
        return sql.toString();
    }
//	/**
//	 * 对所有节点进行检查
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
//		// 依次检查每个节点
//        for (String nodecode : nodecodes) {
//            // 获得该节点分配的系统打印模板
//            DataSet printTemplates = this.executeQuery(ruleExecContext, this.getPrintTemplateIDSql(nodecode));
//            if (printTemplates.isEmpty()) {
//                result.addResultElement(nodecode, "当前节点尚未分配打印模板！\n");
//                continue;
//            }
//            Set<String> templateidSet = new HashSet<String>();
//            for (DataRow printTemplet : printTemplates.getRows()) {
//                templateidSet.add((String) printTemplet.getValue("templateid"));
//            }
//            // 对分配的打印模板进行检查
//            this.checkPrintTemplate(ruleExecContext, result, nodecode,
//                    templateidSet.toArray(new String[templateidSet.size()]));
//        }
//        return result;
//	}

//    private void checkPrintTemplate(IRuleExecuteContext ruleExecContext, ResourceRuleExecuteResult result,
//            String nodecode, String[] printTemplateIDs) throws ResourceParserException {
//        DataSet printTemplates = this.executeQuery(ruleExecContext, this.getTemplatesSql(printTemplateIDs));
//        if (printTemplates.isEmpty()) {
//            result.addResultElement(nodecode, "系统默认模板中指定了不存在的打印模板！\n");
//        }
//    }

//    /**
//     * 查询节点分配的系统打印模板的id
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
//     * 根据id查询分配的打印模板的编码
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
