package com.yonyou.nc.codevalidator.plugin.domain.mm.code.itf;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * �����ӿڵķ���Ӧ��֧������������Ҳ����˵�����������������
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "�����ӿڵķ���Ӧ��֧������������Ҳ����˵����������������� ", relatedIssueId = "162", coder = "lijbe", solution = "�����ӿڵķ���Ӧ��֧������������Ҳ����˵�����������������")
public class TestCase00162 extends AbstractRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {

        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        UpmResourceQuery upmResourceQuery = new UpmResourceQuery();
        upmResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
        for (UpmResource upmResource : upmResources) {
            UpmModuleVO upmModuleVo = upmResource.getUpmModuleVo();
            Map<String, String> serviceImplToItfClassName = this.getServiceImplClassName(upmModuleVo);
            List<String> serviceImplClassNameList =
                    Arrays.asList(serviceImplToItfClassName.keySet().toArray(new String[0]));
            JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
            javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
            javaResourceQuery.setResPrivilege(JavaResPrivilege.PRIVATE);
            javaResourceQuery.setClassNameFilterList(serviceImplClassNameList);
            List<JavaClassResource> javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);

            for (final JavaClassResource javaClassResource : javaResourceList) {
                try {
                    String itfName = serviceImplToItfClassName.get(javaClassResource.getJavaCodeClassName());
                    Class<?> loadClass =
                            ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(
                                    ruleExecContext.getBusinessComponent().getProjectName(), itfName);
                    Method[] methods = loadClass.getMethods();
                    final List<String> itfMethodnames = new ArrayList<String>();
                    for (Method method : methods) {
                        itfMethodnames.add(method.getName());
                    }
                    CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
                    final StringBuilder noteBuilder = new StringBuilder();
                    VoidVisitorAdapter<Void> visitor = new VoidVisitorAdapter<Void>() {
                        @Override
                        public void visit(MethodDeclaration n, Void v) {
                            boolean isArray = true;
                            List<Parameter> paramList = n.getParameters();
                            if (paramList != null && !paramList.isEmpty()) {
                                for (Parameter param : paramList) {

                                    isArray = TestCase00162.this.isArray(param);
                                    if (!isArray) {
                                        break;
                                    }

                                }

                            }
                            if (!isArray) {
                                noteBuilder.append(String.format("������ %s ������������������,���޸�! \n",
                                        javaClassResource.getJavaCodeClassName() + "." + n.getName()));
                            }
                        }
                    };
                    visitor.visit(compilationUnit, null);
                    if (noteBuilder.toString().length() > 0) {
                        result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
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

    /**
     * @param upmModuleVO
     * @return Map<String, String>
     */
    private Map<String, String> getServiceImplClassName(UpmModuleVO upmModuleVO) {
        Map<String, String> result = new HashMap<String, String>();
        List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
        if (pubComponentVoList != null && pubComponentVoList.size() > 0) {
            for (UpmComponentVO upmComponentVO : pubComponentVoList) {
                if (upmComponentVO.getInterfaceName() != null) {
                    result.put(upmComponentVO.getImplementationName(), upmComponentVO.getInterfaceName());
                }
            }
        }
        List<UpmComponentVO> priComponentVoList = upmModuleVO.getPriComponentVoList();
        if (priComponentVoList != null && priComponentVoList.size() > 0) {
            for (UpmComponentVO upmComponentVO : priComponentVoList) {
                if (upmComponentVO.getInterfaceName() != null) {
                    result.put(upmComponentVO.getImplementationName(), upmComponentVO.getInterfaceName());
                }
            }
        }
        return result;
    }

    private boolean isArray(Parameter param) {

        String paramType = param.getType().toString();
        if (paramType == null) {
            return false;
        }
        String type = paramType.toString().trim();
        if ("void".equals(type)) {
            return true;
        }
        if (type.endsWith("[]")) {
            return true;
        }
        type = type.trim();
        // ȥ������
        if (type.indexOf("<") > 0) {
            type = type.substring(0, type.indexOf("<"));
        }
        // ����ط��õ�����ȫ������������ֻ���ж����һ���ؼ��֣��ǲ���Set��List��map;
        if (this.getSet().contains(type)) {
            return true;
        }
        return false;
    }

    /**
     * ֧�ֵļ���
     * 
     * @return
     */
    private List<String> getSet() {
        List<String> list = new ArrayList<String>();
        list.add("Map");
        list.add("Set");
        list.add("List");
        list.add("MapList");
        list.add("java.util.Map");
        list.add("java.util.Set");
        list.add("java.util.List");
        return list;
    }

}
