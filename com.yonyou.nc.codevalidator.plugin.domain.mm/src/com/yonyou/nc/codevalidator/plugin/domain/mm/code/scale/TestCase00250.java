package com.yonyou.nc.codevalidator.plugin.domain.mm.code.scale;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.code.util.MmScaleUtilVisitor;
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
 * ��Ƭ���б���ӡʹ��һ�׾����㷨������д����.����ʹ�ñ����򹫹��ľ��ȴ������
 * ����������һ��ȱ�ݣ����ǲ�֪���������п�Ƭ���б���ߴ�ӡ������ֻ��ͬ���飬��ʾ
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, description = "��Ƭ���б���ӡʹ��һ�׾����㷨������д����.����ʹ�ñ����򹫹��ľ��ȴ������", memo = "", solution = "��Ƭ���б���ӡʹ��һ�׾����㷨������д����.����ʹ�ñ����򹫹��ľ��ȴ������", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "250", coder = "gaojf")
public class TestCase00250 extends AbstractJavaQueryRuleDefinition {

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
            if (javaClassResource.getJavaCodeClassName().indexOf(".scale.") != -1
                    && javaClassResource.getJavaCodeClassName().matches(".*ScaleUtil.*")) {
                javaclasses.add(javaClassResource);
            }
        }

        if (javaclasses.isEmpty() || javaclasses.size() == 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(), "�����ļ�������.\n");
            return result;
        }

        for (JavaClassResource javaClassResource : javaclasses) {
            try {
                CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
                MmScaleUtilVisitor vistor = new MmScaleUtilVisitor();
                vistor.visit(compilationUnit, "");
                if (!vistor.isAllUserOneMethod()) {
                    result.addResultElement(javaClassResource.getResourceFileName(),
                            "��Ƭ���б���ӡʹ�ò���һ�׾����㷨; " + vistor.getNoteBuilder() + "\n");
                }
                if (!vistor.isUserPubScaleFrame()) {
                    result.addResultElement(javaClassResource.getResourceFileName(),
                            "���Ǳ����򹫹��ľ��ȴ�����ơ�nc.vo.pubapp.scale.BillVOScaleProcessor��!\n");
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

}
