package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.config;

import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * �����������������Ҫ��������ӡ����Ԥ����������Ȱ�ť���ӱ����ݴ�ӡ��ȫ����
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "��������������Ҫ��������ӡ����Ԥ����������Ȱ�ť���ӱ����ݴ�ӡ��ȫ����", solution = "�����ļ���nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad���actionList������Ӧ�ð�����Щ��ť", coder = "qiaoyanga", relatedIssueId = "203")
public class TestCase00203 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        return new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        StringBuffer noteBuilder = new StringBuffer();
        for (XmlResource xmlResource : resources) {
            List<Element> listLoadClasss =
                    xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager");
            if (listLoadClasss == null || listLoadClasss.size() == 0) {
                noteBuilder.append("δ֧��������");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }

            Element lazilyLoadManagerEle = listLoadClasss.get(0);

            Element lazilyLoadSupporterEle =
                    xmlResource.getChildPropertyElement(lazilyLoadManagerEle, "lazilyLoadSupporter");

            if (lazilyLoadSupporterEle == null) {
                noteBuilder.append("������������δ��������ӡ����Ԥ���������");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }

            List<Element> lists = xmlResource.getChildElementsByTagName(lazilyLoadSupporterEle, "list");
            if (lists == null || lists.size() == 0) {
                noteBuilder.append("������������δ��������ӡ����Ԥ���������");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }

            // ֻһ��List Element
            Element listEle = lists.get(0);

            // �ҵ����֧�ִ�ӡ��ť�����ص�Element
            Element lazyActionsEle = null;

            // һ����ֱ�� д<bean class=xxx/>,����һ����д <ref bean="xxid"/>
            List<Element> listChildOfBeans = xmlResource.getChildElementsByTagName(listEle, "bean");

            if (listChildOfBeans != null && listChildOfBeans.size() > 0) {
                for (Element beanEle : listChildOfBeans) {
                    if ("nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad".equals(beanEle.getAttribute("class"))) {
                        lazyActionsEle = beanEle;
                        break;
                    }
                }
            }

            if (lazyActionsEle == null) {
                // ref ��ʽ
                List<Element> listChildOfRefs = xmlResource.getChildElementsByTagName(listEle, "ref");

                if (listChildOfRefs != null && listChildOfRefs.size() > 0) {
                    for (Element refEle : listChildOfRefs) {
                        String refBeanId = refEle.getAttribute("bean");
                        // ���� <ref bean="lazyActions" />
                        if (refBeanId != null && (refBeanId.indexOf("Action") > 0 || refBeanId.indexOf("action") > 0)) {
                            lazyActionsEle = xmlResource.getElementById(refBeanId);
                            break;
                        }
                    }
                }

            }

            if (lazyActionsEle == null) {
                // ���ַ�ʽ��û���ҵ�,��û�����ô�ӡ��ť��ص�������֧��
                noteBuilder.append("������������δ����֧������ӡ����Ԥ���������");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }

            Element actionListPropertyEle = xmlResource.getChildPropertyElement(lazyActionsEle, "actionList");

            if (actionListPropertyEle == null) {
                noteBuilder.append("������������δ����֧������ӡ����Ԥ���������");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }
            List<Element> plist = xmlResource.getChildElementsByTagName(actionListPropertyEle, "list");

            if (plist == null || plist.size() == 0) {
                noteBuilder.append("������������δ����֧������ӡ����Ԥ���������");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }

            Element e = plist.get(0);

            List<Element> actions = xmlResource.getChildElementsByTagName(e, "ref");
            if (actions == null || actions.size() < 3) {
                noteBuilder.append("������������δ������ȫ.����ӡ����Ԥ���������");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }
            // if (noteBuilder.length() > 0) {
            // result.addResultElement(xmlResource.getResourcePath(),noteBuilder.toString());
            // }
        }
        return result;
    }

}
