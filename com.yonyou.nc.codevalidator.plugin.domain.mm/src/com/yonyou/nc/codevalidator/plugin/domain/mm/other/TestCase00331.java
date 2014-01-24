package com.yonyou.nc.codevalidator.plugin.domain.mm.other;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

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
 * 插入点是否可用。 通过关键字符*PluginPoint.java取bs包下的*PluginPoint.java文件.
 * 通过关键字BP取出业务规则文件*BP.java，检查其中引用的插入点枚举类是否可用。
 * 
 * @author wangfra
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "检查插入点是否可用。", relatedIssueId = "331", coder = "wangfra", solution = "通过关键字BP取出业务规则文件*BP.java，检查其中引用的插入点枚举类是否可用.")
public class TestCase00331 extends AbstractJavaQueryRuleDefinition {

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
        Map<String, JavaClassResource> pluginPointFilesResMap = new HashMap<String, JavaClassResource>();
        Map<String, JavaClassResource> bpFilesResMap = new HashMap<String, JavaClassResource>();
        for (final JavaClassResource javaClassResource : resources) {

            String className = javaClassResource.getJavaCodeClassName();
            if (className.endsWith("PluginPoint")) {
                pluginPointFilesResMap.put(className, javaClassResource);
            }
            if (className.endsWith("BP")) {
                String key = className.substring(className.lastIndexOf(".") + 1);
                bpFilesResMap.put(key, javaClassResource);
            }
        }

        this.checkPluginPint(pluginPointFilesResMap, bpFilesResMap, ruleExecContext, result);
        // Set<String> keys = bpFilesResMap.keySet();
        // for (String key : keys) {
        //
        // JavaClassResource javaClassRes = bpFilesResMap.get(key);
        //
        // // 检查
        // this.check(javaClassRes, ruleExecContext, result);
        //
        // }

        return result;
    }

    /**
     * 检查插入点是否可用
     * 
     * @param pluginPointFilesResMap
     * @param bpFilesResMap
     * @param ruleExecContext
     * @param result
     * @return
     * @throws RuleBaseException
     */
    private ResourceRuleExecuteResult checkPluginPint(Map<String, JavaClassResource> pluginPointFilesResMap,
            Map<String, JavaClassResource> bpFilesResMap, IRuleExecuteContext ruleExecContext,
            ResourceRuleExecuteResult result) throws RuleBaseException {
        List<String> errorClzs = new ArrayList<String>();
        for (String key : bpFilesResMap.keySet()) {
            JavaClassResource javaClassRes = bpFilesResMap.get(key);
            String className = javaClassRes.getJavaCodeClassName();
            ScanPluginRule scanPluginRule = new ScanPluginRule(className);
            try {
                SourceCodeProcessor.parseRule(javaClassRes.getResourcePath(), scanPluginRule);
            }
            catch (FileNotFoundException e) {
                Logger.error(e.getMessage(), e);
            }
            // 如果BP中引用了未定义的插入点枚举类，则不通过
            if (!new ArrayList<String>(pluginPointFilesResMap.keySet()).containsAll(scanPluginRule
                    .getPluginPointsFileNames())) {
                errorClzs.add(className);
            }
        }
        StringBuilder errorContxt = new StringBuilder();
        if (MMValueCheck.isNotEmpty(errorClzs)) {
            errorContxt.append("BP文件" + errorClzs + "中引用的插入点枚举类不可用，请检查确认。\n");
        }
        if (MMValueCheck.isNotEmpty(errorContxt)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    errorContxt.toString());
        }

        return result;

    }

    // /**
    // *
    // * @param pluginPointFilesResMap
    // * @param ruleExecContext
    // * @return
    // * @throws RuleClassLoadException
    // */
    // private MapList<String, String> getPluginPointValues(
    // Map<String, JavaClassResource> pluginPointFilesResMap,
    // IRuleExecuteContext ruleExecContext) {
    // MapList<String, String> pluginPointValues = new MapList<String, String>();
    // Set<String> keys = pluginPointFilesResMap.keySet();
    // Class<?> loadClass = null;
    // for (String key : keys) {
    // JavaClassResource javaClassResource = pluginPointFilesResMap.get(key);
    // String className = javaClassResource.getJavaCodeClassName();
    // if(null == className || ""==className){
    // continue;
    // }
    // try {
    // loadClass = ClassLoaderUtilsFactory.getClassLoaderUtils()
    // .loadClass(
    // ruleExecContext.getBusinessComponent()
    // .getProjectName(), className);
    // } catch (RuleClassLoadException e) {
    // e.printStackTrace();
    // }
    // Object[] enums = loadClass.getEnumConstants();
    // for (Object value : enums) {
    // pluginPointValues.put(className, value.toString().trim());
    // }
    // }
    // return pluginPointValues;
    // }

    /**
     * 假定用户不会重写java.lang包中的类
     * 
     * @author wangfra
     */
    public static class ScanPluginRule extends AbstractJavaRule {

        private String className = null;

        private Set<String> pluginPointsFileNames = new HashSet<String>();

        public Set<String> getPluginPointsFileNames() {
            return this.pluginPointsFileNames;
        }

        ScanPluginRule(String className) {
            this.className = className;
        }

        @Override
        public Object visit(ASTImportDeclaration node, Object data) {
            // 获得引入声明
            List<ASTName> importDeclarationsList = node.findChildrenOfType(ASTName.class);
            if (!importDeclarationsList.isEmpty()) {
                for (ASTName enumConstant : importDeclarationsList) {
                    if (null != enumConstant.getImage() && enumConstant.getImage().trim().endsWith("PluginPoint")) {
                        this.pluginPointsFileNames.add(enumConstant.getImage().trim());
                    }
                }
            }
            return super.visit(node, data);
        }

        public String getClassName() {
            return this.className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

    }

    // private void check(JavaClassResource javaClassResource,
    // IRuleExecuteContext ruleExecContext,
    // ResourceRuleExecuteResult result) {
    // try {
    // StringBuilder noteBuilder = new StringBuilder();
    // String className = javaClassResource.getJavaCodeClassName();
    //
    // Class<?> loadClass;
    //
    // loadClass = ClassLoaderUtilsFactory.getClassLoaderUtils()
    // .loadClass(
    // ruleExecContext.getBusinessComponent()
    // .getProjectName(), className);
    //
    // RuleContext ruleContext = SourceCodeProcessor.parseRule(
    // javaClassResource.getResourcePath(),
    // new ScanPluginPointRule(className));
    // List<RuleViolation> ruleViolationList = ruleContext
    // .getRuleViolationList();
    // // ***********************************************************************
    // Object[] enums = loadClass.getEnumConstants();
    //
    // /*
    // * 执行 getComponent(), getModule(), getPoint()三个方法，如果有一个返回null，则不通过
    // */
    //
    // if (MMArrayUtil.isEmpty(enums)) {
    // noteBuilder.append(String.format(
    // "当前节点的规则插入点没有正确定义枚举常量,检查枚举类【%s】\n ！",
    // javaClassResource.getJavaCodeClassName()));
    // } else {
    //
    // }
    // } catch (IllegalArgumentException e) {
    //
    // e.printStackTrace();
    // } catch (RuleClassLoadException e) {
    //
    // e.printStackTrace();
    //
    // } catch (SecurityException e) {
    //
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    //
    // /**
    // * 假定用户不会重写java.lang包中的类
    // *
    // * @author wangfra
    // */
    // public static class ScanPluginPointRule extends AbstractJavaRule {
    //
    //
    // private String className;
    //
    // private MapList<String, String> classPluginPointsMapList = new MapList<String, String>();
    //
    // private Set<String> pluginPointsFileNames = new HashSet<String>();
    //
    // ScanPluginPointRule(String className) {
    // this.className = className;
    // }
    //
    //
    // @Override
    // public Object visit(ASTArguments node, Object data) {
    // List<ASTName> enumConstants = node
    // .findDescendantsOfType(ASTName.class);
    // if (!enumConstants.isEmpty()) {
    // for (ASTName enumConstant : enumConstants) {
    // if (null != enumConstant.getImage()&&enumConstant.getImage().startsWith("BomPluginPoint")) {
    // System.out.println("##############ASTArguments########################");
    // System.out.println(className +"******" + enumConstant.getImage().trim());
    // classPluginPointsMapList.put(className, enumConstant.getImage().trim());
    // }
    // }
    // }
    // return visit((JavaNode) node, data);
    // }
    //
    // @Override
    // public Object visit(ASTImportDeclaration node, Object data) {
    // // 获得引入声明
    // List<ASTName> importDeclarationsList = node.findChildrenOfType(ASTName.class);
    // if (!importDeclarationsList.isEmpty()) {
    // for (ASTName enumConstant : importDeclarationsList) {
    // if (null != enumConstant.getImage()&&enumConstant.getImage().endsWith("PluginPoint")) {
    // System.out.println("##############ASTImportDeclaration########################");
    // System.out.println(className +"******" + enumConstant.getImage().trim());
    // pluginPointsFileNames.add(enumConstant.getImage().trim());
    // }
    // }
    // }
    // return super.visit(node, data);
    // }
    //
    // }
}
