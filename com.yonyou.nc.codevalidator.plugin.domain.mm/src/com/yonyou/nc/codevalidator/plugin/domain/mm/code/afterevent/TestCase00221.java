package com.yonyou.nc.codevalidator.plugin.domain.mm.code.afterevent;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTImplementsList;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * �����б༩���¼���ҪΪ�����е�pk_group��pk_org��pk_org_v���ֶ�����Ĭ��ֵ
 * 
 * @since 6.0
 * @version 2013-11-4 ����10:53:17
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "�����б༩���¼���ҪΪ�����е�pk_group��pk_org��pk_org_v���ֶ�����Ĭ��ֵ   ��", relatedIssueId = "221", coder = "zhongcha", solution = "�ڱ����б༩���¼���������Ϊ�����е�pk_group��pk_org��pk_org_v���ֶ�����Ĭ��ֵ ��")
public class TestCase00221 extends AbstractJavaQueryRuleDefinition {

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

        // ���ڱ���Ƿ������֯�ı��¼�������
        boolean isExsitCorrectHandler = false;
        MapList<String, String> errorMapList = new MapList<String, String>();

        try {
            for (JavaClassResource javaClassResource : resources) {
                // ȡ��java�ļ�������ȥ����׺.java��
                String fileName =
                        javaClassResource.getResourceFileName().substring(0,
                                javaClassResource.getResourceFileName().length() - 5);
                // ��java�ļ�������׺ΪAddEventHandler
                if (!fileName.endsWith("BodyAfterRowEditHandler")) {
                    continue;
                }
                ScanRule scanRule = new ScanRule();
                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                if (scanRule.isOrgChangedHandler() && !isExsitCorrectHandler) {
                    isExsitCorrectHandler = true;
                }
                if (scanRule.isOrgChangedHandler() && MMValueCheck.isNotEmpty(scanRule.getErrorList())) {
                    errorMapList.putAll(fileName, scanRule.getErrorList());
                }
            }
            StringBuilder errorContxt = new StringBuilder();

            if (MMValueCheck.isNotEmpty(errorMapList)) {
                for (String key : errorMapList.keySet()) {
                    errorContxt.append("���������Ϊ" + key + "���ڱ����б༩���¼���������û��Ϊ�������ֶ�" + errorMapList.get(key) + "����Ĭ��ֵ��\n");
                }
            }
            if (!isExsitCorrectHandler) {
                errorContxt.append("������в�����������׺ΪBodyAfterRowEditHandler�ı����б༩���¼������ࡣ\n");
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
     * ɨ��java�ļ��Ĺ��򣨼ٶ��û�������дjava.lang���е��ࣩ
     * 
     * @since 6.0
     * @version 2013-10-30 ����2:02:23
     * @author zhongcha
     */
    private static class ScanRule extends AbstractJavaRule {
        /**
         * ��¼��ǰ���Ƿ�ʵ���˽ӿ�nc.ui.pubapp.uif2app.event.IAppEventHandler
         * ��ֵΪfalseʱ�������ж����ܼ���
         */
        private boolean isCorrectInterface = false;

        private boolean isCorrectEvent = false;

        private String currentPackageName;

        private Map<String, String> importClassMap = new HashMap<String, String>();

        private List<String> errorList = new ArrayList<String>();

        public List<String> getErrorList() {
            return this.errorList;
        }

        public ScanRule() {
            this.errorList.add("pk_group");
            this.errorList.add("pk_org");
            this.errorList.add("pk_org_v");
        }

        /**
         * ���ظñ�ɨ������Ƿ�����֯�ı��¼�������
         * 
         * @return
         */
        public boolean isOrgChangedHandler() {
            return this.isCorrectInterface && this.isCorrectEvent;
        }

        @Override
        public Object visit(ASTMethodDeclaration node, Object data) {
            node.getBlock();

            return super.visit(node, data);
        }

        @Override
        public Object visit(ASTPackageDeclaration node, Object data) {
            List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
            if (astNames.size() == 1) {
                ASTName astName = astNames.get(0);
                this.currentPackageName = astName.getImage();
            }
            else {
                RuleContext ruleContext = (RuleContext) data;
                ruleContext.addRuleViolation(ruleContext, this, node, "", null);
                return data;
            }
            return super.visit(node, data);
        }

        @Override
        public Object visit(ASTImportDeclaration node, Object data) {
            List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
            if (!astNames.isEmpty()) {
                for (ASTName astName : astNames) {
                    String importClassName = astName.getImage();
                    this.importClassMap.put(importClassName.substring(importClassName.lastIndexOf(".") + 1),
                            importClassName);
                }
            }
            return super.visit(node, data);
        }

        /**
         * �жϱ�ɨ������Ƿ�ʵ���˽ӿ�nc.ui.pubapp.uif2app.event.IAppEventHandler
         */
        @Override
        public Object visit(ASTImplementsList node, Object data) {
            List<ASTClassOrInterfaceType> interfaceList = node.findChildrenOfType(ASTClassOrInterfaceType.class);
            List<String> interfaceNames = new ArrayList<String>();
            for (ASTClassOrInterfaceType element : interfaceList) {
                String image = element.getImage().trim();
                if (!image.contains(".")) {
                    image =
                            this.importClassMap.containsKey(image) ? this.importClassMap.get(image) : String.format(
                                    "%s.%s", this.currentPackageName, image);
                }
                interfaceNames.add(image);
            }

            if (interfaceNames.contains("nc.ui.pubapp.uif2app.event.IAppEventHandler")) {
                this.isCorrectInterface = true;
                return super.visit(node, data);
            }
            return data;
        }

        @Override
        public Object visit(ASTName node, Object data) {
            if (this.isCorrectEvent && !this.errorList.isEmpty()) {
                if (MMValueCheck.isNotEmpty(node.getImage())) {
                    String image = node.getImage().trim();
                    if (image.endsWith(".setBodyValue")) {
                        if (MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTPrimaryPrefix.class))
                                && MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTPrimaryPrefix.class)
                                        .getFirstParentOfType(ASTPrimaryExpression.class))) {
                            // ��ú���ָ���������������ʽ
                            ASTPrimaryExpression priExpression =
                                    node.getFirstParentOfType(ASTPrimaryPrefix.class).getFirstParentOfType(
                                            ASTPrimaryExpression.class);
                            if (priExpression.hasDescendantOfType(ASTArguments.class)
                                    && priExpression.findDescendantsOfType(ASTArguments.class).get(0)
                                            .hasDescendantOfType(ASTExpression.class)) {
                                // ��÷�����һ������
                                ASTExpression expression =
                                        priExpression.findDescendantsOfType(ASTArguments.class).get(0)
                                                .findDescendantsOfType(ASTExpression.class).get(0);
                                if (expression.hasDescendantOfType(ASTName.class)) {
                                    String itemname = expression.findDescendantsOfType(ASTName.class).get(0).getImage();
                                    if (MMValueCheck.isNotEmpty(itemname)) {
                                        itemname = itemname.trim();
                                        if ("pk_group".equals(itemname)) {
                                            this.errorList.remove(itemname);
                                        }
                                        if ("pk_org".equals(itemname)) {
                                            this.errorList.remove(itemname);
                                        }
                                        if ("pk_org_v".equals(itemname)) {
                                            this.errorList.remove(itemname);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
            return super.visit(node, data);
        }

        /**
         * ���ݸñ�ɨ����ʵ�ֵĽӿ��е��¼������Ƿ�Ϊnc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent
         */
        @Override
        public Object visit(ASTClassOrInterfaceType node, Object data) {
            if (this.isCorrectInterface) {
                String image = node.getImage().trim();
                if (!image.contains(".")) {
                    image =
                            this.importClassMap.containsKey(image) ? this.importClassMap.get(image) : String.format(
                                    "%s.%s", this.currentPackageName, image);
                }
                if ("nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent".equals(image)) {
                    this.isCorrectEvent = true;
                }

            }

            return super.visit(node, data);
        }
    }

}
