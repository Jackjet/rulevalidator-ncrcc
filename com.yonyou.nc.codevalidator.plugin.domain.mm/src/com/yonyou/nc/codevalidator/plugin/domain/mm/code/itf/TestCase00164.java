package com.yonyou.nc.codevalidator.plugin.domain.mm.code.itf;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * 操作接口中定义的方法应该有异常声明BusinessException
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "操作接口中定义的方法应该有异常声明【BusinessException】", relatedIssueId = "164", coder = "lijbe", solution = "操作接口中定义的方法应该有异常声明【声明的异常为BusinessException】")
public class TestCase00164 extends AbstractRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {

        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        UpmResourceQuery upmResourceQuery = new UpmResourceQuery();
        upmResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
        // 接口方法分析类
        InterfaceVisitorAdapter visitor = new InterfaceVisitorAdapter();
        for (UpmResource upmResource : upmResources) {
            UpmModuleVO upmModuleVo = upmResource.getUpmModuleVo();
            List<String> serviceItfClassNameList = this.getServiceItfClassName(upmModuleVo);
            if (MMValueCheck.isEmpty(serviceItfClassNameList)) {
                continue;
            }
            List<JavaClassResource> javaResourceList = this.getJavaResources(ruleExecContext, serviceItfClassNameList);
            StringBuilder noteBuilder = new StringBuilder();
            for (JavaClassResource javaClassResource : javaResourceList) {
                try {
                    CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
                    visitor.visit(compilationUnit, null);
                    if (MMValueCheck.isNotEmpty(visitor.getNoExprMethodList())) {
                        this.appendMsg(noteBuilder, javaClassResource, "没有进行异常声明", visitor.getWrongExprMethodList());
                    }
                    if (MMValueCheck.isNotEmpty(visitor.getWrongExprMethodList())) {
                        this.appendMsg(noteBuilder, javaClassResource, "异常声明不正确,没有声明BusinessException",
                                visitor.getWrongExprMethodList());
                    }
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
     * 取得upm文件中的所有接口
     * 
     * @param upmModuleVO
     */
    private List<String> getServiceItfClassName(UpmModuleVO upmModuleVO) {
        List<String> itfList = new ArrayList<String>();
        List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
        if (pubComponentVoList != null && pubComponentVoList.size() > 0) {
            for (UpmComponentVO upmComponentVO : pubComponentVoList) {
                if (upmComponentVO.getInterfaceName() != null) {
                    itfList.add(upmComponentVO.getInterfaceName());
                }
            }
        }
        List<UpmComponentVO> priComponentVoList = upmModuleVO.getPriComponentVoList();
        if (priComponentVoList != null && priComponentVoList.size() > 0) {
            for (UpmComponentVO upmComponentVO : priComponentVoList) {
                if (upmComponentVO.getInterfaceName() != null) {
                    itfList.add(upmComponentVO.getInterfaceName());
                }
            }
        }
        return itfList;
    }

    private List<JavaClassResource> getJavaResources(IRuleExecuteContext ruleExecContext, List<String> filterClazzs)
            throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        javaResourceQuery.setResPrivilege(JavaResPrivilege.PUBLIC);
        javaResourceQuery.setClassNameFilterList(filterClazzs);

        List<JavaClassResource> javaResourceList = new ArrayList<JavaClassResource>();
        javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);

        return javaResourceList;
    }

    private void appendMsg(StringBuilder noteBuilder, JavaClassResource javaClassResource, String endWith,
            List<String> methods) {
        for (String m : methods) {
            noteBuilder
                    .append(String.format("方法【%s】%s！\n", javaClassResource.getJavaCodeClassName() + "." + m, endWith));
        }

    }

    /**
     * 扫miao接口中的方法
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class InterfaceVisitorAdapter extends VoidVisitorAdapter<Void> {

        /**
         * 装载没有异常声明的方法
         */
        List<String> noExprMethodList = new ArrayList<String>();

        /**
         * 异常声明不为BusinessException的方法
         */
        List<String> wrongExprMethodList = new ArrayList<String>();

        @Override
        public void visit(MethodDeclaration n, Void v) {
            List<NameExpr> throwsList = n.getThrows();

            if (MMValueCheck.isEmpty(throwsList)) {
                this.noExprMethodList.add(n.getName());
            }
            else {
                boolean flag = false;
                for (NameExpr nameExpr : throwsList) {
                    if (nameExpr.getName().toString().contains("BusinessException")) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    this.wrongExprMethodList.add(n.getName());
                }
            }
        }

        public List<String> getNoExprMethodList() {
            return this.noExprMethodList;
        }

        public List<String> getWrongExprMethodList() {
            return this.wrongExprMethodList;
        }

    }
}
