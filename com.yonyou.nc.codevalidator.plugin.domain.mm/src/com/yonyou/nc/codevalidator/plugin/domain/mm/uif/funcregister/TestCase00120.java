package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.funcregister;

import java.util.ArrayList;
import java.util.HashMap;
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
 * ���ܽڵ�ע���У����ܽڵ�ע���а����ļ��ֶε�ֵ�������Ҫ��[���ܱ���.html]
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "���ܽڵ�ע���У����ܽڵ�ע���а����ļ��ֶε�ֵ�������Ҫ�󣬼�[���ܱ���.html]", solution = "���ܽڵ�ע���У����ܽڵ�ע���а����ļ��ֶε�ֵ�������Ҫ��[���ܱ���.html]", coder = "lijbe", relatedIssueId = "120")
public class TestCase00120 extends AbstractXmlRuleDefinition {

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

        List<String> funcodes = new ArrayList<String>();
        for (XmlResource xmlResource : resources) {
            funcodes.add(xmlResource.getFuncNodeCode());
        }

        Map<String, String> funNameMap = new HashMap<String, String>();

        Map<String, String> funHelpMap =
                this.queryFunHelpNameByFuncode(funcodes.toArray(new String[] {}), ruleExecContext, funNameMap);
        if (null == funHelpMap || funHelpMap.size() < 1) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "��������Ĺ��ܱ����Ƿ���ȷ!");
        }

        List<String> funNames = new ArrayList<String>();
        for (String key : funHelpMap.keySet()) {
            if (!(key + ".html").equals(funHelpMap.get(key))) {
                funNames.add(funNameMap.get(key));
            }
        }
        if (funNames.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܽڵ�:%s �İ����ļ��������Ϲ淶.\n", funNames));
        }

        return result;
    }

    /**
     * ȡ�ù��ܽڵ��й��ܱ����Ӧ�Ĺ�������
     * 
     * @param funcodes
     * @param ruleExecContext
     * @param funNameMap
     * @return
     * @throws RuleBaseException
     */
    private Map<String, String> queryFunHelpNameByFuncode(String[] funcodes, IRuleExecuteContext ruleExecContext,
            Map<String, String> funNameMap) throws RuleBaseException {

        Map<String, String> funHelpMap = new HashMap<String, String>();

        String sql = "select funcode,fun_name,help_name from sm_funcregister where funcode in(";

        String whereValue = "";

        for (int i = 0; i < funcodes.length; i++) {

            if (i == funcodes.length - 1) {
                whereValue += "'" + funcodes[i] + "'";
            }
            else {
                whereValue += "'" + funcodes[i] + "',";
            }
        }
        whereValue += ") and dr = 0 ";
        sql += whereValue;

        DataSet dataSet = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
        if (dataSet.isEmpty()) {
            return funHelpMap;
        }

        List<DataRow> dataRows = dataSet.getRows();

        for (DataRow dataRow : dataRows) {
            funHelpMap.put((String) dataRow.getValue("funcode"), (String) dataRow.getValue("help_name"));
            funNameMap.put((String) dataRow.getValue("funcode"), (String) dataRow.getValue("fun_name"));
        }

        return funHelpMap;

    }

}
