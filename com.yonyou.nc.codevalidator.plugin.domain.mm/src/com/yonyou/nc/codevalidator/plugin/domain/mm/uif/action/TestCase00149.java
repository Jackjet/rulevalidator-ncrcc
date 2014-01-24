package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * 配置文件中actionsOfList和actionsOfCard以及单据模板表体中配置的按钮和按钮注册表中对应的按钮个数是否一致，以及按钮编码是否一致
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "配置文件中actionsOfList和actionsOfCard以及单据模板表体中配置的按钮和按钮注册表中对应的按钮个数和编码是否一致 ", relatedIssueId = "149", coder = "lijbe", solution = "配置文件中actionsOfList和actionsOfCard以及单据模板表体中配置的按钮和按钮注册表中对应的按钮个数和编码是否一致，按钮注册表中不能存在配置文件中没有的按钮编码. ")
public class TestCase00149 extends AbstractXmlRuleDefinition {

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

        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
            return result;
        }
        for (XmlResource xmlResource : resources) {
            /**
             * TODO
             * 查询出功能节点注册的按钮编码，找出配置文件中所有的Action，并且取到Action的code，同数据库中的比较是否一致.
             */
            Set<String> bdActionCodes = this.queryRegigsterActionCode(xmlResource.getFuncNodeCode(), ruleExecContext);
            Set<String> actionClazzs = this.getAllActionBeanId(xmlResource);
            Set<String> xmlActionCodes;
            xmlActionCodes = this.getActionCode(xmlResource, actionClazzs);

            Set<String> unExistActionCodes = this.getUnexistActionCodes(xmlActionCodes, bdActionCodes);

            if (!unExistActionCodes.isEmpty()) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(), String.format(
                        "功能编码为：%s 的节点中，按钮注册表中的按钮比配置文件多了：%s \n", xmlResource.getFuncNodeCode(), unExistActionCodes));
            }
        }

        return result;
    }

    /**
     * 检查功能节点注册的按钮编码是否都在xml中存在，并且编码是否一致，不一致返回xml中不存在的按钮编码
     * 
     * @param xmlActionCodes
     * @param dbActionCodes
     * @return
     */
    private Set<String> getUnexistActionCodes(Set<String> xmlActionCodes, Set<String> dbActionCodes) {

        Set<String> moreActionCodes = new HashSet<String>();

        for (String code : dbActionCodes) {
            // fs_upload,fs_download,fs_delete,fs_browse是手工写入数据库中的，配置文件中没有配置
            if (code.equals("fs_upload") || code.equals("fs_download") || code.equals("fs_delete")
                    || code.equals("fs_browse")) {
                continue;
            }
            if (!xmlActionCodes.contains(code)) {
                moreActionCodes.add(code);
            }
        }
        return moreActionCodes;
    }

    /**
     * 得到所有Action的Code
     * 
     * @param xmlResource
     * @param actionClazzs
     * @return
     * @throws RuleClassLoadException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("unchecked")
    private Set<String> getActionCode(XmlResource xmlResource, Set<String> actionClazzs) throws RuleBaseException {
        Set<String> actionCodes = new HashSet<String>();

        boolean isPreview = true;
        for (String actionClazz : actionClazzs) {
            List<Element> elements = xmlResource.getBeanElementByClass(actionClazz);
            // 同一个class被不同的bean使用
            for (Element element : elements) {
                String beanId = element.getAttribute("id");
                Element codeProperty = xmlResource.getChildPropertyElement(beanId, "code");
                String codeValue = null;
                if (null != codeProperty) {
                    codeValue = codeProperty.getAttribute("value");
                }
                if (MMValueCheck.isNotEmpty(codeValue)) {
                    actionCodes.add(codeValue);
                    continue;
                }
                IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();

                try {
                    Class<AbstractAction> clazz =
                            (Class<AbstractAction>) classLoaderUtils.loadClass(xmlResource.getBusinessComponent()
                                    .getProjectName(), actionClazz);
                    AbstractAction actionInstance = clazz.newInstance();
                    Method methodGetValue = clazz.getMethod("getValue", String.class);
                    codeValue = (String) methodGetValue.invoke(actionInstance, "Code");
                    if (MMValueCheck.isNotEmpty(codeValue)) {
                        actionCodes.add(codeValue);
                        continue;
                    }
                    // 预览不能直接从getValue中取到值，需要先设置一下它是预览，给Action设置Code
                    if (classLoaderUtils.isExtendsParentClass(xmlResource.getBusinessComponent().getProjectName(),
                            actionClazz, "nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction")) {
                        Method methodSetPreview = clazz.getMethod("setPreview", boolean.class);
                        methodSetPreview.invoke(actionInstance, isPreview);
                        if (isPreview) {
                            isPreview = false;
                        }
                        Method methodGetValue2 = clazz.getMethod("getValue", String.class);

                        codeValue = (String) methodGetValue2.invoke(actionInstance, "Code");
                        if (MMValueCheck.isNotEmpty(codeValue)) {
                            actionCodes.add(codeValue);
                        }

                    }
                }
                catch (NoSuchMethodException e) {
                    Logger.error(e.getMessage(), e);
                }
                catch (IllegalAccessException e) {
                    Logger.error(e.getMessage(), e);
                }
                catch (InvocationTargetException e) {
                    Logger.error(e.getMessage(), e);
                }
                catch (InstantiationException e) {
                    Logger.error(e.getMessage(), e);
                }
            }
        }

        return actionCodes;

    }

    /**
     * 从数据库中获取本节点注册的按钮
     * 
     * @param funcode
     * @return
     * @throws RuleBaseException
     */
    private Set<String> queryRegigsterActionCode(String funcode, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        Set<String> actionCodeList = new HashSet<String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select bu.btncode ");
        sb.append("from sm_butnregister bu ");
        sb.append("inner join sm_funcregister sf ");
        sb.append("on bu.parent_id=sf.cfunid ");
        sb.append("where sf.funcode='");
        sb.append(funcode);
        sb.append("' and (bu.dr = 0 or bu.dr is null ) and (sf.dr = 0 or sf.dr is null ) ");

        DataSet dataSet = SQLQueryExecuteUtils.executeQuery(sb.toString(), ruleExecContext.getRuntimeContext());
        if (!dataSet.isEmpty()) {
            for (DataRow dataRow : dataSet.getRows()) {
                actionCodeList.add((String) dataRow.getValue("btncode"));
            }
        }

        return actionCodeList;
    }

    /**
     * 返回配置文件下所有Action的beanId
     * 
     * @param xmlResource
     * @return
     * @throws RuleBaseException
     */
    private Set<String> getAllActionBeanId(XmlResource xmlResource) throws RuleBaseException {
        Set<String> actionBeanIds = new HashSet<String>();

        List<Element> cardActionElements = xmlResource.getCardActions();

        List<Element> listActionElements = xmlResource.getListActions();
        if (null != cardActionElements) {
            for (Element element : cardActionElements) {
                String actionBean = element.getAttribute("class");
                if (MMValueCheck.isNotEmpty(actionBean)) {
                    actionBeanIds.add(actionBean);
                }
            }
        }

        if (null != listActionElements) {
            for (Element element : listActionElements) {
                String actionBean = element.getAttribute("class");
                if (MMValueCheck.isNotEmpty(actionBean)) {
                    actionBeanIds.add(actionBean);
                }
            }
        }
        // 卡片表体行按钮
        List<Element> cardBodyActionElements = new ArrayList<Element>();
        this.dealActionExended(xmlResource, cardBodyActionElements);
        if (null != cardBodyActionElements) {
            for (Element element : cardBodyActionElements) {
                String actionBean = element.getAttribute("class");
                if (MMValueCheck.isNotEmpty(actionBean)) {
                    actionBeanIds.add(actionBean);
                }
            }
        }

        return actionBeanIds;

    }

    /**
     * 处理卡片表体肩按钮
     * 
     * @param document
     * @param idSet
     * @throws RuleBaseException
     * @throws RuleClassLoadException
     * @throws ResourceParserException
     * @throws BusinessException
     */
    private void dealActionExended(XmlResource xmlResource, List<Element> actionList) throws RuleBaseException {
        List<Element> billFormElementList = xmlResource.getAllBeanClass("nc.ui.pubapp.uif2app.view.ShowUpableBillForm");
        if (billFormElementList == null || billFormElementList.size() == 0) {
            return;
        }
        Element bodyLineElement = xmlResource.getChildPropertyElement(billFormElementList.get(0), "bodyLineActions");

        List<Element> subElements = null;
        List<Element> bodyActionElement = new ArrayList<Element>();
        // bodyLineActions
        if (null != bodyLineElement) {
            subElements = xmlResource.getChildElementsByTagName(bodyLineElement, "list");
            if (null == subElements || subElements.size() == 0) {
                return;
            }
            bodyActionElement = xmlResource.getChildElementsByTagName(subElements.get(0), "bean");
        }
        // BodyActionMap
        else {
            List<Element> tempBoydActionList = new ArrayList<Element>();
            bodyLineElement = xmlResource.getChildPropertyElement(billFormElementList.get(0), "BodyActionMap");
            subElements = xmlResource.getChildElementsByTagName(bodyLineElement, "map");
            if (null == subElements || subElements.size() == 0) {
                return;
            }
            subElements = xmlResource.getChildElementsByTagName(subElements.get(0), "entry");
            if (null == subElements || subElements.size() == 0) {
                return;
            }
            for (Element entryElement : subElements) {
                subElements = xmlResource.getChildElementsByTagName(entryElement, "list");
                if (null == subElements || subElements.size() == 0) {
                    return;
                }
                tempBoydActionList = xmlResource.getChildElementsByTagName(subElements.get(0), "bean");
                bodyActionElement.addAll(tempBoydActionList);
            }

        }
        IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();

        if (null != bodyActionElement) {
            for (Element element : bodyActionElement) {

                String clazzName = element.getAttribute("class");
                //
                // 得到卡片表体肩按钮，去除分割按钮nc.ui.pub.beans.ActionsBar$ActionsBarSeparator
                if (classLoaderUtils.isExtendsParentClass(xmlResource.getBusinessComponent().getProjectName(),
                        clazzName, "javax.swing.AbstractAction")
                        && !classLoaderUtils.isExtendsParentClass(xmlResource.getBusinessComponent().getProjectName(),
                                clazzName, "nc.ui.pub.beans.ActionsBar$ActionsBarSeparator")) {
                    actionList.add(element);
                }
            }
        }

    }

}
