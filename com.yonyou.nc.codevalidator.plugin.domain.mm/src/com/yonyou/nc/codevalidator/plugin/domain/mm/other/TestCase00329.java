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
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.OTHERCONFIGFILE, subCatalog = SubCatalogEnum.OCF_UPM, description = "同一模块中,upm中注册的EJB名称必须一致。", specialParamDefine = {
    "本模块下upm文件中注册的统一的EJB名称"
}, coder = "wangfra", relatedIssueId = "329")
public class TestCase00329 extends AbstractUpmQueryRuleDefinition {

    private static String PARAM_NAME = "本模块下upm文件中注册的统一的EJB名称";

    @Override
    protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
            throws ResourceParserException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String ejbName = ruleExecContext.getParameter(TestCase00329.PARAM_NAME);
        if (MMValueCheck.isEmpty(ejbName)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请输入该模块下的upm文件中注册的EJB名称!");
            return result;
        }
        if (null == resources) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getModule(), "该模块下UPM文件不存在！");
            return result;
        }

        for (UpmResource upmResource : resources) {
            String name = upmResource.getUpmModuleVo().getModuleName();
            if (null == name) {
                result.addResultElement(upmResource.getResourceFileName(), "该UPM文件中的EJB名称是空的！");
            }
            else if (name.equals(ejbName)) {
                result.addResultElement(upmResource.getResourceFileName(), "该UPM文件中的EJB名称与统一的名称不一致！");
            }
        }
        return result;
    }

}
