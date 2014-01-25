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
 * ������Ƿ���ȷ���� ͨ���ؼ��ַ�*PluginPoint.javaȡbs���µ�*PluginPoint.java�ļ�.����,�õ�һ��ö��ʵ��,
 * �ж����������ЩiPluginPoint.getComponent() == null || iPluginPoint.getModule() ==
 * null || iPluginPoint.getPoint() == null ������ȷ
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "������Ƿ���ȷ����", relatedIssueId = "261", coder = "lijbe", solution = "ͨ���ؼ��ַ�PluginPointȡbs���µ�*PluginPoint.java�ļ�.����,�õ�һ��ö��ʵ�����ж������getComponent() == null || getModule() == null ||getPoint() == null ��������ȷ.")
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

            // ���
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
             * ִ�� getComponent(), getModule(), getPoint()���������������һ������null����ͨ��
             */
            Object[] enums = loadClass.getEnumConstants();

            if (MMArrayUtil.isEmpty(enums)) {
                noteBuilder.append(String.format("��ǰ�ڵ�Ĺ�������û����ȷ����ö�ٳ���,���ö���ࡾ%s��\n ��",
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
                    noteBuilder.append(String.format("��ǰ�ڵ�Ĺ�������û����ȷ�������get����,������ ��%s��\n!",
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
