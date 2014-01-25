package ncmdp.model;

import java.io.PrintWriter;

import ncmdp.serialize.XMLSerialize;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ConvergeConnection extends Connection {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String NODE_NAME = "convergeconnection";
	public ConvergeConnection(Cell source, Cell target) {
		super(source, target);
		setName("converge connection");
	}

	@Override
	public void connect() {
		super.connect();
	}

	@Override
	public void disConnect() {
		super.disConnect();
	}

	public Element createElement(Document doc, String componetId) {
		Element ele = doc.createElement(NODE_NAME);
		ele.setAttribute("componentID", componetId);
		setElementAttribute(ele);
		super.appendBendPointEle(doc, ele);
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, String componetId) {
		pw.print(tabStr + "<"+NODE_NAME);
		pw.print(" componentID='" + componetId + "' ");
		pw.print(genXMLAttrString());
		pw.println(">");
		super.printBendPointXML(pw, tabStr + "\t");
		pw.println(tabStr + "</"+NODE_NAME+">");
	}

	public static ConvergeConnection parseNode(Node node) {
		ConvergeConnection con = null;
		String name = node.getNodeName();
		if (NODE_NAME.equalsIgnoreCase(name)) {
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				String srcId = map.getNamedItem("source").getNodeValue();
				String tarId = map.getNamedItem("target").getNodeValue();
				Cell src = XMLSerialize.getInstance().getCell(srcId);
				Cell tar = XMLSerialize.getInstance().getCell(tarId);
				con = new ConvergeConnection(src, tar);
				Connection.parseNodeAttr(node, con);
				Connection.parseConnectionBendPoint(node, con);
			}
		}
		return con;
	}
	//	@Override
	//	public void createConn() {
	//		if (getSource() instanceof BusiActivity && getTarget() instanceof BusiOperation) {
	//			BusiActivity sourceAct = (BusiActivity) getSource();
	//			BusiOperation targOp = (BusiOperation) getTarget();
	//			sourceAct.addOperation(new RefBusiOperation(targOp,sourceAct.getId()));
	//		}
	//	}
	//	
	//	@Override
	//	public void deleteConn() {
	//		if (getSource() instanceof BusiActivity && getTarget() instanceof BusiOperation) {
	//			BusiActivity sourceAct = (BusiActivity) getSource();
	//			BusiOperation targOp = (BusiOperation) getTarget();
	//			sourceAct.removeOperation(new RefBusiOperation(targOp,sourceAct.getId()));
	//		}
	//	}
}
