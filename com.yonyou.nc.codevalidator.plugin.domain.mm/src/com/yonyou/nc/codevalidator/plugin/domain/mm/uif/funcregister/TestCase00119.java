package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.funcregister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 参数节点编码的前两位应该是领域编码。并且8位。
 * 
 * @author gaojf
 */
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "参数节点编码的前两位应该是领域编码。并且8位。", solution = "判断参数节点编码的前两位应该是领域编码。并且8位。", coder = "gaojf", relatedIssueId = "119")
public class TestCase00119 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResQry;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {

        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        ArrayList<String> funcodeList = new ArrayList<String>();
        for (XmlResource xmlResource : resources) {

            String funcode = xmlResource.getFuncNodeCode();
            funcodeList.add(funcode);
        }
        Map<String, String> funcodeDomainMap = this.getFuncodeDomainCode(funcodeList, ruleExecContext);
        if (funcodeDomainMap == null) {

            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("请选择要检查的节点编号。\n"));
            return result;

        }

        for (String funcode : funcodeDomainMap.keySet()) {

            if (funcode.length() != 8) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点:%s 的节点号应为8位。\n", funcode));
                continue;
            }

            String domainCode = funcodeDomainMap.get(funcode);

            if (domainCode == null) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点:%s 的领域编号为空\n", funcode));
                continue;
            }
            if (!domainCode.equals(funcode.subSequence(0, 2))) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点:%s 的领域编号%s,节点号前两位应与领域编号相同。\n", funcode, domainCode));
                continue;
            }
        }
        return result;
    }

    private Map<String, String> getFuncodeDomainCode(ArrayList<String> funcodeList, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {

        Map<String, String> funcodeDomainMap = new HashMap<String, String>();
        StringBuilder sql = new StringBuilder();
        if (funcodeList == null || funcodeList.isEmpty()) {
            return null;
        }
        for (String key : funcodeList) {
            funcodeDomainMap.put(key, null);
        }
        sql.append("select t.parentcode,sf.funcode from dap_dapsystem t,sm_funcregister sf "
                + "where t.moduleid = sf.own_module and sf.funcode in ( ");

        if (funcodeList != null && !funcodeList.isEmpty()) {

            Iterator<String> iter = funcodeList.iterator();

            while (iter.hasNext()) {
                sql.append("'");
                sql.append(iter.next());
                sql.append("'");
                if (iter.hasNext()) {
                    sql.append(",");
                }
                else {
                    sql.append(")");
                }
            }
        }
        DataSet dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());
        if (dataSet.isEmpty()) {
            return funcodeDomainMap;
        }

        List<DataRow> dataRows = dataSet.getRows();
        for (DataRow dataRow : dataRows) {
            funcodeDomainMap.put((String) dataRow.getValue("funcode"), (String) dataRow.getValue("parentcode"));
        }

        return funcodeDomainMap;
    }
}
