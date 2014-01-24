package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;

/**
 * 转单查询对话框的初始化应该跟单据自身的查询对话框初始化逻辑保持一致
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, description = "转单查询对话框的初始化应该跟单据自身的查询对话框初始化逻辑保持一致", memo = "", solution = "", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "255", coder = "gaojf")
public class TestCase00255 extends AbstractJavaQueryRuleDefinition {

    @Override
    protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
        return javaResourceQuery;
    }

    @Override
    protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
            throws RuleBaseException {

        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        List<JavaClassResource> javaclasses = new ArrayList<JavaClassResource>();
        // 找到所有查询包里的类
        for (JavaClassResource javaClassResource : resources) {
            if (javaClassResource.getJavaCodeClassName().indexOf(".query.") != -1) {
                javaclasses.add(javaClassResource);
            }
        }
        if (javaclasses.size() == 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "没有找到.query.中的类，或不存在.query.包。\n");
            return result;
        }
        List<JavaClassResource> queryForOthers = this.getQueryBillsForOther(ruleExecContext, javaclasses);
        if (queryForOthers.size() == 0 || queryForOthers.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "没有找到转单类，请检查选择的工程中是否包含了转单查询类（继承了DefaultBillReferQuery）\n");
            return result;
        }
        List<JavaClassResource> queryConditionInit = this.getQueryConditionInitClasses(ruleExecContext, javaclasses);
        if (queryConditionInit.size() == 0 || queryConditionInit.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "没有找到单据查询初始化类，请检查查询初始化名称中是否包含了queryConditionInit字符！\n");
            return result;
        }

        for (JavaClassResource javaClassResource : queryForOthers) {
            boolean b = false;
            try {
                CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));

                List<ImportDeclaration> impots = compilationUnit.getImports();
                for (ImportDeclaration impo : impots) {

                    String temp = impo.getName().toString();

                    for (JavaClassResource javaClass : queryConditionInit) {
                        String name = javaClass.getJavaCodeClassName();
                        if (name.equals(temp)) {
                            b = true;
                        }
                    }
                }
                if (!b) {
                    result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String
                            .format("【%s】中没有找到单据查询初始化类，" + "请检查转单初始化是否调用了单据初始化或者单据初始化类中是否包含queryConditionInit字符。",
                                    javaClassResource.getJavaCodeClassName()));
                }

            }
            catch (ParseException e) {
                Logger.error(e.getMessage(), e);
            }
            catch (IOException e) {
                Logger.error(e.getMessage(), e);
            }
        }

        return result;
    }

    /**
     * 查询转单的类（继承了DefaultBillReferQuery的类）
     * 
     * @param ruleExecContext
     * @param resources
     * @return
     * @throws RuleBaseException
     */
    private List<JavaClassResource> getQueryBillsForOther(IRuleExecuteContext ruleExecContext,
            List<JavaClassResource> resources) throws RuleBaseException {
        List<JavaClassResource> result = new ArrayList<JavaClassResource>();
        for (JavaClassResource javaClassResource : resources) {
            boolean isExtends =
                    ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(
                            ruleExecContext.getBusinessComponent().getProjectName(),
                            javaClassResource.getJavaCodeClassName(), "nc.ui.pubapp.billref.src.DefaultBillReferQuery");
            if (isExtends) {
                result.add(javaClassResource);
                break;
            }

        }
        return result;

    }

    /**
     * 单据查询初始化的类
     * 
     * @param ruleExecContext
     * @param resources
     * @return
     */
    private List<JavaClassResource> getQueryConditionInitClasses(IRuleExecuteContext ruleExecContext,
            List<JavaClassResource> resources) {
        List<JavaClassResource> queryCondition = new ArrayList<JavaClassResource>();
        for (JavaClassResource javaClassResource : resources) {

            String name = javaClassResource.getJavaCodeClassName();
            if (name.toLowerCase().endsWith("queryconditioninitializer")) {
                queryCondition.add(javaClassResource);
            }
        }
        return queryCondition;
    }
}
