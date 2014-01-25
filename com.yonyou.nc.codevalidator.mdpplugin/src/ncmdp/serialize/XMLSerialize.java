package ncmdp.serialize;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ncmdp.model.Cell;
import ncmdp.model.JGraph;
import ncmdp.util.MDPLogger;
import ncmdp.util.MDPUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * ���ڽ���Ԫ�����ļ�����Ҫ����������Jgraph�����ļ��Ĺ���
 * @author wangxmn
 */
public class XMLSerialize {
	private static XMLSerialize instance = new XMLSerialize();;
	private HashMap<String, Cell> hm = new HashMap<String, Cell>();
	private XMLSerialize() {
		super();
	}
	public static XMLSerialize getInstance() {
		return instance;
	}
	public void register(Cell e) {
		hm.put(e.getId(), e);
	}
	public Cell getCell(String id) {
		return hm.get(id);
	}

	/**
	 * ����JGraphΪ����ļ�
	 * @param pw
	 * @param jgraph
	 */
	public void serializedToXML(PrintWriter pw, JGraph jgraph) {
		pw.println("<?xml version='1.0' encoding='gb2312'?>");
		jgraph.printXMLString(pw, "");
	}

	/**
	 * ����Ԫ�����ļ�
	 * @param filePath
	 * @param loadLazy
	 * @return
	 */
	public JGraph paserXmlToMDP(String filePath, boolean loadLazy) {
		//�ж��ļ��Ƿ�Ϊbmf�ļ������������JGraph
		JGraph graph = MDPUtil.isBpfInput(filePath)?JGraph.getBPFJGraph():JGraph.getBMFJGraph();
		FileInputStream fis = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			fis = new FileInputStream(filePath);
			Document doc = db.parse(fis);
			Node root = doc.getDocumentElement();
			if (JGraph.JGRAPH_XML_TAG.equalsIgnoreCase(root.getNodeName())) {
				//�����ڵ�
				graph = JGraph.parseNode(root, loadLazy);
			}
		} catch (ParserConfigurationException e) {
			MDPLogger.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			MDPLogger.error(e.getMessage(), e);
		} catch (SAXException e) {
			MDPLogger.error(e.getMessage(), e);
		} catch (IOException e) {
			MDPLogger.error(e.getMessage(), e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					MDPLogger.error(e.getMessage(), e);
				}
			}
		}
		return graph;
	}
}
