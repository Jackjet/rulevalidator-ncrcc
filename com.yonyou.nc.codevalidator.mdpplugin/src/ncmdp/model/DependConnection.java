package ncmdp.model;

import java.io.PrintWriter;

import ncmdp.serialize.XMLSerialize;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 依赖关联关系
 * @author wangxmn
 *
 */
public class DependConnection extends Connection {
	private static final long serialVersionUID = 3105098908199155237L;

	public DependConnection(Cell source, Cell target) {
		super(source, target);
		setName("depend connection");
	}
	
	@Override
	public void connect() {
		super.connect();
	}

	@Override
	public void disConnect() {
		super.disConnect();
	}
	public Element createElement(Document doc,String componetId){
		Element ele = doc.createElement("dependConnection");
		ele.setAttribute("componentID", componetId);
		setElementAttribute(ele);
		super.appendBendPointEle(doc, ele);
		return ele;
	}
	public void printXMLString(PrintWriter pw,String tabStr,String componetId){
		pw.print(tabStr+"<dependConnection ");
		pw.print(" componentID='"+componetId+"' ");
		pw.print(genXMLAttrString());
		pw.println(">");
		super.printBendPointXML(pw,tabStr+"\t");
		pw.println(tabStr+"</dependConnection>");
	}
	public static DependConnection parseNode(Node node){
		DependConnection con = null;
		String name = node.getNodeName();
		if("dependConnection".equalsIgnoreCase(name)){
			NamedNodeMap map = node.getAttributes();
			if(map != null){
				String srcId = map.getNamedItem("source").getNodeValue();
				String tarId = map.getNamedItem("target").getNodeValue();
				Cell src = XMLSerialize.getInstance().getCell(srcId);
				Cell tar = XMLSerialize.getInstance().getCell(tarId);
				con = new DependConnection(src, tar);
				Connection.parseNodeAttr(node, con);
				Connection.parseConnectionBendPoint(node, con);
			}
		}
		return con;
	}
}
