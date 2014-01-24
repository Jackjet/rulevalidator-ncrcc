package com.yonyou.nc.codevalidator.plugin.domain.mm.code.beforerule;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
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
 * 更新BP前规则：判断是否已经存在后续单据的检查规则
 * 
 * @since 6.0
 * @version 2013-11-4 下午2:15:35
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, specialParamDefine = {
    "检查规则",
}, description = "更新BP前规则：判断是否已经存在后续单据的检查规则。若该组件下只有一个节点，参数直接为规则（如：DomUpdateStatusCheckRule）,若存在多个节点，则参数形如dmo@CheckDmoStatusRule#BillUpdateStatusCheckRule（代表：节点编码为dmo下的BP类检测CheckDmoStatusRule和BillUpdateStatusCheckRule），指定节点下只检查指定的规则，组件中若还存在其他节点的BP，执行参数中不带节点的其他规则的检查。) ", relatedIssueId = "287", coder = "zhongcha", solution = "在前规则中添加后续单据的检查规则。", memo = "若该组件下只有一个节点，参数直接为规则（如：DomUpdateStatusCheckRule）,若存在多个节点，则参数形如dmo@CheckDmoStatusRule#BillUpdateStatusCheckRule（代表：节点编码为dmo下的BP类检测CheckDmoStatusRule和BillUpdateStatusCheckRule），指定节点下只检查指定的规则，组件中若还存在其他节点的BP，执行参数中不带节点的其他规则的检查。")
public class TestCase00287 extends AbstractJavaQueryRuleDefinition {
    private final static String COM_PARAM = "commonParameter";

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
        String[] parameters = ruleExecContext.getParameterArray("检查规则");
        MapList<String, String> node2rules = this.getNode2Rules(parameters);
        if (MMValueCheck.isEmpty(node2rules)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "参数为空或者参数不合法。");
            return result;
        }
        // 需要检查的class和其对应需要检查的rule
        MapList<String, String> clz2rules = new MapList<String, String>();
        // 记录没有执行的规则
        MapList<String, String> errorClass2rules = new MapList<String, String>();
        // 记录没有匹配参数的bp类
        List<String> cantCheckClz = new ArrayList<String>();
        // 记录没有addBeforeRule()的类
        List<String> notMethodClz = new ArrayList<String>();
        // 记录参数中节点编码下没找到合法BP的节点编码
        Set<String> nodes = new HashSet<String>();
        nodes.addAll(node2rules.keySet());
        nodes.remove(TestCase00287.COM_PARAM);
        // 需要检查的类的所在package和类名的对应
        Map<String, JavaClassResource> package2BPClzs = new HashMap<String, JavaClassResource>();
        try {
            for (JavaClassResource javaClassResource : resources) {
                String fileName = javaClassResource.getJavaCodeClassName();
                String filePackage = fileName.substring(0, fileName.lastIndexOf("."));
                if (fileName.endsWith("UpdateBP")) {
                    // 用于记录该fileName是不是属于参数中的某个节点下，若在则检查参数中指定的规则，否则执行默认的和参数中不带节点的其他规则
                    boolean isExistNode = false;
                    for (String node : node2rules.keySet()) {
                        if (fileName.contains("." + node + ".")) {
                            // 默认认为同一个包下存在多个后缀相同的类，则类名最短的是需要检查的
                            if (!package2BPClzs.keySet().contains(filePackage)
                                    || package2BPClzs.keySet().contains(filePackage)
                                    && package2BPClzs.get(filePackage).getJavaCodeClassName().length() > fileName
                                            .length()) {
                                package2BPClzs.put(filePackage, javaClassResource);
                                clz2rules.putAll(fileName, node2rules.get(node));
                                nodes.remove(node);
                            }
                            isExistNode = true;
                            break;
                        }
                    }
                    if (!isExistNode && node2rules.keySet().contains(TestCase00287.COM_PARAM)) {
                        // 默认认为同一个包下存在多个后缀相同的类，则类名最短的是需要检查的
                        if (!package2BPClzs.keySet().contains(filePackage)
                                || package2BPClzs.keySet().contains(filePackage)
                                && package2BPClzs.get(filePackage).getJavaCodeClassName().length() > fileName.length()) {
                            package2BPClzs.put(filePackage, javaClassResource);
                            clz2rules.putAll(fileName, node2rules.get(TestCase00287.COM_PARAM));
                        }
                    }
                    else {
                        cantCheckClz.add(fileName);
                    }
                }
            }
            if (MMValueCheck.isNotEmpty(package2BPClzs)) {
                for (JavaClassResource javaClassResource : package2BPClzs.values()) {
                    String fileName = javaClassResource.getJavaCodeClassName();
                    List<String> rulesList = new ArrayList<String>();
                    for (String rule : clz2rules.get(fileName)) {
                        rulesList.add(rule);
                    }
                    ScanUpdateBPRule scanRule = new ScanUpdateBPRule(rulesList);
                    // 扫描该java文件判定是否存在后续单据的检查规则
                    SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                    if (!scanRule.isExistMethod()) {
                        notMethodClz.add(fileName);
                    }
                    if (scanRule.isExistMethod() && !scanRule.getRulesList().isEmpty()) {
                        for (String rule : scanRule.getRulesList()) {
                            errorClass2rules.put(fileName, rule);
                        }
                    }
                }
            }

            StringBuilder errorContxt = new StringBuilder();
            if (MMValueCheck.isNotEmpty(cantCheckClz)) {
                errorContxt.append("该组件中名为" + cantCheckClz + "的BP类没有对应的检查规则，请在参数中匹配其的需要检查的规则。\n");
            }
            else if (MMValueCheck.isEmpty(package2BPClzs)) {
                errorContxt.append("该组件中不存在类名后缀为UpdateBP的更新BP。\n");
            }

            if (MMValueCheck.isNotEmpty(nodes)) {
                errorContxt.append("该组件中不存在名为" + nodes + "的节点（即包路径中不含这些节点名）。\n");
            }
            if (MMValueCheck.isNotEmpty(notMethodClz)) {
                errorContxt.append("该组件中名为" + notMethodClz + "的BP类没有addBeforeRule的方法。\n");
            }
            if (MMValueCheck.isNotEmpty(errorClass2rules)) {
                for (String key : errorClass2rules.keySet()) {
                    errorContxt.append("类名为[ " + key + " ]的BP中的前规则没有执行规则:" + errorClass2rules.get(key) + "。\n");
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
     * 将参数转化为节点编码 -对应- 检查的规则，若不存在节点编码则代表
     * 
     * @param parameters
     * @return
     */
    public MapList<String, String> getNode2Rules(String[] parameters) {

        if (MMValueCheck.isEmpty(parameters)) {
            return null;
        }
        MapList<String, String> node2rules = new MapList<String, String>();
        for (String parameter : parameters) {
            if (parameter.indexOf("@") <= 0) {
                if (parameter.indexOf("@") == 0) {
                    parameter = parameter.substring(1);
                }
                String[] rules = parameter.split("#");
                for (String rule : rules) {
                    node2rules.put(TestCase00287.COM_PARAM, rule);
                }
            }
            else {
                String[] clsAndRuls = parameter.split("@");
                String[] classNames = clsAndRuls[0].split("#");
                String[] ruleNames = clsAndRuls[1].split("#");
                for (String className : classNames) {
                    for (String ruleName : ruleNames) {
                        node2rules.put(className.trim(), ruleName.trim());
                    }
                }
            }
        }
        return node2rules;
    }

    /**
     * 扫描java文件的规则
     * 
     * @since 6.0
     * @version 2013-10-30 下午2:02:43
     * @author zhongcha
     */
    private static class ScanUpdateBPRule extends AbstractJavaRule {

        private boolean isExistMethod = false;

        public boolean isExistMethod() {
            return this.isExistMethod;
        }

        private List<String> rulesList = new ArrayList<String>();

        public List<String> getRulesList() {
            return this.rulesList;
        }

        public ScanUpdateBPRule(List<String> rulesList) {
            this.rulesList = rulesList;
        }

        @Override
        public Object visit(ASTMethodDeclarator node, Object data) {
            if (MMValueCheck.isNotEmpty(node.getImage()) && "addBeforeRule".equals(node.getImage().trim())) {
                if (!this.isExistMethod) {
                    this.isExistMethod = true;
                }

                if (MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTMethodDeclaration.class))
                        && node.getFirstParentOfType(ASTMethodDeclaration.class).hasDecendantOfAnyType(
                                ASTBlockStatement.class)) {
                    List<ASTBlockStatement> blockStatemtList =
                            node.getFirstParentOfType(ASTMethodDeclaration.class).findDescendantsOfType(
                                    ASTBlockStatement.class);
                    for (ASTBlockStatement blockStatemt : blockStatemtList) {
                        if (MMValueCheck
                                .isNotEmpty(blockStatemt.getFirstChildOfType(ASTLocalVariableDeclaration.class))
                                && blockStatemt.getFirstChildOfType(ASTLocalVariableDeclaration.class)
                                        .hasDecendantOfAnyType(ASTAllocationExpression.class)) {
                            ASTClassOrInterfaceType ruleType =
                                    blockStatemt.getFirstChildOfType(ASTLocalVariableDeclaration.class)
                                            .findDescendantsOfType(ASTAllocationExpression.class).get(0)
                                            .getFirstChildOfType(ASTClassOrInterfaceType.class);
                            if (MMValueCheck.isNotEmpty(ruleType) && MMValueCheck.isNotEmpty(ruleType.getImage())) {
                                String image = ruleType.getImage().trim();
                                if (image.contains(".")) {
                                    image = image.substring(image.lastIndexOf('.') + 1, image.length());
                                }
                                this.rulesList.remove(image);
                            }
                        }
                    }
                }
            }
            return super.visit(node, data);
        }

    }

}
