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
 * 业务单位的编辑前事件中必须根据当前物料进行参照过滤 1,先按照关键字"*unitHandler.java",
 * "*UnitHandler.java"取单位处理类,如果没有,则根据元数据以及计量单位的DataType常量
 * ,从所有的编辑前事件(表头+表体)中,运用本领域的getHandler(String
 * key)方式找到计量单位处理类;2,分析该事件类使用运用了本领域的辅助了
 * "nc.ui.mmf.busi.measure.handler.AssMeasureHandler"以及调用了方法".beforeEdit"
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "业务单位的编辑前事件中必须根据当前物料进行参照过滤 ", relatedIssueId = "227", coder = "lijbe", solution = "1.通过编辑前事件类型找到事件监听处理类;2.根据关键字符*UnitHandler或者*unitHandler【单位编辑事件命名以其结尾】找到编辑事件处理类;"
        + "2.分析beforeEdit中是否调用了【nc.ui.mmf.busi.measure.handler.AssMeasureHandler.beforeEdit方法】")
public class TestCase00227 extends AbstractXmlRuleDefinition {

    private String[] eventList = new String[] {
        "nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent",
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
         * 通过class：nc.ui.pubapp.uif2app.model.AppEventHandlerMediator找到所有的事件处理处理类
         * 。 再通过事件处理类找到具体的事件处理类. 然后判断其是否符合本领域事件的处理规范.
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
     * 检查事件初始化类，是否满足:public class XXBodyBeforeEditHandler extends MMEventHandler
     * implements IAppEventHandler<CardBodyBeforeEditEvent> 这种实现形式
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
             * 如果监听类符合标准，就查看具体的事件处理 类
             */
            if (MMValueCheck.isEmpty(visitor.concreteHandlerList)) {
                noteBuilder.append(String.format("【%s】中没有找到以UnitHandler或unitHandler结尾的编辑事件处理类.\n",
                        javaClassResource.getJavaCodeClassName()));
                result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
                return;
            }
            // 检查具体事件的处理类
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
            if (!visitor.isRef) {
                noteBuilder
                        .append(String
                                .format("业务单位编辑事件处理类【%s】编辑前事件中没有根据物料过滤单位,即没有调用【nc.ui.mmf.busi.measure.handler.AssMeasureHandler.beforeEdit方法】进行处理.\n",
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
     * 根据className查询资源文件
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
        noteBuilder.append(String.format("事件监听初始化类【%s】没有使用本领域的事件处理框架.\n", javaClassResource.getJavaCodeClassName()));
    }

    /**
     * 扫描事件监听处理类是否正确，判断依据是写法如下: public class XXBodyBeforeEditHandler extends
     * MMEventHandler implements IAppEventHandler<CardBodyBeforeEditEvent>,
     * 并且：handleAppEvent不能为空
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private class EventHandlerVisitorAdapter extends VoidVisitorAdapter<Void> {

        /**
         * 判断事件的监听类是否是正确
         */
        boolean isRight = false;

        /**
         * 存放具体的处理事件
         */
        List<String> concreteHandlerList = new ArrayList<String>();

        @Override
        public void visit(ImportDeclaration n, Void arg) {

            String importClazz = n.getName().toString();
            // 可以将引用个类都加进来因为多页签比较特殊
            if (MMValueCheck.isNotEmpty(importClazz)) {
                if (importClazz.endsWith("UnitHandler") || importClazz.endsWith("unitHandler")) {
                    this.concreteHandlerList.add(importClazz);
                }
            }
            super.visit(n, arg);
        }

        /**
         * his否还需要考虑在继承类中使用全路径的情况
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
     * 扫描事件监听处理类是否正确，判断依据是写法如下:public class BomBodyItemAstUnitHandler extends
     * MMBaseHandler
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private class ConcreteEventHandlerVisitorAdapter extends VoidVisitorAdapter<Void> {

        /**
         * 是否引入
         */
        boolean isImport = false;

        /**
         * 是否支持多选行
         */
        boolean isRef = false;

        /**
         * 判断是否引入了MaterialvidHandler类
         */
        @Override
        public void visit(ImportDeclaration n, Void arg) {
            if (this.isImport) {
                return;
            }
            if ("nc.ui.mmf.busi.measure.handler.AssMeasureHandler".equals(n.getName().toString())) {
                this.isImport = true;
            }

            super.visit(n, arg);
        }

        /**
         * 判断handleAppEvent方法是否为空,以及加载initMap中的方法
         */
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            if (!this.isImport) {
                return;
            }
            if (this.isRef) {
                return;
            }

            /**
             * 去除表体编辑前事件
             */
            if ("afterEdit".equals(n.getName())) {
                return;
            }
            BlockStmt bodyStmt = n.getBody();
            if (bodyStmt == null) {
                return;
            }
            List<Statement> stmts = bodyStmt.getStmts();
            /*
             * MaterialvidHandler handler = new MaterialvidHandler(this.param);
             * handler.afterEdit(e);
             */
            String variable = "";
            boolean hasRef = false;

            for (Statement stmt : stmts) {
                String stmtStr = stmt.toString().trim();
                stmtStr = stmtStr.replaceAll(" {2,}", " ");// 去除多余的空格
                if (stmtStr.contains("new AssMeasureHandler") && !hasRef) {
                    String[] mhs = stmtStr.split(" ");
                    variable = mhs[1];
                    hasRef = true;
                }
                if (stmtStr.contains(variable + ".beforeEdit") && hasRef) {
                    this.isRef = true;
                }

            }

        }

    }
}
