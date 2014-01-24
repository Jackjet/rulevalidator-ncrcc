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
 * ����ת���ľ��ȴ���Ӧ��ʹ��һ�׾��ȴ����߼�
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, description = "ת�����ȴ���ʹ��һ�׾��ȴ����߼�,�����ж���", memo = "", solution = "ת�����ȴ���ʹ��һ�׾��ȴ����߼�,�����ж���;1.ͨ���ؼ���*TransferListViewProcessor��ƽ̨ITransferListViewProcessor�ӿ��ҵ�ת��������;2.�鿴ת�����������Ƿ�ʹ���˶��׾��ȴ�����", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "257", coder = "lijbe")
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
                    "û���ҵ�ת�������ࡾת���������ԣ�*TransferListViewProcessor��β��.\n");
            return result;
        }

        for (JavaClassResource javaClassResource : javaclasses) {
            try {
                CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
                TransferListViewProcessorVisitor visitor = new TransferListViewProcessorVisitor();
                visitor.visit(compilationUnit, null);
                if (!visitor.isTransferItf) {
                    result.addResultElement(javaClassResource.getResourceFileName(), String.format(
                            "ת�������ࣺ��%s��û��ʵ��ƽ̨�ӿڡ�%s��.\n ", javaClassResource.getJavaCodeClassName(),
                            "nc.ui.pubapp.billref.dest.TransferViewProcessor"));
                }
                if (visitor.scaleUtilList.size() > 1) {
                    result.addResultElement(javaClassResource.getResourceFileName(), String.format(
                            "ת�������ࣺ��%s���С�%s���׾��ȴ����ࡾ%s��,Ӧ�޸�Ϊʹ��һ�״����߼�.\n ", javaClassResource.getJavaCodeClassName(),
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
         * ��ž��ȴ�����
         */
        List<String> scaleUtilList = new ArrayList<String>();

        /**
         * �Ƿ�ʵ����ITransferListViewProcessor�ӿ�
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
            // �ж��Ƿ��о��ȴ�����
            if (MMValueCheck.isNotEmpty(importClazz)) {
                if (importClazz.indexOf(".scale.") > 0 && importClazz.matches(".*ScaleUtil.*")) {
                    this.scaleUtilList.add(importClazz);
                }
            }
            super.visit(n, arg);
        }

    }

}
