package com.yonyou.nc.codevalidator.plugin.domain.mm.code.query;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
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
 * 查询时集团数据隔离
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "点击查询按钮查询时仅能查询出当前集团的数据 ", relatedIssueId = "293", coder = "lijbe", solution = "点击查询按钮查询时仅能查询出当前集团的数据   ，"
        + "即查询时，需要使用appendCurrentGroup方法限制只能查询当前集团的数据.【注意】查询实现类以QueryImpl结尾")
public class TestCase00293 extends AbstractRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {

        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        UpmResourceQuery upmResourceQuery = new UpmResourceQuery();
        upmResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
        for (UpmResource upmResource : upmResources) {
            UpmModuleVO upmModuleVo = upmResource.getUpmModuleVo();
            List<String> queryImplClassList = this.getQueryImplClassName(upmModuleVo);
            if (MMValueCheck.isEmpty(queryImplClassList)) {
                result.addResultElement(upmResource.getResourcePath(), "没有找到以QueryImpl结尾的查询实现类，请检查命名是否规范 \n");
                continue;
            }

            JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
            javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
            javaResourceQuery.setResPrivilege(JavaResPrivilege.PRIVATE);
            javaResourceQuery.setClassNameFilterList(queryImplClassList);
            List<JavaClassResource> javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);

            for (final JavaClassResource javaClassResource : javaResourceList) {

                try {
                    CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
                    QueryImplVisitorAdapter visitor = new QueryImplVisitorAdapter();
                    visitor.visit(compilationUnit, null);
                    // 是否引用了懒加载类
                    boolean isImport = visitor.isImport();
                    // 是否正确使用了懒加载
                    boolean isFlag = visitor.isFlag();
                    // 是否需要懒加载
                    boolean isGroup = visitor.isGroup();
                    if (!isGroup) {
                        continue;
                    }
                    if (!isImport || !isFlag) {
                        result.addResultElement(javaClassResource.getJavaCodeClassName(),
                                "查询时，只能查询出当前集团的数据，即需要使用【appendCurrentGroup】方法限制只能查询出当前集团的数据. \n");
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
     * 得到*QueryImpl的类名
     */
    private List<String> getQueryImplClassName(UpmModuleVO upmModuleVO) {
        List<String> queryImplClassList = new ArrayList<String>();
        List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
        if (pubComponentVoList != null && pubComponentVoList.size() > 0) {
            String queryImpl = null;
            for (UpmComponentVO upmComponentVO : pubComponentVoList) {
                queryImpl = upmComponentVO.getImplementationName();
                if (queryImpl != null && queryImpl.endsWith("QueryImpl")) {
                    queryImplClassList.add(queryImpl);
                }
            }
        }
        List<UpmComponentVO> priComponentVoList = upmModuleVO.getPriComponentVoList();
        if (priComponentVoList != null && priComponentVoList.size() > 0) {
            String queryImpl = null;
            for (UpmComponentVO upmComponentVO : priComponentVoList) {
                queryImpl = upmComponentVO.getImplementationName();
                if (queryImpl != null && queryImpl.endsWith("QueryImpl")) {
                    queryImplClassList.add(queryImpl);
                }
            }
        }
        return queryImplClassList;
    }

    /**
     * 扫面引用类和方法
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class QueryImplVisitorAdapter extends VoidVisitorAdapter<Void> {
        boolean isImport = false;

        boolean flag = false;

        boolean isGroup = false;

        @Override
        public void visit(ImportDeclaration n, Void arg) {
            // 已经找到就不需要再往下执行
            if (this.isImport) {
                return;
            }
            String importClazz = n.getName().toString();
            if ("nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor".equals(importClazz)) {
                this.isImport = true;

                return;
            }
        }

        @Override
        public void visit(MethodDeclaration n, Void v) {

            // 没有引用懒加载类
            if (!this.isImport) {
                return;
            }

            List<Parameter> params = n.getParameters();
            boolean isContain = params.toString().contains("IQueryScheme");
            if (!isContain) {
                return;
            }
            //
            if (!this.isGroup) {
                this.isGroup = true;
            }

            // 得到方法体，即{}中的内容
            BlockStmt body = n.getBody();
            if (body == null) {
                return;
            }
            // 得到方法体内的语句块
            List<Statement> stmts = body.getStmts();
            if (stmts == null) {
                return;
            }

            for (Statement stmt : stmts) {
                if (stmt.toString().contains("appendCurrentGroup")) {
                    this.flag = true;
                    return;
                }
            }

        }

        public boolean isImport() {
            return this.isImport;
        }

        public boolean isFlag() {
            return this.flag;
        }

        public boolean isGroup() {
            return this.isGroup;
        }

    }
}
