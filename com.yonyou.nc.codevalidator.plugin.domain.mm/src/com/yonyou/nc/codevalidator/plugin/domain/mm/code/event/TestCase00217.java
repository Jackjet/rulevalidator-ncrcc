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
 * 主组织改变时清空界面数据，自动表体增行
 * 
 * @since 6.0
 * @version 2013-11-4 上午10:53:17
 * @author zhongcha
 */
@RuleDefinition(catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "主组织改变时清空界面数据，自动表体增行。", relatedIssueId = "217", coder = "zhongcha", solution = "在主组织改变事件处理类中增加billform.addnew()的方法。")
public class TestCase00217 extends AbstractJavaQueryRuleDefinition {

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
        // 记录没有调用addnew()的类
        List<String> notMethodClz = new ArrayList<String>();

        try {
            for (JavaClassResource javaClassResource : resources) {
                // 取得java文件的名（去掉后缀.java）
                String fileName =
                        javaClassResource.getResourceFileName().substring(0,
                                javaClassResource.getResourceFileName().length() - 5);
                // 该java文件的名后缀为OrgChangedHandler
                if (!fileName.endsWith("OrgChangedHandler")) {
                    continue;
                }
                ScanRule scanRule = new ScanRule();
                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                if (scanRule.isOrgChangedHandler() && !isExsitCorrectHandler) {
                    isExsitCorrectHandler = true;
                }
                if (scanRule.isOrgChangedHandler() && !scanRule.isExistMethod()) {
                    notMethodClz.add(fileName);
                }
            }
            StringBuilder errorContxt = new StringBuilder();

            if (!notMethodClz.isEmpty()) {
                errorContxt.append("该组件中名为" + notMethodClz + "的组织改变事件处理类中没有自动表体增行。\n");
            }
            if (!isExsitCorrectHandler) {
                errorContxt.append("该组件中不存在类名后缀为OrgChangedHandler的组织改变事件处理类。\n");
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

        private boolean isExistMethod = false;

        public boolean isExistMethod() {
            return this.isExistMethod;
        }

        /**
         * 返回该被扫描的类是否是组织改变事件处理类
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

        /**
         * 判断该被扫描的类中是否调用了指定的方法，调用了，根据情况处理相应的值
         */
        @Override
        public Object visit(ASTName node, Object data) {
            if (this.isCorrectEvent) {
                String image = node.getImage().trim();
                if (image.endsWith(".addNew")) {
                    this.isExistMethod = true;
                    return data;
                }
            }
            return super.visit(node, data);
        }

        /**
         * 由于该工具存在问题。扫描billForm.addNew()和this.billForm.addNew()得到的结果不同
         * 该处的判断是为了判断是否存在第二种情况
         */
        @Override
        public Object visit(ASTPrimarySuffix node, Object data) {
            if (this.isCorrectEvent) {
                if (MMValueCheck.isNotEmpty(node.getImage())) {
                    String image = node.getImage().trim();
                    if (image.endsWith("addNew")) {
                        this.isExistMethod = true;
                        return data;
                    }
                }
            }
            return super.visit(node, data);
        }

        /**
         * 根据该被扫描类实现的接口中的事件类型是否为nc.ui.pubapp.uif2app.event.OrgChangedEvent
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
                if ("nc.ui.pubapp.uif2app.event.OrgChangedEvent".equals(image)) {
                    this.isCorrectEvent = true;
                }

            }

            return super.visit(node, data);
        }
    }

}
