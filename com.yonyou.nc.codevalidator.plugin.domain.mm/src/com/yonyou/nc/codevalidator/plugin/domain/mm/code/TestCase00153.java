package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * 创建VO类（聚合VO、表头VO、表体VO、VO元数据类、枚举类），VO只是承载数据的对象，不能包含任何业务逻辑 主设计 是 自动化测试
 * nc.test.mm.autotest.bill.testcase.classbased.pub.vo.TestCase000078
 * 
 * @author mazhqa
 * @reporter 刘宝权 生产制造原有用例
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, description = "创建VO类（聚合VO、表头VO、表体VO、VO元数据类、枚举类），VO只是承载数据的对象，不能包含任何业务逻辑 主设计 是 自动化测试", memo = "nc.test.mm.autotest.bill.testcase.classbased.pub.vo.TestCase000078", solution = "", subCatalog = SubCatalogEnum.JC_CODECRITERION, coder = "qiaoyang", relatedIssueId = "153")
public class TestCase00153 extends AbstractJavaQueryRuleDefinition {

    private static final String SUPERVO_CLASS = "nc.vo.pub.SuperVO";

    private static final List<String> LEGAL_METHOD_NAMES = Arrays.asList("getMetaData", "getDr", "setDr", "getTs",
            "setTs", "getParentPKFieldName", "getPKFieldName", "getTableName", "equals");

    @Override
    protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext)
            throws ResourceParserException {
        JavaResourceQuery resourceQuery = new JavaResourceQuery();
        resourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        resourceQuery.setResPrivilege(JavaResPrivilege.PUBLIC);
        return resourceQuery;
    }

    @Override
    protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
            throws ResourceParserException {
        BusinessComponent businessComponent = ruleExecContext.getBusinessComponent();
        final IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        StringBuffer noteBuilder = new StringBuffer();
        for (JavaClassResource resource : resources) {
            final String javaCodeClassName = resource.getJavaCodeClassName();
            if (!javaCodeClassName.contains(".entity.")) {
                continue;
            }
            try {
                boolean isSuperVoClass =
                        classLoaderUtils.isParentClass(businessComponent.getProjectName(), javaCodeClassName,
                                TestCase00153.SUPERVO_CLASS);
                if (isSuperVoClass) {
                    List<String> attributeNames =
                            Arrays.asList(this.initAttributeNames(classLoaderUtils, businessComponent,
                                    javaCodeClassName));
                    CompilationUnit compilationUnit = JavaParser.parse(new File(resource.getResourcePath()));
                    final List<String> methodNames = new ArrayList<String>();
                    VoidVisitorAdapter<List<String>> visitor = new VoidVisitorAdapter<List<String>>() {

                        @Override
                        public void visit(MethodDeclaration md, List<String> attributeNames) {
                            String attribute = this.getAttributeName(md.getName());
                            if (attributeNames.contains(attribute)) {
                                return;
                            }
                            else if (TestCase00153.LEGAL_METHOD_NAMES.contains(md.getName())) {
                                return;
                            }
                            else {
                                methodNames.add(md.getName());
                            }
                        }

                        /**
                         * 根据方法名称取得属性名
                         * 
                         * @param methodName
                         */
                        private String getAttributeName(String methodName) {
                            if (methodName == null || methodName.length() < 4) {
                                return null;
                            }
                            String name = methodName.substring(3);
                            return name.toLowerCase();
                        }

                    };
                    visitor.visit(compilationUnit, attributeNames);
                    if (methodNames.size() > 0) {
                        noteBuilder.append(String.format(javaCodeClassName + "包含额外的业务方法: %s \n", methodNames));
                    }
                }
            }
            catch (RuleClassLoadException e) {
                Logger.error(e.getMessage(), e);
            }
            catch (ParseException e) {
                Logger.error(e.getMessage(), e);
            }
            catch (IOException e) {
                Logger.error(e.getMessage(), e);
            }
        }
        if (noteBuilder.length() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    noteBuilder.toString());
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private String[] initAttributeNames(IClassLoaderUtils classLoaderUtils, BusinessComponent businessComponent,
            String javaCodeClassName) throws RuleClassLoadException {
        Class loadVoClass = classLoaderUtils.loadClass(businessComponent.getProjectName(), javaCodeClassName);
        Field[] fields = loadVoClass.getDeclaredFields();
        if (fields == null) {
            return null;
        }
        String[] attributenNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            attributenNames[i] = fields[i].getName().toLowerCase();
        }
        return attributenNames;
    }

}
