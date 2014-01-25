package ncmdp.model;

import java.io.PrintWriter;
import java.io.Serializable;

import ncmdp.tool.NCMDPTool;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class BusiItfAttr implements Cloneable, Serializable,Constant {
	private static final long serialVersionUID = 8884053828808141291L;
	private String id = ""; //$NON-NLS-1$
	private String name = "name"; //$NON-NLS-1$
	private String displayName = "displayName"; //$NON-NLS-1$
	private String desc = ""; //$NON-NLS-1$
	private String typeStyle = TYPE_STYLES[0];
	private Type type = null;
	public BusiItfAttr() {
		super();
		setId(NCMDPTool.generateID());
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getId() {
		return id;
	}
	private void setId(String id) {
		this.id = id;
	}
	public String getTypeStyle() {
		return typeStyle;
	}
	public void setTypeStyle(String typeStyle) {
		this.typeStyle = typeStyle;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Element createElement(Document doc, int index, String classId){
		Element ele = doc.createElement("busiItAttr"); //$NON-NLS-1$
		ele.setAttribute("sequence", index+""); //$NON-NLS-1$ //$NON-NLS-2$
		ele.setAttribute("classID", classId); //$NON-NLS-1$
		setElementAttribute(ele);
		return ele;
	}
	public void printXMLString(PrintWriter pw, String tabStr, int index, String classId) {
		pw.print(tabStr + "<busiItAttr "); //$NON-NLS-1$
		pw.print(" sequence='"+index+"' "); //$NON-NLS-1$ //$NON-NLS-2$
		pw.print(" classID='"+classId+"' "); //$NON-NLS-1$ //$NON-NLS-2$
		
		pw.print(genXMLAttrString());
		pw.println("/>"); //$NON-NLS-1$
	}
	private void setElementAttribute(Element ele){
		ele.setAttribute("id",getId()); //$NON-NLS-1$
		ele.setAttribute("name",getName()); //$NON-NLS-1$
		ele.setAttribute("displayName",getDisplayName()); //$NON-NLS-1$
		ele.setAttribute("desc",getDesc()); //$NON-NLS-1$
		ele.setAttribute("dataTypeStyle",getTypeStyle()); //$NON-NLS-1$
		if(getType() != null){
			getType().setElementAttribute(ele);
		}
		
	}
	private Object genXMLAttrString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id='").append(this.getId()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("name='").append(this.getName()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("displayName='").append(this.getDisplayName()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("desc='").append(this.getDesc()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("dataTypeStyle='").append(this.getTypeStyle()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		if(getType() != null){
			sb.append(getType().genXMLAttrString());
		}
		return sb.toString();
	}
	public static BusiItfAttr parseNode(Node node){
		BusiItfAttr attr = null;
		String name = node.getNodeName();
		if("busiItAttr".equalsIgnoreCase(name)){ //$NON-NLS-1$
			attr = new BusiItfAttr();
			NamedNodeMap map = node.getAttributes();
			attr.setId(map.getNamedItem("id").getNodeValue()); //$NON-NLS-1$
			attr.setName(map.getNamedItem("name").getNodeValue()); //$NON-NLS-1$
			attr.setDisplayName(map.getNamedItem("displayName").getNodeValue()); //$NON-NLS-1$
			attr.setDesc(map.getNamedItem("desc").getNodeValue()); //$NON-NLS-1$
			if(map.getNamedItem("dataTypeStyle") != null) //$NON-NLS-1$
				attr.setTypeStyle(map.getNamedItem("dataTypeStyle").getNodeValue()); //$NON-NLS-1$
			Type type = Type.parseType(map);
			if(type != null)
				attr.setType(type);
		}
		return attr;
	}
	public BusiItfAttr copy(){
		BusiItfAttr attr = new BusiItfAttr();
		attr.setDesc(this.getDesc());
		attr.setDisplayName(this.getDisplayName());
		attr.setName(this.getName());
		attr.setType(this.getType());
		return attr;
	}
	public String validate(){
		StringBuilder sb = new StringBuilder();
		if(getType() == null)
			sb.append(Messages.BusiItfAttr_36+getDisplayName()+Messages.BusiItfAttr_37);
		if(getTypeStyle()==null|| getTypeStyle().trim().length() == 0){
			sb.append(Messages.BusiItfAttr_38+getDisplayName()+Messages.BusiItfAttr_39);
		}	
		if(sb.length() > 0){
			return sb.toString();
		}else
			return null;
	}


}
