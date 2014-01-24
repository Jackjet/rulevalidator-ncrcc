package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.MmUIFactoryConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;

/**
 * xml解析工具类
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MmXmlAnalysisUtil {
    private static String[] EVENT_LIST = new String[] {
        "nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent",
        "nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent",
        "nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent",
        "nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent"
    };

    /**
     * 获得xml中事件容器的所有EventHandlerGroup
     * 
     * @param xmlResource
     * @param result
     * @return
     */
    public static List<Element> getEventHandlerElementList(XmlResource xmlResource, ResourceRuleExecuteResult result) {
        List<Element> eventElementList = xmlResource.getBeanElementByClass(MmUIFactoryConstants.EVENT_LISTENER_CALSS);
        if (eventElementList == null || eventElementList.size() == 0) {
            result.addResultElement(xmlResource.getResourcePath(), "找不到事件容器:"
                    + MmUIFactoryConstants.EVENT_LISTENER_CALSS + "\n");
            return null;
        }

        Element eventMediatorElement = null;
        if (eventElementList.size() > 1) {
            // 当有多个事件容器的时候. 可能有别的对话框也支持事件配置.
            // 但是主界面的世界配置类,ref对应的model应该是标准的manageAppModel
            for (Element element : eventElementList) {
                Element modelEle = xmlResource.getChildPropertyElement(element, "model");
                // ref对应的model应该是标准的manageAppModel
                if ("manageAppModel".equals(modelEle.getAttribute("ref"))) {
                    eventMediatorElement = element;
                    break;
                }
            }

            if (eventMediatorElement == null) {
                // 此种情况是由于 主事件容器配置的model, id 不是标准的命名manageAppModel
                eventMediatorElement = eventElementList.get(0);
            }
        }
        else {
            eventMediatorElement = eventElementList.get(0);
        }
        // 获得事件容器的handlerGroup
        Element handlerGroupElement = xmlResource.getChildPropertyElement(eventMediatorElement, "handlerGroup");
        if (handlerGroupElement == null) {
            result.addResultElement(xmlResource.getResourcePath(), "找不到事件容器的handlerGroup \n");
            return null;
        }
        // 获得事件容器的handlerGroup的list
        List<Element> handlerGroupList = xmlResource.getChildElementsByTagName(handlerGroupElement, "list");
        if (handlerGroupList == null || handlerGroupList.size() == 0) {
            result.addResultElement(xmlResource.getResourcePath(), "找不到事件容器的handlerGroup的list \n");
            return null;
        }
        Element listElement = handlerGroupList.get(0);
        // 获得所有事件Bean
        List<Element> beanList = xmlResource.getChildElementsByTagName(listElement, "bean");
        if (beanList == null || beanList.size() == 0) {
            result.addResultElement(xmlResource.getResourcePath(), "事件容器的handlerGroup的list是空的 \n");
            return null;
        }
        return beanList;
    }

    /**
     * 找到Xml文件中事件类型对应的处理类
     * 
     * @param xmlResource
     * @return
     */
    public static MapList<String, String> getEventHandlerClass(XmlResource xmlResource, String[] eventList) {
        if (MMValueCheck.isEmpty(eventList)) {
            eventList = MmXmlAnalysisUtil.EVENT_LIST;
        }

        MapList<String, String> handlerMapList = new MapList<String, String>();

        List<Element> mediatorElementList =
                xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.event.EventHandlerGroup");

        for (Element element : mediatorElementList) {
            String tmpKey = "";
            String tmpClass = "";
            String refBeanId = "";
            NodeList nodeList = element.getElementsByTagName("property");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NodeList tmpNodeList = node.getChildNodes();
                if (tmpNodeList != null) {

                    for (int k = 0; k < tmpNodeList.getLength(); k++) {
                        Node tmpNode = tmpNodeList.item(k);
                        NamedNodeMap tmpNodeMap = tmpNode.getAttributes();
                        if (null == tmpNodeMap) {
                            continue;
                        }
                        for (int h = 0; h < tmpNodeMap.getLength(); h++) {
                            Node n = tmpNodeMap.item(h);
                            String nodeName = n.getNodeName();
                            String nodeValue = n.getNodeValue();

                            if ("class".equals(nodeName)) {
                                tmpClass = nodeValue;
                                break;
                            }
                            if ("bean".equals(nodeName)) {
                                refBeanId = nodeValue;
                            }
                        }
                    }
                }
                NamedNodeMap nodeMap = node.getAttributes();
                if (nodeMap != null) {
                    for (int k = 0; k < nodeMap.getLength(); k++) {
                        Node tmpNode = nodeMap.item(k);
                        // String nodeName = tmpNode.getNodeName();
                        String nodeValue = tmpNode.getNodeValue();
                        if (Arrays.asList(eventList).contains(nodeValue)) {
                            tmpKey = nodeValue;
                        }
                    }
                }
            }
            if (MMValueCheck.isNotEmpty(tmpKey) && MMValueCheck.isNotEmpty(tmpClass)) {
                handlerMapList.put(tmpKey, tmpClass);
                continue;
            }
            if (MMValueCheck.isNotEmpty(refBeanId) && MMValueCheck.isNotEmpty(tmpKey)) {
                Element e = xmlResource.getElementById(refBeanId);
                tmpClass = e.getAttribute("class");
                handlerMapList.put(tmpKey, tmpClass);
            }
        }

        return handlerMapList;

    }

}
