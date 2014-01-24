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
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 元数据的单据模板上本元数据的字段名称不允许修改
 * (解释：本元数据的字段：从元数据中拖过来的字段，不包括带出来的字段)
 * 
 * @since 6.0
 * @version 2013-9-17 下午4:58:16
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "元数据的单据模板上本元数据的字段名称不允许修改 ", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "将单据模板中该字段的defaultshowname的值设为空  ", coder = "zhongcha", relatedIssueId = "136")
public class TestCase00136 extends AbstractScriptQueryRuleDefinition {

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
            MapList<String, String> errorMap = new MapList<String, String>();
            for (int i = 0; i < ds.getRowCount(); i++) {
                String templetName = (String) ds.getValue(i, "bill_templetcaption");
                String itemkey = (String) ds.getValue(i, "itemkey");
                if (MMValueCheck.isNotEmpty(ds.getValue(i, "defaultshowname"))) {
                    errorMap.put(templetName, itemkey);
                }
            }
            StringBuilder errorContxt = new StringBuilder();
            if (MMValueCheck.isNotEmpty(errorMap)) {
                for (String templetName : errorMap.keySet()) {
                    errorContxt.append("节点编码为[" + funnode + "]的单据模板[" + templetName + "]中的本元数据字段"
                            + errorMap.get(templetName) + "名称被修改。\n");
                }
            }
            if (MMValueCheck.isNotEmpty(errorContxt)) {
                result.addResultElement(funnode, errorContxt.toString());
            }
        }
        return result;
    }

    private String getSql(String funnode) {
        return "select pb.defaultshowname,pb.itemkey,pt.bill_templetcaption from pub_billtemplet_b pb,pub_billtemplet pt where pb.userdefflag = 'N'  and pb.pk_billtemplet=pt.pk_billtemplet  and pb.itemkey not like '%.%' and pt.pk_billtemplet in(select pb.templateid from pub_systemplate_base pb where pb.tempstyle='0' and pb.funnode='"
                + funnode + "')";
    }

}
