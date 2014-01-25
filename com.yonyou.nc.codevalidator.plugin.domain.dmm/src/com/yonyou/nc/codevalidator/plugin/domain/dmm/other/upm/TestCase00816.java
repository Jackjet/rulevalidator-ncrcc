package com.yonyou.nc.codevalidator.plugin.domain.dmm.other.upm;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

@RuleDefinition(catalog = CatalogEnum.OTHERCONFIGFILE, coder = "songkj", description = "UPM������Լ�����<component priority=\"0\" singleton=\"true\" remote=\"true\" tx=\"CMT\" supportAlias=\"true\"> ", relatedIssueId = "816", subCatalog = SubCatalogEnum.OCF_UPM, repairLevel = RepairLevel.SUGGESTREPAIR, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00816 extends AbstractUpmQueryRuleDefinition {

    @Override
    protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        StringBuilder noteString = null;
        for (UpmResource upmResource : resources) {
            noteString = new StringBuilder();
            UpmModuleVO upmModuleVo = upmResource.getUpmModuleVo();
            List<UpmComponentVO> pubComponentVoList = upmModuleVo.getPubComponentVoList();
            for (UpmComponentVO upmComponentVO : pubComponentVoList) {
                int priority = upmComponentVO.getPriority();
                boolean singleton = upmComponentVO.isSingleton();
                boolean remote = upmComponentVO.isRemote();
                String tx = upmComponentVO.getTx();
                boolean supportAlias = upmComponentVO.isSupportAlias();
                if (priority != 0) {
                    noteString.append("priority���Բ�Ϊ'0'");
                }
                if (!singleton) {
                    noteString.append("singleton���Բ�Ϊ'true'");
                }
                if (!remote) {
                    noteString.append("remote���Բ�Ϊ'true'");
                }
                if (!"CMT".equals(tx)) {
                    noteString.append("tx���Բ�Ϊ'CMT'");
                }
                if (!supportAlias) {
                    noteString.append("supportAlias���Բ�Ϊ'true'");
                }
            }
            if (noteString.length() > 0) {
                result.addResultElement(upmResource.getResourcePath(), noteString.toString());
            }
        }
        return result;
    }
}
