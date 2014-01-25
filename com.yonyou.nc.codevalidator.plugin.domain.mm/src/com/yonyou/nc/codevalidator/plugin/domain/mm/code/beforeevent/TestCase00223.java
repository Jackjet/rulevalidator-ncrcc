package com.yonyou.nc.codevalidator.plugin.domain.mm.code.beforeevent;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util.MmXmlAnalysisUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMMapUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
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
 * �������ϵı༭ǰ�¼��б���֧�ֲ��ն�ѡ
 * 1,�ȸ��ݱ�׼�ؼ���*MaterialHandler.javaȡ�¼���,���ȡ����,��������ϰ汾��Ԫ����DataType
 * ,��Ԫ����ȡ�����ϰ汾�ֶ�,Ȼ��ȡ�����еı༭ǰ�¼�,Ӧ�ñ�����Ļ���,ͨ�����ֶ����ҵ���Ӧ�Ĵ�����;2,�������¼����Ƿ�����
 * "nc.ui.mmf.busi.measure.handler.MaterialvidHandler",
 * ".setMultiSelectedEnabled(true)".���������,����ȷ
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "�����У����ϵı༭ǰ�¼�����֧�ֶ�ѡ����", relatedIssueId = "223", coder = "lijbe", solution = "1.ͨ���¼������ҵ��¼�����������;2.���ݹؼ��ַ�*MaterialHandler����*materialHandler�����ϱ༭�¼����������β���ҵ��༭ǰ�¼�������;"
        + "2.����beforeEdit���Ƿ�����˶�ѡ���յ����á���setMultiSelectedEnabled(true)��")
public class TestCase00223 extends AbstractXmlRuleDefinition {

    private String[] eventList = new String[] {
        "nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent"
    };

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResQry;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        /*
         * ͨ��class��nc.ui.pubapp.uif2app.model.AppEventHandlerMediator�ҵ����е��¼���������
         * �� ��ͨ���¼��������ҵ�������¼�������. Ȼ���ж����Ƿ���ϱ������¼��Ĵ���淶.
         */

        for (XmlResource xmlResource : resources) {
            MapList<String, String> handlerClazzList =
                    MmXmlAnalysisUtil.getEventHandlerClass(xmlResource, this.eventList);
            if (MMMapUtil.isEmpty(handlerClazzList)) {
                continue;
            }
            Collection<List<String>> collection = handlerClazzList.toMap().values();
            List<String> handlerList = new ArrayList<String>();
            Iterator<List<String>> itr = collection.iterator();
            while (itr.hasNext()) {
                handlerList.addAll(itr.next());
            }
            this.checkEventHandler(ruleExecContext, result, handlerList);
        }

        return result;
    }

    private void checkEventHandler(IRuleExecuteContext ruleExecContext, ResourceRuleExecuteResult result,
            List<String> clazzList) throws RuleBaseException {
        List<JavaClassResource> javResList = this.getJavaResources(ruleExecContext, clazzList);
        for (JavaClassResource javaClassResource : javResList) {
            this.checkHandler(ruleExecContext, result, javaClassResource);
        }
    }

    /**
     * ����¼���ʼ���࣬�Ƿ�����:public class XXBodyBeforeEditHandler extends MMEventHandler
     * implements IAppEventHandler<CardBodyBeforeEditEvent> ����ʵ����ʽ
     * 
     * @param javaClassResource
     * @throws RuleBaseException
     */
    private void checkHandler(IRuleExecuteContext ruleExecContext, ResourceRuleExecuteResult result,
            JavaClassResource javaClassResource) throws RuleBaseException {
        try {
            EventHandlerVisitorAdapter visitor = new EventHandlerVisitorAdapter();
            CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
            visitor.visit(compilationUnit, null);
            StringBuilder noteBuilder = new StringBuilder();
            if (!visitor.isRight) {
                this.appendMsg(noteBuilder, javaClassResource);
                if (noteBuilder.length() > 0) {
                    result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
                }
                return;
            }
            /*
             * �����������ϱ�׼���Ͳ鿴������¼����� ��
             */
            if (MMValueCheck.isEmpty(visitor.concreteHandlerList)) {
                noteBuilder
                        .append(String
                                .format("��%s����û���ҵ���MaterialHandler��materialHandler��MaterialvidHandler��materialvidHandler��β�����ϱ༭�¼�������.\n",
                                        javaClassResource.getJavaCodeClassName()));
                result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
                return;
            }
            // �������¼��Ĵ�����
            this.checkConcreteHandler(ruleExecContext, visitor.concreteHandlerList);

        }
        catch (ParseException e) {
            Logger.error(e.getMessage(), e);
        }
        catch (IOException e) {
            Logger.error(e.getMessage(), e);
        }

    }

    private void checkConcreteHandler(IRuleExecuteContext ruleExecContext, List<String> classList)
            throws RuleBaseException {
        List<JavaClassResource> javResList = this.getJavaResources(ruleExecContext, classList);
        for (JavaClassResource javaClassResource : javResList) {
            this.checkConcreteHandler(javaClassResource);
        }

    }

    private void checkConcreteHandler(JavaClassResource javaClassResource) {
        ConcreteEventHandlerVisitorAdapter visitor = new ConcreteEventHandlerVisitorAdapter();
        try {
            StringBuilder noteBuilder = new StringBuilder();
            CompilationUnit

            compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
            visitor.visit(compilationUnit, null);
            if (!visitor.isRight) {
                noteBuilder.append(String.format(
                        "���ϱ༭ǰ�¼������ࡾ%s��û�����ö�ѡ���յ�����,��û��UIRefPane��setMultiSelectedEnabled����û������Ϊtrue.\n",
                        javaClassResource.getJavaCodeClassName()));
            }
        }
        catch (ParseException e) {
            Logger.error(e.getMessage(), e);
        }
        catch (IOException e) {
            Logger.error(e.getMessage(), e);
        }

    }

    /**
     * ����className��ѯ��Դ�ļ�
     * 
     * @param ruleExecContext
     * @param filterClazzs
     * @return
     * @throws RuleBaseException
     */
    private List<JavaClassResource> getJavaResources(IRuleExecuteContext ruleExecContext, List<String> filterClazzs)
            throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
        javaResourceQuery.setClassNameFilterList(filterClazzs);
        List<JavaClassResource> javaResourceList = new ArrayList<JavaClassResource>();
        javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);

        return javaResourceList;
    }

    private void appendMsg(StringBuilder noteBuilder, JavaClassResource javaClassResource) {
        noteBuilder.append(String.format("�¼�������ʼ���ࡾ%s��û��ʹ�ñ�������¼�������.\n", javaClassResource.getJavaCodeClassName()));
    }

    /**
     * ɨ���¼������������Ƿ���ȷ���ж�������д������: public class XXBodyBeforeEditHandler extends
     * MMEventHandler implements IAppEventHandler<CardBodyBeforeEditEvent>,
     * ���ң�handleAppEvent����Ϊ��
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class EventHandlerVisitorAdapter extends VoidVisitorAdapter<Void> {

        /**
         * �ж��¼��ļ������Ƿ�����ȷ
         */
        boolean isRight = false;

        /**
         * ��ž���Ĵ����¼�
         */
        List<String> concreteHandlerList = new ArrayList<String>();

        // /**
        // * ��ž����¼�������:key:ֻ��������value:����ȫ·��������
        // */
        // Map<String, String> importClassMap = new HashMap<String, String>();
        //
        // /**
        // * �ж�handleAppEvent�����Ƿ�Ϊ��,�Լ�����initMap�еķ���
        // */
        // @Override
        // public void visit(MethodDeclaration n, Void arg) {
        // if (!this.isRight) {
        // return;
        // }
        // if (!"initMap".equals(n.getName())) {
        // return;
        // }
        // BlockStmt bodyStmt = n.getBody();
        // if (bodyStmt == null) {
        // return;
        // }
        // List<Statement> stmts = bodyStmt.getStmts();
        // /*
        // * this.putHandler(BomItemVO.CASSMEASUREID + BomTableCode.BOMITEMS,
        // * BomBodyItemAstUnitHandler.class);
        // */
        // for (Statement stmt : stmts) {
        // String stmtStr = stmt.toString();
        // String tmpClass = stmtStr.substring(stmtStr.indexOf(",") + 1,
        // stmtStr.indexOf(".class")).trim();
        // String importClazz = this.importClassMap.get(tmpClass);
        // if (MMValueCheck.isNotEmpty(tmpClass)
        // && MMValueCheck.isNotEmpty(importClazz)) {
        // if (importClazz.endsWith("MaterialHandler")
        // || importClazz.endsWith("materialHandler")
        // || importClazz.endsWith("MaterialvidHandler")
        // || importClazz.endsWith("materialvidHandler")) {
        // this.concreteHandlerList.add(importClazz);
        // }
        //
        // }
        // }
        //
        // }

        @Override
        public void visit(ImportDeclaration n, Void arg) {
            String importClazz = n.getName().toString();
            // ���Խ����ø��඼�ӽ�����Ϊ��ҳǩ�Ƚ�����
            if (MMValueCheck.isNotEmpty(importClazz) && MMValueCheck.isNotEmpty(importClazz)) {
                if (importClazz.endsWith("MaterialHandler") || importClazz.endsWith("materialHandler")
                        || importClazz.endsWith("MaterialvidHandler") || importClazz.endsWith("materialvidHandler")) {
                    this.concreteHandlerList.add(importClazz);
                }
            }
            super.visit(n, arg);
        }

        /**
         * his����Ҫ�����ڼ̳�����ʹ��ȫ·�������
         */
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {

            String parentClazz = "";
            String implItf = "";
            if (!MMValueCheck.isEmpty(n.getExtends())) {
                parentClazz = n.getExtends().toString();
            }
            if (!MMValueCheck.isEmpty(n.getImplements())) {
                implItf = n.getImplements().toString();
            }
            if (parentClazz.contains("MMEventHandler") && implItf.contains("IAppEventHandler")) {
                this.isRight = true;
            }
            super.visit(n, arg);
        }

    }

    /**
     * ɨ���¼������������Ƿ���ȷ���ж�������д������:public class BomBodyItemAstUnitHandler extends
     * MMBaseHandler
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class ConcreteEventHandlerVisitorAdapter extends VoidVisitorAdapter<Void> {

        /**
         * �ж��¼��ļ������Ƿ�����ȷ
         */
        boolean isRight = false;

        /**
         * his����Ҫ�����ڼ̳�����ʹ��ȫ·�������
         */
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {

            String parentClazz = "";

            if (!MMValueCheck.isEmpty(n.getExtends())) {
                parentClazz = n.getExtends().toString();
            }
            if (parentClazz.contains("MMBaseHandler")) {
                this.isRight = true;
            }
            super.visit(n, arg);
        }

        /**
         * �ж�handleAppEvent�����Ƿ�Ϊ��,�Լ�����initMap�еķ���
         */
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            if (!this.isRight) {
                return;
            }
            if (!"afterEdit".equals(n.getName())) {
                return;
            }
            BlockStmt bodyStmt = n.getBody();
            if (bodyStmt == null) {
                return;
            }
            List<Statement> stmts = bodyStmt.getStmts();
            /*
             * this.putHandler(BomItemVO.CASSMEASUREID + BomTableCode.BOMITEMS,
             * BomBodyItemAstUnitHandler.class);
             */
            for (Statement stmt : stmts) {
                String stmtStr = stmt.toString();
                if (stmtStr.contains(".setMultiSelectedEnabled(true)")) {
                    this.isRight = true;
                }
            }

        }

    }
}
