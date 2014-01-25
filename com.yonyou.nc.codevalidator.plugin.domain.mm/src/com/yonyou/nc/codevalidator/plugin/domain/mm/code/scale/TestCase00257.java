package com.yonyou.nc.codevalidator.plugin.domain.mm.code.scale;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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

/**
 * 对于转单的精度处理应该使用一套精度处理逻辑
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, description = "转单精度处理使用一套精度处理逻辑,不能有多套", memo = "", solution = "转单精度处理使用一套精度处理逻辑,不能有多套;1.通过关键字*TransferListViewProcessor和平台ITransferListViewProcessor接口找到转单处理类;2.查看转单处理类中是否使用了多套精度处理类", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "257", coder = "lijbe")
public class TestCase00257 extends AbstractJavaQueryRuleDefinition {

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

        for (JavaClassResource javaClassResource : resources) {
            if (javaClassResource.getJavaCodeClassName().endsWith("TransferListViewProcessor")) {
                javaclasses.add(javaClassResource);
            }
        }

        if (javaclasses.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                    "没有找到转单处理类【转单处理类以：*TransferListViewProcessor结尾】.\n");
            return result;
        }

        for (JavaClassResource javaClassResource : javaclasses) {
            try {
                CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
                TransferListViewProcessorVisitor visitor = new TransferListViewProcessorVisitor();
                visitor.visit(compilationUnit, null);
                if (!visitor.isTransferItf) {
                    result.addResultElement(javaClassResource.getResourceFileName(), String.format(
                            "转单处理类：【%s】没有实现平台接口【%s】.\n ", javaClassResource.getJavaCodeClassName(),
                            "nc.ui.pubapp.billref.dest.TransferViewProcessor"));
                }
                if (visitor.scaleUtilList.size() > 1) {
                    result.addResultElement(javaClassResource.getResourceFileName(), String.format(
                            "转单处理类：【%s】有【%s】套精度处理类【%s】,应修改为使用一套处理逻辑.\n ", javaClassResource.getJavaCodeClassName(),
                            visitor.scaleUtilList.size(), visitor.scaleUtilList.toString()));
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

    private class TransferListViewProcessorVisitor extends VoidVisitorAdapter<Void> {

        /**
         * 存放精度处理类
         */
        List<String> scaleUtilList = new ArrayList<String>();

        /**
         * 是否实现了ITransferListViewProcessor接口
         */
        boolean isTransferItf = false;

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            List<ClassOrInterfaceType> itfList = n.getImplements();
            if (MMValueCheck.isEmpty(itfList)) {
                return;

            }
            if (itfList.toString().contains("ITransferListViewProcessor")) {
                this.isTransferItf = true;
            }
            super.visit(n, arg);
        }

        @Override
        public void visit(ImportDeclaration n, Void arg) {
            if (!this.isTransferItf) {
                return;
            }
            String importClazz = n.getName().toString();
            // 判断是否有精度处理类
            if (MMValueCheck.isNotEmpty(importClazz)) {
                if (importClazz.indexOf(".scale.") > 0 && importClazz.matches(".*ScaleUtil.*")) {
                    this.scaleUtilList.add(importClazz);
                }
            }
            super.visit(n, arg);
        }

    }

}
