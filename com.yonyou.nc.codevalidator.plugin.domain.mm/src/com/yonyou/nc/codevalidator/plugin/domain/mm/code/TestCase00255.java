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
 * ת����ѯ�Ի���ĳ�ʼ��Ӧ�ø���������Ĳ�ѯ�Ի����ʼ���߼�����һ��
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, description = "ת����ѯ�Ի���ĳ�ʼ��Ӧ�ø���������Ĳ�ѯ�Ի����ʼ���߼�����һ��", memo = "", solution = "", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "255", coder = "gaojf")
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
        // �ҵ����в�ѯ�������
        for (JavaClassResource javaClassResource : resources) {
            if (javaClassResource.getJavaCodeClassName().indexOf(".query.") != -1) {
                javaclasses.add(javaClassResource);
            }
        }
        if (javaclasses.size() == 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "û���ҵ�.query.�е��࣬�򲻴���.query.����\n");
            return result;
        }
        List<JavaClassResource> queryForOthers = this.getQueryBillsForOther(ruleExecContext, javaclasses);
        if (queryForOthers.size() == 0 || queryForOthers.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "û���ҵ�ת���࣬����ѡ��Ĺ������Ƿ������ת����ѯ�ࣨ�̳���DefaultBillReferQuery��\n");
            return result;
        }
        List<JavaClassResource> queryConditionInit = this.getQueryConditionInitClasses(ruleExecContext, javaclasses);
        if (queryConditionInit.size() == 0 || queryConditionInit.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "û���ҵ����ݲ�ѯ��ʼ���࣬�����ѯ��ʼ���������Ƿ������queryConditionInit�ַ���\n");
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
                            .format("��%s����û���ҵ����ݲ�ѯ��ʼ���࣬" + "����ת����ʼ���Ƿ�����˵��ݳ�ʼ�����ߵ��ݳ�ʼ�������Ƿ����queryConditionInit�ַ���",
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
     * ��ѯת�����ࣨ�̳���DefaultBillReferQuery���ࣩ
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
     * ���ݲ�ѯ��ʼ������
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
