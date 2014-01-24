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
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "检查指定的几个支持使用权限的字段：物料、物料分类、工作中心和部门。检查此字段应该是元数据条件。", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, coder = "wangfra", relatedIssueId = "323")
public class TestCase00323 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition{

	
	private static final String ERR1 = "节点未分配查询模板";  //错误信息分类
	private static final String ERR2 = "节点分配的查询模板不存在"; 
	private static final String ERR3 = "物料等查询条件是非元数据条件"; 
    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] nodecodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        MapSet<String, String> funnodeTempletMapSet=getQueryTemplateIDOfFunnodes(ruleExecContext);
        MapList<String,Map<String,String>> ctemplateidCodeMap=getSpeciQueryCon(ruleExecContext);
    	
        if (MMValueCheck.isEmpty(nodecodes)) {
        	  Logger.warn("当前规则功能节点参数为空，将检查所有功能节点！");
        	  nodecodes= funnodeTempletMapSet.keySet().toArray(new String[funnodeTempletMapSet.keySet().size()]);
        }
    
        
        Set<String> ctemplateidOfNodeSet = null;
    	MapSet<String, String> detailInforResult=new MapSet<String, String>();
        // 依次检查每个节点
        for (String nodecode : nodecodes) {
            // 获得该节点对应的所有查询模板
        	ctemplateidOfNodeSet = funnodeTempletMapSet.get(nodecode);
        	
        	//如果此节点未分配查询模板，则跳出本次循环，检查下一个节点
            if (null ==ctemplateidOfNodeSet || ctemplateidOfNodeSet.isEmpty()) {
            	detailInforResult.put(ERR1, nodecode);
                continue;
            }
            
            //如果该节点分配的查询模板id在 pub_query_condition表中未查到，则说明该节点分配了不存在的查询模板
            if (!ctemplateidCodeMap.keySet().containsAll(ctemplateidOfNodeSet) ) {
            	detailInforResult.put(ERR2, nodecode);
            }
            //通过下面的操作，取得节点分配的并且是存在的查询模板
            ctemplateidOfNodeSet.retainAll(ctemplateidCodeMap.keySet());
            //检查查询模板的物料等字段是否是元数据条件
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
     * 处理错误提示信息
     * @param result
     * @param detailInforResult
     * @return
     */
	private ResourceRuleExecuteResult processErrorInfor(ResourceRuleExecuteResult result,
			MapSet<String, String> detailInforResult) {
		if(null !=detailInforResult.get(ERR1)&&detailInforResult.size()>0){
            result.addResultElement(ERR1, "功能节点编码为" + detailInforResult.get(ERR1)
                + "的节点尚未分配查询模板！请检查是否要分配！\n");
        }
        if(null !=detailInforResult.get(ERR2)&&detailInforResult.size()>0){
            result.addResultElement(ERR2, "功能节点编码为" + detailInforResult.get(ERR2)
                    + "的节点所分配的查询模板不存在！\n");
         }
        if(null !=detailInforResult.get(ERR3)&&detailInforResult.size()>0){
            result.addResultElement(ERR3, "功能节点编码为" + detailInforResult.get(ERR3)
                + "的节点,其查询模板中的物料、部门或工作中心应该设为元数据条件！\n");
        }
     
		return result;
	}

    /**
	 * 从数据库表 pub_systemplate_base 中查询每个节点所分配的查询模板。这种对应关系存在MapSet容器中。
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
	 * 从数据库表 pub_query_condition 中查询物料等条件。将pk_templet和consult_code、if_notmdcondition的对应关系存在MapList容器中。
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
     * 构造从功能节点默认模板表中查询到默认查询模板注册数据
     * @return
     */
    private String getAllQueryTemplateIDSql() {
    	SQLBuilder result = new SQLBuilder();
        result.append("select s.funnode,s.templateid from pub_systemplate_base s where isnull(s.dr,0) = 0 and s.tempstyle = 1 ");
        return result.toString();
    }
	
	
    private String getSpecifiedQueryConditonSql() {
    	SQLBuilder result = new SQLBuilder();
        result.append("select pq.pk_templet,pq.consult_code,pq.if_notmdcondition from  pub_query_condition pq where isnull(pq.dr,0) = 0 and (pq.consult_code like '物料%' or  pq.consult_code like '部门%'  or  pq.consult_code like 'UAP-工作中心%')");
        return result.toString();
    }
}
