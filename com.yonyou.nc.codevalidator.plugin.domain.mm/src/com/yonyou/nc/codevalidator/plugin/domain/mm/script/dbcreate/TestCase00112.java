package com.yonyou.nc.codevalidator.plugin.domain.mm.script.dbcreate;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 注册单据类型，单据类型编码要符合本领域的编码规则
 * 
 * @since 6.0
 * @version 2013-9-22 下午7:15:47
 * @author zhongcha
 */
@RuleDefinition(catalog = CatalogEnum.CREATESCRIPT, subCatalog = SubCatalogEnum.CS_UNIFORM, description = "注册单据类型，单据类型编码要符合本领域的编码规则 ", specialParamDefine = {
    "领域编码&规范前缀"
}, memo = "请在参数中输入领域编码和规范前缀,格式如MMPAC&55(其中MMPAC为领域编码，55为领域MMPAC的规范前缀)，多个参数请用,分开", solution = "修改该单据类型编码使其符合其领域的编码规则  ", coder = "zhongcha", relatedIssueId = "112")
public class TestCase00112 extends AbstractScriptQueryRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] parameters = ruleExecContext.getParameterArray("领域编码&规范前缀");
        if (MMValueCheck.isEmpty(parameters) || parameters[0].trim().equals("")) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "参数为空，请输入参数！\n");
            return result;
        }
        int errorNo = 0;
        for (String parameter : parameters) {
            String[] params = this.getSplitParameter(parameter);
            errorNo++;
            if (MMValueCheck.isEmpty(params)) {
                result.addResultElement("第" + errorNo + "个参数", "参数输入有误，请修改参数。");
            }
            else {
                DataSet ds = this.executeQuery(ruleExecContext, this.getSql(params[0]));
                List<String> errorList = new ArrayList<String>();
                for (int i = 0; i < ds.getRowCount(); i++) {
                    String billtypecode = (String) ds.getValue(i, "pk_billtypecode");
                    if (billtypecode.length() <= params[1].length()) {
                        errorList.add(billtypecode);
                    }
                    else {
                        String prefix = billtypecode.substring(0, params[1].length());
                        if (!prefix.equals(params[1])) {
                            errorList.add(billtypecode);
                        }
                    }
                }
                if (MMValueCheck.isNotEmpty(errorList)) {
                    result.addResultElement(params[0], "领域编码为[" + params[0] + "]的单据类型编码为" + errorList
                            + "的编码不符合该领域的编码规则。\n");
                }
            }
        }
        return result;
    }

    private String getSql(String domainCode) {
        return "select b.pk_billtypecode from bd_billtype b where b.istransaction='N' and isnull(b.dr,0)=0 and b.systemcode='"
                + domainCode + "'";
    }

    /**
     * 将参数用&分成两个值的String数组
     * 
     * @param parameter
     * @return
     */
    public String[] getSplitParameter(String parameter) {
        if (MMValueCheck.isEmpty(parameter) || !parameter.contains("&") || parameter.indexOf("&") <= 0) {
            return null;
        }
        return parameter.split("&");
    }
}
