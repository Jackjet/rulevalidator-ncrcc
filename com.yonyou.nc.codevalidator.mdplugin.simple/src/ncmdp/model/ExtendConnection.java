package ncmdp.model;

import java.io.PrintWriter;

import ncmdp.serialize.XMLSerialize;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ExtendConnection extends Connection {
	private static final long serialVersionUID = 8161289414059688989L;

	public ExtendConnection(Cell source, Cell target) {
		super(source, target);
		setName("Extend Connection");
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public void connect() {
//		super.connect();
//		if(getTarget() instanceof ValueObject){
//			((ValueObject)getSource()).setSuperEntity((ValueObject)getTarget());
//		}else if(getTarget() instanceof Reference){
//			((ValueObject)getSource()).setSuperEntity((ValueObject)((Reference)getTarget()).getReferencedCell());
//		}
//	}

	@Override
	public void disConnect() {
		super.disConnect();
		((ValueObject)getSource()).setSuperEntity(null);
	}
	public Element createElement(Document doc,String componetId){
		Element ele = doc.createElement("ExtendConnection");
		ele.setAttribute("componentID", componetId);
		setElementAttribute(ele);
		super.appendBendPointEle(doc, ele);
		return ele;
	}
	public void printXMLString(PrintWriter pw,String tabStr,String componetId){
		pw.print(tabStr+"<ExtendConnection ");
		pw.print(" componentID='"+componetId+"' ");
		pw.print(genXMLAttrString());
		pw.println(">");
		super.printBendPointXML(pw,tabStr+"\t");
		pw.println(tabStr+"</ExtendConnection>");
	}
	public static ExtendConnection parseNode(Node node){
		ExtendConnection con = null;
		String name = node.getNodeName();
		if("ExtendConnection".equalsIgnoreCase(name)){
			NamedNodeMap map = node.getAttributes();
			if(map != null){
				String srcId = map.getNamedItem("source").getNodeValue();
				String tarId = map.getNamedItem("target").getNodeValue();
				Cell src = XMLSerialize.getInstance().getCell(srcId);
				Cell tar = XMLSerialize.getInstance().getCell(tarId);
				con = new ExtendConnection(src, tar);
				Connection.parseNodeAttr(node, con);
				Connection.parseConnectionBendPoint(node, con);
			}
		}
		return con;
	}
}
