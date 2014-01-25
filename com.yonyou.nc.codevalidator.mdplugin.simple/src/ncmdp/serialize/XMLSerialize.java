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
 * 用于解析元数据文件，主要还是利用了Jgraph解析文件的功能
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
	 * 保存JGraph为组件文件
	 * @param pw
	 * @param jgraph
	 */
	public void serializedToXML(PrintWriter pw, JGraph jgraph) {
		pw.println("<?xml version='1.0' encoding='gb2312'?>");
		jgraph.printXMLString(pw, "");
	}

	/**
	 * 解析元数据文件
	 * @param filePath
	 * @param loadLazy
	 * @return
	 */
	public JGraph paserXmlToMDP(String filePath, boolean loadLazy) {
		//判断文件是否为bmf文件，并创建相关JGraph
		JGraph graph = MDPUtil.isBpfInput(filePath)?JGraph.getBPFJGraph():JGraph.getBMFJGraph();
		FileInputStream fis = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			fis = new FileInputStream(filePath);
			Document doc = db.parse(fis);
			Node root = doc.getDocumentElement();
			if (JGraph.JGRAPH_XML_TAG.equalsIgnoreCase(root.getNodeName())) {
				//解析节点
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
