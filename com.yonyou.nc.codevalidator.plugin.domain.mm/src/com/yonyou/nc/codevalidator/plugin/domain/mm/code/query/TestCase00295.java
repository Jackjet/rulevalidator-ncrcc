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
 * ��ѯʱ��֧�������أ�
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "�����ѯ��ť��ѯʱ��Ҫ֧�������� ������ѯʹ��nc.impl.pubapp.pattern.data.bill.BillLazyQuery", relatedIssueId = "295", coder = "lijbe", solution = "�����ѯ��ť��ѯʱ��Ҫ֧�������� ��"
        + "����ѯʱ������ʹ��nc.impl.pubapp.pattern.data.bill.BillLazyQuery�࣬������ƽ̨���������ܵĲ�ѯʹ��nc.itf.pubapp.uif2app.components.grand.IGrandAggVosQueryService��query����.��ע�⡿��ѯʵ������QueryImpl��β")
public class TestCase00295 extends AbstractRuleDefinition {

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
                result.addResultElement(upmResource.getResourcePath(), "û���ҵ���QueryImpl��β�Ĳ�ѯʵ���࣬���������Ƿ�淶 \n");
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
                    // �Ƿ���������������
                    boolean isImport = visitor.isImport();
                    // �Ƿ���ȷʹ����������
                    boolean isFlag = visitor.isFlag();
                    // �Ƿ���Ҫ������
                    boolean isLazy = visitor.isLazy();
                    if (!isLazy) {
                        continue;
                    }
                    if (!isImport || !isFlag) {
                        result.addResultElement(
                                javaClassResource.getJavaCodeClassName(),
                                "��ѯû��ʹ���������ࡾnc.impl.pubapp.pattern.data.bill.BillLazyQuery��;�������������,����Ҫʹ�á�nc.itf.pubapp.uif2app.components.grand.IGrandAggVosQueryService�� \n");
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
     * �õ�*QueryImpl������
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
     * ɨmiao������ͷ���
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class QueryImplVisitorAdapter extends VoidVisitorAdapter<Void> {
        boolean isImport = false;

        boolean flag = false;

        boolean isLazy = false;

        @Override
        public void visit(ImportDeclaration n, Void arg) {
            // �Ѿ��ҵ��Ͳ���Ҫ������ִ��
            if (this.isImport) {
                return;
            }
            String importClazz = n.getName().toString();
            if ("nc.impl.pubapp.pattern.data.bill.BillLazyQuery".equals(importClazz)
                    || "nc.itf.pubapp.uif2app.components.grand.IGrandAggVosQueryService".equals(importClazz)) {
                this.isImport = true;

                return;
            }
        }

        @Override
        public void visit(MethodDeclaration n, Void v) {

            // û��������������
            if (!this.isImport) {
                return;
            }

            List<Parameter> params = n.getParameters();
            boolean isContain = params.toString().contains("IQueryScheme");
            if (!isContain) {
                return;
            }
            // �ж��Ƿ���Ҫʹ�������أ����û��ʹ�ò�ѯ������ѯ����Ĭ��Ϊ����Ҫ������
            if (!this.isLazy) {
                this.isLazy = true;
            }

            // �õ������壬��{}�е�����
            BlockStmt body = n.getBody();
            if (body == null) {
                return;
            }
            // �õ��������ڵ�����
            List<Statement> stmts = body.getStmts();
            if (stmts == null) {
                return;
            }

            for (Statement stmt : stmts) {
                if (stmt.toString().contains("BillLazyQuery") || stmt.toString().contains("IGrandAggVosQueryService")) {
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

        public boolean isLazy() {
            return this.isLazy;
        }

    }
}
