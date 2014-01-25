package com.yonyou.nc.codevalidator.plugin.domain.mm.code.event;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTImplementsList;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 新增事件需要为单据的单据日期等设置默认值（可选）
 * 
 * @since 6.0
 * @version 2013-11-4 上午10:53:17
 * @author zhongcha
 */
@RuleDefinition(catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "新增事件需要为单据的单据日期等设置默认值（可选）。", relatedIssueId = "220", coder = "zhongcha", solution = "在新增事件处理类中为单据的单据日期等设置默认值 。")
public class TestCase00220 extends AbstractJavaQueryRuleDefinition {

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

        // 用于标记是否存在组织改变事件处理类
        boolean isExsitCorrectHandler = false;
        List<String> errorList = new ArrayList<String>();

        try {
            for (JavaClassResource javaClassResource : resources) {
                // 取得java文件的名（去掉后缀.java）
                String fileName =
                        javaClassResource.getResourceFileName().substring(0,
                                javaClassResource.getResourceFileName().length() - 5);
                // 该java文件的名后缀为AddEventHandler
                if (!fileName.endsWith("AddEventHandler")) {
                    continue;
                }
                ScanRule scanRule = new ScanRule();
                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                if (scanRule.isAddEventHandler() && !isExsitCorrectHandler) {
                    isExsitCorrectHandler = true;
                }
                if (!scanRule.isCorrectClz()) {
                    errorList.add(fileName);
                }
            }
            StringBuilder errorContxt = new StringBuilder();

            if (MMValueCheck.isNotEmpty(errorList)) {
                errorContxt.append("该组件中名为" + errorList + "的新增事件处理类中没有为单据的单据日期等设置默认值。\n");
            }
            if (!isExsitCorrectHandler) {
                errorContxt.append("该组件中不存在类名后缀为AddEventHandler的组织改变事件处理类。\n");
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
     * 扫描java文件的规则（假定用户不会重写java.lang包中的类）
     * 
     * @since 6.0
     * @version 2013-10-30 下午2:02:23
     * @author zhongcha
     */
    private static class ScanRule extends AbstractJavaRule {
        /**
         * 记录当前类是否实现了接口nc.ui.pubapp.uif2app.event.IAppEventHandler
         * 其值为false时，其他判定不能继续
         */
        private boolean isCorrectInterface = false;

        private boolean isCorrectEvent = false;

        private String currentPackageName;

        private Map<String, String> importClassMap = new HashMap<String, String>();

        private boolean isCorrectClz = false;

        public boolean isCorrectClz() {
            return this.isCorrectClz;
        }

        /**
         * 返回该被扫描的类是否是组织改变事件处理类
         * 
         * @return
         */
        public boolean isAddEventHandler() {
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
         * 判断被扫描的类是否实现了接口nc.ui.pubapp.uif2app.event.IAppEventHandler
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
            if (this.isCorrectEvent && !this.isCorrectClz) {
                if (MMValueCheck.isNotEmpty(node.getImage())) {
                    String image = node.getImage().trim();
                    // 此处的就是为了判断代码是否存在AppContext.getInstance.getBusiDate（这里这么复杂，是为了判空）
                    if (image.endsWith("AppContext.getInstance")
                            && MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTPrimaryPrefix.class))
                            && MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTPrimaryPrefix.class)
                                    .getFirstParentOfType(ASTPrimaryExpression.class))) {
                        List<ASTPrimarySuffix> primarySuffix =
                                node.getFirstParentOfType(ASTPrimaryPrefix.class)
                                        .getFirstParentOfType(ASTPrimaryExpression.class)
                                        .findChildrenOfType(ASTPrimarySuffix.class);
                        if (MMValueCheck.isNotEmpty(primarySuffix) && primarySuffix.size() >= 2) {
                            String temp = primarySuffix.get(1).getImage();
                            if (MMValueCheck.isNotEmpty(temp) && "getBusiDate".equals(temp.trim())) {
                                this.isCorrectClz = true;
                                return data;
                            }
                        }
                    }
                }
            }
            return super.visit(node, data);
        }

        /**
         * 根据该被扫描类实现的接口中的事件类型是否为nc.ui.pubapp.uif2app.event.billform.AddEvent
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
                if ("nc.ui.pubapp.uif2app.event.billform.AddEvent".equals(image)) {
                    this.isCorrectEvent = true;
                }

            }

            return super.visit(node, data);
        }
    }

}
