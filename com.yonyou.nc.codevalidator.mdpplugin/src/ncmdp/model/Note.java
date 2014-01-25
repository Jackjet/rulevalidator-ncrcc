package ncmdp.model;

import java.io.PrintWriter;

import ncmdp.serialize.XMLSerialize;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 注释类
 * @author wangxmn
 *
 */
public class Note extends Cell {
	private static final long serialVersionUID = -8412275075420893722L;
	public static final String PROP_REMARK="note_remark";
	private String remark = "";
	
	public Note() {
		super("note");
		
	}
	public Note(String name) {
		super(name);
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		String old = this.remark;
		this.remark = remark;
		firePropertyChange(PROP_REMARK, old, remark);
		
	}
	public Element createElement(Document doc, JGraph graph){
		Element ele = doc.createElement("notecell");
		ele.setAttribute("componentID", graph.getId());
		super.setElementAttribute(ele);
		Element remarkEle = doc.createElement("remark");
		ele.appendChild(remarkEle);
		remarkEle.appendChild(doc.createTextNode(getRemark()));
		return ele;
	}
	public void printXMLString(PrintWriter pw,String tabStr,JGraph graph){
		pw.print(tabStr+"<notecell ");
		pw.print(" componentID='"+graph.getId()+"' ");
		pw.print(super.genXMLAttrString());
		pw.println(">");
		pw.print(tabStr+"\t<remark>");
		pw.print(getRemark());
		pw.println("</remark>");
		pw.println(tabStr+"</notecell>");
	}
	public static Note parseNode(Node node){
		Note note = null;
		String name = node.getNodeName();
		if("notecell".equalsIgnoreCase(name)){
			note = new Note();
			Cell.parseNodeAttr(node, note);
			NodeList nl = node.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node child = nl.item(i);
				if(child.getNodeName().equalsIgnoreCase("remark")){
					parseNoteRemark(child, note);
					break;
				}
			}
			XMLSerialize.getInstance().register(note);
		}
		return note;
	}
	private static void parseNoteRemark(Node node, Note note){
		if(node.getNodeName().equalsIgnoreCase("remark")){
			NodeList nl = node.getChildNodes();
			if(nl.getLength() > 0){
				Node child = nl.item(0);
				String remark = child.getNodeValue();
				note.setRemark(remark);
			}
		}
	}
	public boolean showInExplorerTree(){
		return false;
	}
	@Override
	public String validate() {
		//都没有必要进行验证
		return null;
	}
	
}
