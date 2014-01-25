package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTNameList;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

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
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * 除EJB的后台实现类外，其他的后台类的方法中不再声明异常，而是直接wrapp住异常
 * 
 * @since 6.0
 * @version 2013-10-30 下午4:51:42
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "除EJB的后台实现类外，其他的后台类的方法中不再声明异常，而是直接wrapp住异常  ", relatedIssueId = "260", coder = "zhongcha", solution = "修改使方法中不再声明异常，而是直接wrapp住异常。")
public class TestCase00260 extends AbstractJavaQueryRuleDefinition {

    @Override
    protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        javaResourceQuery.setResPrivilege(JavaResPrivilege.PRIVATE);
        return javaResourceQuery;
    }

    @Override
    protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        Map<String, List<String>> errorClz2Method = new HashMap<String, List<String>>();

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

        try {
            for (JavaClassResource javaClassResource : resources) {
                // 取得java文件的名（去掉后缀.java）
                String fileName = javaClassResource.getJavaCodeClassName();
                // 若该java文件的名后缀为Impl，则执行后续校验
                if (MMValueCheck.isNotEmpty(upmImplClassList) && upmImplClassList.contains(fileName)) {
                    continue;
                }
                ScanRule scanRule = new ScanRule();
                // 扫描该java文件判定是否存在判断单据状态的规则
                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                if (!scanRule.getErrorMethod().isEmpty()) {
                    errorClz2Method.put(fileName, scanRule.getErrorMethod());
                }
            }
            StringBuilder errorContxt = new StringBuilder();
            if (MMValueCheck.isNotEmpty(errorClz2Method)) {
                for (String clz : errorClz2Method.keySet()) {
                    errorContxt.append("类名为[ " + clz + " ]的方法" + errorClz2Method.get(clz)
                            + "不能声明异常，而是应该直接wrapp住异常  。\n");
                }
            }
            if (MMValueCheck.isNotEmpty(errorContxt)) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        errorContxt.toString());
            }
        }
        catch (FileNotFoundException e) {
            Logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 扫描java文件的规则
     * 
     * @since 6.0
     * @version 2013-10-30 下午2:02:23
     * @author zhongcha
     */
    private static class ScanRule extends AbstractJavaRule {
        private List<String> errorMethod = new ArrayList<String>();

        public List<String> getErrorMethod() {
            return this.errorMethod;
        }

        @Override
        public Object visit(ASTNameList node, Object data) {
            if (MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTMethodDeclaration.class))) {
                ASTMethodDeclarator methodDeclarator =
                        node.getFirstParentOfType(ASTMethodDeclaration.class).getFirstChildOfType(
                                ASTMethodDeclarator.class);
                if (MMValueCheck.isNotEmpty(methodDeclarator) && MMValueCheck.isNotEmpty(methodDeclarator.getImage())) {
                    // 记录有异常的方法
                    this.errorMethod.add(methodDeclarator.getImage().trim());
                }
            }

            return super.visit(node, data);
        }
    }

}
