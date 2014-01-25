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
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "打印模板编码要符合本领域的编码规则。", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, coder = "wangfra", relatedIssueId = "145")
public class TestCase00145 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition{

	private static final String ERR1 = "节点未分配打印模板";  //错误信息分类
	private static final String ERR2 = "节点分配的打印模板不存在"; 
	private static final String ERR3 = "模板编码为空"; 
	private static final String ERR4 = "模板编码不符合规范"; 
	
    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] nodecodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
    	MapSet<String, String> funnodeTempletMapSet=getPrintTemplateIDOfFunnodes(ruleExecContext);
    	Map<String,String> ctemplateidCodeMap=getAllPrintTemplate(ruleExecContext);
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
            if (!ctemplateidCodeMap.keySet().containsAll(ctemplateidOfNodeSet) ) {
            	detailInforResult.put(ERR2, nodecode);
            }
            
            //通过下面的操作，取得节点分配的并且是存在的打印模板
            ctemplateidOfNodeSet.retainAll(ctemplateidCodeMap.keySet());
            //检查打印模板的编码是否符合规范
            for (String ctemplateid : ctemplateidOfNodeSet) {
            	if(null ==ctemplateidCodeMap.get(ctemplateid)){//模板编码为空
            		detailInforResult.put(ERR3, nodecode);
            	}
            	if (!ctemplateidCodeMap.get(ctemplateid).startsWith(nodecode)) {//模板编码不符合规范
            		   detailInforResult.put(ERR4, nodecode);
                 }
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
        if(null !=detailInforResult.get(ERR3)&&detailInforResult.size()>0){
            result.addResultElement(ERR3, "功能节点编码为" + detailInforResult.get(ERR3)
                + "的节点的打印模板编码为空！\n");
        }
        if(null !=detailInforResult.get(ERR4)&&detailInforResult.size()>0){
            result.addResultElement(ERR4, "功能节点编码为" + detailInforResult.get(ERR4)
                    + "的节点的打印模板编码不符合规范！\n");
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
	 * 从数据库表 pub_print_template 中查询所有打印模板。将ctemplateid和模板编码vtemplatecode的对应关系存在Map容器中。
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
        sql.append(" select ctemplateid,vtemplatecode from pub_print_template where  isnull(dr,0)=0 and pk_corp='@@@@'");
        return sql.toString();
    }
}
