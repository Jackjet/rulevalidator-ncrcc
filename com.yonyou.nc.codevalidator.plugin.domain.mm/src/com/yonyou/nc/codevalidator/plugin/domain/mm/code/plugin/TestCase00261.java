package com.yonyou.nc.codevalidator.plugin.domain.mm.code.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMArrayUtil;
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
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * 插入点是否正确定义 通过关键字符*PluginPoint.java取bs包下的*PluginPoint.java文件.反射,得到一个枚举实例,
 * 判断如果存在这些iPluginPoint.getComponent() == null || iPluginPoint.getModule() ==
 * null || iPluginPoint.getPoint() == null 则处理不正确
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "插入点是否正确定义", relatedIssueId = "261", coder = "lijbe", solution = "通过关键字符PluginPoint取bs包下的*PluginPoint.java文件.反射,得到一个枚举实例，判断如果【getComponent() == null || getModule() == null ||getPoint() == null 】则处理不正确.")
public class TestCase00261 extends AbstractJavaQueryRuleDefinition {

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
        Map<String, JavaClassResource> javaClazzResMap = new HashMap<String, JavaClassResource>();

        for (final JavaClassResource javaClassResource : resources) {

            String className = javaClassResource.getJavaCodeClassName();

            if (className.endsWith("PluginPoint")) {
                String key = className.substring(0, className.lastIndexOf("."));
                javaClazzResMap.put(key, javaClassResource);
            }
        }

        Set<String> keys = javaClazzResMap.keySet();
        for (String key : keys) {

            JavaClassResource javaClassRes = javaClazzResMap.get(key);

            // 检查
            this.check(javaClassRes, ruleExecContext, result);

        }

        return result;
    }

    private void check(JavaClassResource javaClassResource, IRuleExecuteContext ruleExecContext,
            ResourceRuleExecuteResult result) {
        try {
            StringBuilder noteBuilder = new StringBuilder();
            String className = javaClassResource.getJavaCodeClassName();

            Class<?> loadClass;

            loadClass =
                    ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(
                            ruleExecContext.getBusinessComponent().getProjectName(), className);

            /*
             * 执行 getComponent(), getModule(), getPoint()三个方法，如果有一个返回null，则不通过
             */
            Object[] enums = loadClass.getEnumConstants();

            if (MMArrayUtil.isEmpty(enums)) {
                noteBuilder.append(String.format("当前节点的规则插入点没有正确定义枚举常量,检查枚举类【%s】\n ！",
                        javaClassResource.getJavaCodeClassName()));
            }
            else {
                Method getComponentMethod = loadClass.getMethod("getComponent");

                Method getModuleMethod = loadClass.getMethod("getModule");

                Method getPointMethod = loadClass.getMethod("getPoint");

                Object component = getComponentMethod.invoke(enums[0]);

                Object module = getModuleMethod.invoke(enums[0]);

                Object point = getPointMethod.invoke(enums[0]);
                if (MMValueCheck.isEmpty(component) || MMValueCheck.isEmpty(module) || MMValueCheck.isEmpty(point)) {
                    noteBuilder.append(String.format("当前节点的规则插入点没有正确定义相关get方法,请检查类 【%s】\n!",
                            javaClassResource.getJavaCodeClassName()));
                }

            }
            if (noteBuilder.toString().length() > 0) {
                result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
            }
        }
        catch (IllegalArgumentException e) {
            Logger.error(e.getMessage(), e);
        }
        catch (IllegalAccessException e) {
            Logger.error(e.getMessage(), e);
        }
        catch (InvocationTargetException e) {
            Logger.error(e.getMessage(), e);

        }
        catch (RuleClassLoadException e) {
            Logger.error(e.getMessage(), e);

        }
        catch (SecurityException e) {
            Logger.error(e.getMessage(), e);
        }
        catch (NoSuchMethodException e) {
            Logger.error(e.getMessage(), e);
        }
    }

}
