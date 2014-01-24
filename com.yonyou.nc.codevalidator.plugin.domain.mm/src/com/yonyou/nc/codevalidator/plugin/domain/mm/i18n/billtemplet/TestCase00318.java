package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.billtemplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractLangRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 参数的多语处理
 * 
 * @author qiaoyang
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "参数的多语处理", specialParamDefine = {
    CommonRuleParams.DOMAINPARAM
}, solution = "", coder = "qiaoyang", relatedIssueId = "318")
public class TestCase00318 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        // 领域编码
        String[] modulecode = ruleExecContext.getParameterArray(CommonRuleParams.DOMAINPARAM);
        String sql = null;
        if (modulecode == null || modulecode.length < 1) {
            sql = "select initcode, domainflag from pub_sysinittemp where isnull(dr,0)=0";
        }
        else {
            sql =
                    "select initcode, domainflag from pub_sysinittemp where isnull(dr,0)=0 and "
                            + SQLQueryExecuteUtils.buildSqlForIn("domainflag", modulecode);
        }

        DataSet ds = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
        Map<String, String> initcodetomodels = new HashMap<String, String>();
        if (null != ds && ds.getRowCount() > 0) {
            for (DataRow sr : ds.getRows()) {
                if (null != sr.getValue("initcode") && !"~".equals(sr.getValue("initcode"))) {
                    initcodetomodels.put("Dinitname" + sr.getValue("initcode"), (String) sr.getValue("domainflag"));
                }
            }
            String langsql =
                    "select id from "
                            + SQLQueryExecuteUtils.getCurrentMultiLangTableName()
                            + "  where "
                            + SQLQueryExecuteUtils
                                    .buildSqlForIn("id", initcodetomodels.keySet().toArray(new String[0]));
            DataSet langds = SQLQueryExecuteUtils.executeQuery(langsql, ruleExecContext.getRuntimeContext());
            List<String> allLangids = new ArrayList<String>();
            List<String> nolangids = new ArrayList<String>();
            for (DataRow lang : langds.getRows()) {
                allLangids.add((String) lang.getValue("id"));
            }
            for (String paramId : initcodetomodels.keySet()) {
                if (!allLangids.contains(paramId)) {
                    nolangids.add(paramId);
                }
            }
            if (nolangids.size() > 0) {
                MapList<String, String> errorinfos = new MapList<String, String>();
                for (String nolangid : nolangids) {
                    errorinfos.put(initcodetomodels.get(nolangid), "参数编码：" + nolangid.substring(9) + "未作参数名称多语！\n");
                }
                if (errorinfos.size() > 0) {
                    for (String key : errorinfos.keySet()) {
                        for (String errorinfo : errorinfos.get(key)) {
                            result.addResultElement(key, errorinfo);
                        }
                    }
                }
            }
        }
        else if (modulecode != null && modulecode.length > 0) {
            for (String code : modulecode) {
                result.addResultElement(code, "无系统参数\n");
            }
        }
        return result;
    }
}
