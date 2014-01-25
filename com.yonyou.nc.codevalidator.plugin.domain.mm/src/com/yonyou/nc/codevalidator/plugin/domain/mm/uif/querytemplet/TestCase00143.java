package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.querytemplet;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 查询模板上的主组织及单据日期必输并设置默认值
 * 
 * @since 6.0
 * @version 2013-9-17 下午4:43:42
 * @author zhongcha
 */
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "查询模板上的主组织及单据日期必输并设置默认值#mainorg#", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "将相应节点的查询模板上的主组织及单据日期设置为必输并设置默认值  ", coder = "zhongcha", relatedIssueId = "143")
public class TestCase00143 extends AbstractScriptQueryRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funnodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        if (MMValueCheck.isEmpty(funnodes) || funnodes[0].trim().equals("")) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "参数为空，请输入参数！\n");
            return result;
        }
        for (String funnode : funnodes) {
            DataSet ds = this.executeQuery(ruleExecContext, this.getSql(funnode));
            MapList<String, String> errorMap1 = new MapList<String, String>();
            MapList<String, String> errorMap2 = new MapList<String, String>();
            for (int i = 0; i < ds.getRowCount(); i++) {
                String fieldcode = (String) ds.getValue(i, "field_code");
                String modelname = (String) ds.getValue(i, "model_name");
                if (MMValueCheck.isEmpty(ds.getValue(i, "value"))) {
                    errorMap1.put(modelname, fieldcode);
                }
                if (ds.getValue(i, "if_must").toString().equals("N")) {
                    errorMap2.put(modelname, fieldcode);
                }
            }
            StringBuilder errorMessage = new StringBuilder();
            if (MMValueCheck.isNotEmpty(errorMap1)) {
                for (String templetName : errorMap1.keySet()) {
                    errorMessage.append("节点号为" + funnode + "的查询模板[" + templetName + "]中的查询条件字段为"
                            + errorMap1.get(templetName) + "未设置默认值。\n");
                }
            }
            if (MMValueCheck.isNotEmpty(errorMap2)) {
                for (String templetName : errorMap2.keySet()) {
                    errorMessage.append("节点号为" + funnode + "的查询模板[" + templetName + "]中的查询条件字段为"
                            + errorMap2.get(templetName) + "未设置为必输条件。\n");
                }
            }
            if (MMValueCheck.isNotEmpty(errorMessage)) {
                result.addResultElement(funnode, errorMessage.toString());
            }
        }
        return result;
    }

    private String getSql(String funnode) {
        return "select pc.value,pc.if_must,pc.field_code,pt.model_name from pub_query_condition pc,pub_query_templet pt where  pc.field_code in ('pk_org','dbilldate')  and pc.pk_templet=pt.id and  pt.id in(select pb.templateid from pub_systemplate_base pb where pb.tempstyle='1' and pb.funnode='"
                + funnode + "')";
    }
}
