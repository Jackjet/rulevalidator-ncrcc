package com.yonyou.nc.codevalidator.plugin.domain.mm.code.impl;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * 检查是否存在接口实现类为抽象类的问题
 * 
 * @since 6.0
 * @version 2013-10-11 下午8:03:49
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "检查是否存在接口实现类为抽象类的问题  ", relatedIssueId = "70", coder = "zhongcha", solution = "请修改使得该实现类不是抽象类")
public class TestCase00070 extends AbstractJavaQueryRuleDefinition {

    @Override
    protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        return javaResourceQuery;
    }

    @Override
    protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        List<String> upmImplClassList = new ArrayList<String>();
        UpmResourceQuery upmResourceQuery = new UpmResourceQuery();
        upmResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
        // 获取组件中upm中实现类列表
        for (UpmResource upmResource : upmResources) {
            UpmModuleVO upmModuleVo = upmResource.getUpmModuleVo();
            List<UpmComponentVO> pubComponentVoList = upmModuleVo.getPubComponentVoList();
            for (UpmComponentVO upmComponentVO : pubComponentVoList) {
                upmImplClassList.add(upmComponentVO.getImplementationName());
            }
        }

        // upmImplClassList.add("nc.ui.bd.bom.bom0202.action.AbstractBomBodyLineChangeAction");// 加入一个抽象类进行测试

        List<String> errorList1 = new ArrayList<String>();
        List<String> errorList2 = new ArrayList<String>();
        for (String impl : upmImplClassList) {
            try {
                int modifier =
                        ClassLoaderUtilsFactory.getClassLoaderUtils()
                                .loadClass(ruleExecContext.getBusinessComponent().getProjectName(), impl)
                                .getModifiers();
                if (Modifier.isAbstract(modifier)) {
                    errorList1.add(impl);
                }
            }
            catch (RuleClassLoadException e) {
                errorList2.add(impl);
            }
        }
        StringBuilder errorContxt = new StringBuilder();
        if (MMValueCheck.isNotEmpty(errorList1)) {
            errorContxt.append("实现类名为" + errorList1 + "的类不能是抽象类。\n");
        }
        if (MMValueCheck.isNotEmpty(errorList2)) {
            errorContxt.append("实现类名为" + errorList2 + "无法加载到。\n");
        }
        if (MMValueCheck.isNotEmpty(errorContxt)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    errorContxt.toString());
        }

        return result;
    }
}
