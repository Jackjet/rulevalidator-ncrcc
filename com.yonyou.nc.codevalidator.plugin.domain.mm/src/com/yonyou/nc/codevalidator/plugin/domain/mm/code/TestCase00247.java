package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
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
 * �б���洦���ദ���˾����߼���
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.JAVACODE, description = "����б���洦���ദ���˾����߼���", relatedIssueId = "247", subCatalog = SubCatalogEnum.JC_CODECRITERION, coder = "gaojf")
public class TestCase00247 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResQry;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
            return result;
        }

        List<Element> elements = new ArrayList<Element>();

        for (XmlResource xmlResource : resources) {

            List<Element> eles = xmlResource.getElementsByTagName("bean");
            for (Element e : eles) {
                String className = e.getAttribute("class");
                className = className.trim();
                if (className == "" || className == null) {
                    continue;
                }
                boolean isExtends =
                        ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(
                                ruleExecContext.getBusinessComponent().getProjectName(), className,
                                "nc.ui.pubapp.uif2app.view.ShowUpableBillListView");

                if (isExtends) {
                    elements.add(e);
                }
            }
            if (elements == null || elements.isEmpty()) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("�ڵ㣺%s���б��಻���ڻ���û�м̳�ShowUpableBillListView. \n", xmlResource.getFuncNodeCode()));
                continue;
            }
            List<String> classnames = new ArrayList<String>();
            for (Element ele : elements) {
                classnames.add(ele.getAttribute("class"));
            }

            JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
            javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());

            javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
            javaResourceQuery.setClassNameFilterList(classnames);
            List<JavaClassResource> javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);
            for (JavaClassResource javaClassResource : javaResourceList) {
                try {
                    CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));

                    List<ImportDeclaration> imports = compilationUnit.getImports();
                    boolean hasScale = false;
                    for (ImportDeclaration impo : imports) {

                        String impot = impo.getName().toString();
                        if (impot.indexOf(".scale.") != -1) {
                            hasScale = true;
                            break;
                        }

                    }
                    if (!hasScale) {
                        result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String
                                .format("�ڵ㡾%s�����б��ࡾ%s���У��б���û�н��о��ȴ���. \n", xmlResource.getFuncNodeCode(),
                                        javaClassResource.getResourceFileName()));
                    }
                }
                catch (ParseException e) {
                    Logger.error(e.getMessage(), e);
                }
                catch (IOException e) {
                    Logger.error(e.getMessage(), e);
                }
            }
        }
        return result;
    }

}
