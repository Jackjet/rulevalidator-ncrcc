package com.yonyou.nc.codevalidator.plugin.domain.mm.script.dbcreate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * 预置交易类型的transtype_class字段必须输入交易类型的VO类是否存在
 * 
 * @since 6.0
 * @version 2013-9-22 下午7:48:48
 * @author zhongcha
 */
@RuleDefinition(catalog = CatalogEnum.CREATESCRIPT, subCatalog = SubCatalogEnum.CS_UNIFORM, description = "预置交易类型的transtype_class字段必须输入交易类型的VO类是否存在 ", specialParamDefine = {
    "领域编码"
}, memo = "请在参数中输入领域编码(如MMPAC)，多个参数请用,分开", solution = "请添加该预置交易类型的transtype_class字段必须输入交易类型的VO类 ", coder = "zhongcha", relatedIssueId = "117")
public class TestCase00117 extends AbstractScriptQueryRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] domainCodes = ruleExecContext.getParameterArray("领域编码");
        if (MMValueCheck.isEmpty(domainCodes) || domainCodes[0].trim().equals("")) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "参数为空，请输入参数！\n");
            return result;
        }
        for (String domainCode : domainCodes) {
            DataSet ds = this.executeQuery(ruleExecContext, this.getSql(domainCode));
            if (MMValueCheck.isEmpty(ds)) {
                // 至少存在一个默认的交易类型
                result.addResultElement(domainCode, "领域编码为[" + domainCode + "]的不存在默认交易类型");
                continue;
            }
            List<String> errorList1 = new ArrayList<String>();
            List<String> errorList2 = new ArrayList<String>();
            // 该MapList的key为单据交易类型的VO类（即transtype_class），value为其对应的交易类型编码（即pk_billtypecode）的值
            MapList<String, String> class2codes = new MapList<String, String>();
            for (int i = 0; i < ds.getRowCount(); i++) {
                String transtypeclass = (String) ds.getValue(i, "transtype_class");
                String billtypecode = (String) ds.getValue(i, "pk_billtypecode");
                class2codes.put(transtypeclass, billtypecode);
            }
            for (Entry<String, List<String>> class2code : class2codes.entrySet()) {
                // 记录单据交易类型的VO类为空的交易类型编码
                if (MMValueCheck.isEmpty(class2code.getKey())) {
                    errorList1 = class2code.getValue();
                }
                else {
                    // 记录代码中无法加载的VO类（如果catch到异常说明不存在该VO类）
                    try {
                        ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(
                                ruleExecContext.getBusinessComponent().getProjectName(), class2code.getKey());
                    }
                    catch (RuleClassLoadException e) {
                        errorList2.add(class2code.getKey());
                    }
                }
            }
            StringBuilder errorContxt = new StringBuilder();
            if (MMValueCheck.isNotEmpty(errorList1)) {
                errorContxt.append("领域编码为[" + domainCode + "]的交易类型编码为" + errorList1 + "与其对应的交易类型的VO类的值为空。\n");

            }
            if (MMValueCheck.isNotEmpty(errorList2)) {
                for (String voclass : errorList2) {
                    errorContxt.append("领域编码为[" + domainCode + "]的交易类型编码为" + class2codes.get(voclass) + "与其对应的交易类型的VO类"
                            + voclass + "在代码中不存在。\n");
                }
            }
            if (MMValueCheck.isNotEmpty(errorContxt)) {
                result.addResultElement(domainCode, errorContxt.toString());
            }
        }
        return result;
    }

    private String getSql(String domainCode) {
        return "select distinct t.transtype_class,t.pk_billtypecode from bd_billtype t where  t.pk_group='global00000000000000'  and isnull(t.dr,0)=0  and t.parentbilltype in (select b.pk_billtypeid from bd_billtype b where b.istransaction='N'and b.canextendtransaction='Y' and isnull(b.dr,0)=0  and b.systemcode='"
                + domainCode + "' )";
    }

}
