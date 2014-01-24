package ncmdp.serialize;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import ncmdp.exception.ValidateException;
import ncmdp.model.JGraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * JGrahp���л�����
 * @author wangxmn
 *
 */
public class JGraphSerializeTool {
	private static void validateJGraph(JGraph graph, Document doc)
			throws ValidateException, Exception {
		// У��������ϵ�Ƿ񶼴���
		XPath path = XPathFactory.newInstance().newXPath();
		String str = "/component/refdependLoseIDs/losedid/@id";
		NodeList list = (NodeList) path.evaluate(str, doc,
				XPathConstants.NODESET);
		if (list.getLength() > 0) {
			StringBuilder sb = new StringBuilder("����id����Ӧ��ʵ�嶪ʧ,���ܱ��棺\r\n");
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				String id = node.getNodeValue();
				sb.append(id).append("\r\n");
			}
			throw new ValidateException(sb.toString());
		}

	}

	public static void saveToFile(JGraph graph, File file) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element root = graph.createElement(doc);
			doc.appendChild(root);
			validateJGraph(graph, doc);
//			String industry = graph.getIndustry().getCode();
			/** ��������ʵ�塢�ӿڡ�ö�ٵ�file��ӳ�� dingxm@2009-09-03 */
			XPath xpath = XPathFactory.newInstance().newXPath();
			/* ʵ�� */
			NodeList entityList = (NodeList) xpath.evaluate(
					"/component/celllist/entity", doc, XPathConstants.NODESET);
			for (int k = 0; k < entityList.getLength(); k++) {
				Node node = entityList.item(k);
				NamedNodeMap map = node.getAttributes();
				if (map.getNamedItem("id") != null) {
//					String tempEntityID = map.getNamedItem("id").getNodeValue();
//					MDPCachePool
//							.getInstance()
//							.getEntityIDMap()
//							.put(tempEntityID, industry, file.getAbsolutePath());
				}
			}
			/* ҵ��ӿ� */
			NodeList itfList = (NodeList) xpath.evaluate(
					"/component/celllist/busiIterface", doc,
					XPathConstants.NODESET);
			for (int k = 0; k < itfList.getLength(); k++) {
				Node node = itfList.item(k);
				NamedNodeMap map = node.getAttributes();
				if (map.getNamedItem("id") != null) {
//					String tempEntityID = map.getNamedItem("id").getNodeValue();
//					MDPCachePool
//							.getInstance()
//							.getEntityIDMap()
//							.put(tempEntityID, industry, file.getAbsolutePath());
				}
			}
			/* ö�� */
			NodeList enumList = (NodeList) xpath.evaluate(
					"/component/celllist/Enumerate", doc,
					XPathConstants.NODESET);
			for (int k = 0; k < enumList.getLength(); k++) {
				Node node = enumList.item(k);
				NamedNodeMap map = node.getAttributes();
				if (map.getNamedItem("id") != null) {
//					String tempEntityID = map.getNamedItem("id").getNodeValue();
//					MDPCachePool
//							.getInstance()
//							.getEntityIDMap()
//							.put(tempEntityID, industry, file.getAbsolutePath());
				}
			}
			/* �����ӿ� */
			NodeList opItfList = (NodeList) xpath.evaluate(
					"/component/celllist/opinterface", doc,
					XPathConstants.NODESET);
			for (int k = 0; k < opItfList.getLength(); k++) {
				Node node = opItfList.item(k);
				NamedNodeMap map = node.getAttributes();
				if (map.getNamedItem("id") != null) {
//					String tempEntityID = map.getNamedItem("id").getNodeValue();
//					MDPCachePool
//							.getInstance()
//							.getEntityIDMap()
//							.put(tempEntityID, industry, file.getAbsolutePath());
				}
			}
			/* ҵ����� */
			NodeList busiOperationList = (NodeList) xpath.evaluate(
					"/component/celllist/busioperation", doc,
					XPathConstants.NODESET);
			for (int k = 0; k < busiOperationList.getLength(); k++) {
				Node node = busiOperationList.item(k);
				NamedNodeMap map = node.getAttributes();
				if (map.getNamedItem("id") != null) {
//					String tempEntityID = map.getNamedItem("id").getNodeValue();
//					MDPCachePool
//							.getInstance()
//							.getEntityIDMap()
//							.put(tempEntityID, industry, file.getAbsolutePath());
				}
			}
			/* ҵ�� */
			NodeList busiActivityList = (NodeList) xpath.evaluate(
					"/component/celllist/busiActivity", doc,
					XPathConstants.NODESET);
			for (int k = 0; k < busiActivityList.getLength(); k++) {
				Node node = busiActivityList.item(k);
				NamedNodeMap map = node.getAttributes();
				if (map.getNamedItem("id") != null) {
//					String tempEntityID = map.getNamedItem("id").getNodeValue();
//					MDPCachePool
//							.getInstance()
//							.getEntityIDMap()
//							.put(tempEntityID, industry, file.getAbsolutePath());
				}
			}
			/** end */
			DomTreeToXML.domTreeToXML(doc, file);
		} catch (ValidateException e) {
			throw e;
		} catch (Exception e) {
//			MessageDialog.openError(Display.getCurrent().getActiveShell(),
//					"����", NCMDPTool.getExceptionRecursionError(e));
//			e.printStackTrace();
		} finally {
		}
	}

	public static String serializeToString(JGraph graph) {
		String text = "";
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element root = graph.createElement(doc);
			doc.appendChild(root);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DomTreeToXML.domTreeToStream(doc, baos);
			text = baos.toString("UTF-8");
		} catch (Exception e) {
//			MessageDialog.openError(Display.getCurrent().getActiveShell(),
//					"����", NCMDPTool.getExceptionRecursionError(e));
//			e.printStackTrace();
		} finally {
		}
		return text;

	}

	/**
	 * ����Ԫ�����ļ���jgraph��
	 * @param file
	 * @param loadLazy �Ƿ�ʹ��������
	 * @return
	 */
	public static JGraph loadFromFile(File file, boolean loadLazy) {
		JGraph graph = XMLSerialize.getInstance().paserXmlToMDP(file.getPath(),
				loadLazy);
		return graph;
	}

	/**
	 * ����Ԫ�����ļ�
	 * @param file
	 * @return
	 */
	public static JGraph loadFromFile(File file) {
		return loadFromFile(file, false);
	}
}
