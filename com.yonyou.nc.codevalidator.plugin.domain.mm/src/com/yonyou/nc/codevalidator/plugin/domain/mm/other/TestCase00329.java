package com.yonyou.nc.codevalidator.plugin.domain.mm.other;

import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

/**
 * @since 1.0
 * @version 1.0.0.0
 * @author wangfra
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.OTHERCONFIGFILE, subCatalog = SubCatalogEnum.OCF_UPM, description = "ͬһģ����,upm��ע���EJB���Ʊ���һ�¡�", specialParamDefine = {
    "��ģ����upm�ļ���ע���ͳһ��EJB����"
}, coder = "wangfra", relatedIssueId = "329")
public class TestCase00329 extends AbstractUpmQueryRuleDefinition {

    private static String PARAM_NAME = "��ģ����upm�ļ���ע���ͳһ��EJB����";

    @Override
    protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
            throws ResourceParserException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String ejbName = ruleExecContext.getParameter(TestCase00329.PARAM_NAME);
        if (MMValueCheck.isEmpty(ejbName)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "�������ģ���µ�upm�ļ���ע���EJB����!");
            return result;
        }
        if (null == resources) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getModule(), "��ģ����UPM�ļ������ڣ�");
            return result;
        }

        for (UpmResource upmResource : resources) {
            String name = upmResource.getUpmModuleVo().getModuleName();
            if (null == name) {
                result.addResultElement(upmResource.getResourceFileName(), "��UPM�ļ��е�EJB�����ǿյģ�");
            }
            else if (name.equals(ejbName)) {
                result.addResultElement(upmResource.getResourceFileName(), "��UPM�ļ��е�EJB������ͳһ�����Ʋ�һ�£�");
            }
        }
        return result;
    }

}
