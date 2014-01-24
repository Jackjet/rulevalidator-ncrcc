package ncmdp.model;

import java.io.PrintWriter;

import ncmdp.serialize.XMLSerialize;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
/**
 * ×¢ÊÍ¹ØÏµ
 * @author wangxmn
 *
 */
public class NoteConnection extends Connection {
	private static final long serialVersionUID = 5693936078499923196L;
	public NoteConnection(Cell source, Cell target) {
		super(source, target);
		setName("note connection");
		setDisplayName("");
	}
	public Element createElement(Document doc,String componetId){
		Element ele = doc.createElement("noteConnection");
		ele.setAttribute("componentID", componetId);
		setElementAttribute(ele);
		super.appendBendPointEle(doc, ele);
		return ele;
	}
	public void printXMLString(PrintWriter pw,String tabStr,String componetId){
		pw.print(tabStr+"<noteConnection ");
		pw.print(" componentID='"+componetId+"' ");
		pw.print(genXMLAttrString());
		pw.println(">");
		super.printBendPointXML(pw,tabStr+"\t");
		pw.println(tabStr+"</noteConnection>");
	}
	public static NoteConnection parseNode(Node node){
		NoteConnection con = null;
		String name = node.getNodeName();
		if("noteConnection".equalsIgnoreCase(name)){
			NamedNodeMap map = node.getAttributes();
			if(map != null){
				String srcId = map.getNamedItem("source").getNodeValue();
				String tarId = map.getNamedItem("target").getNodeValue();
				Cell src = XMLSerialize.getInstance().getCell(srcId);
				Cell tar = XMLSerialize.getInstance().getCell(tarId);
				con = new NoteConnection(src, tar);
				Connection.parseNodeAttr(node, con);
				Connection.parseConnectionBendPoint(node, con);
			}
		}
		return con;
	}
}
