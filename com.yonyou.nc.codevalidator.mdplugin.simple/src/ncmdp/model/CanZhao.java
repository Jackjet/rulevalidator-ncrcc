package ncmdp.model;

import java.io.PrintWriter;
import java.io.Serializable;

import ncmdp.tool.basic.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CanZhao implements Cloneable, Serializable, Constant{
	private static final long serialVersionUID = -7750117107826769566L;
	
	private String canzhaoName = "";
	private boolean isDefault = false;
	private ValueObject vo = null;
	public CanZhao() {
		super();
	}
	public String getName() {
		return canzhaoName;
	}
	public void setName(String canzhaoName) {
		this.canzhaoName = canzhaoName;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public ValueObject getVo() {
		return vo;
	}
	public void setVo(ValueObject vo) {
		this.vo = vo;
	}
	public Element createElement(Document doc){
		Element e = doc.createElement("canzhao");
		e.setAttribute("name",getName());
		e.setAttribute("isdefault",isDefault()?"true":"false");
		e.setAttribute("cellid",getVo().getId());
		return e;
	}
	public void printXMLString(PrintWriter pw, String tabStr) {
		pw.print(tabStr+"<canzhao ");
		pw.print("name='"+XMLUtil.getXMLString(getName())+"' ");
		pw.print("isdefault='"+(isDefault()?"true":"false")+"' ");
		pw.print("cellid='"+getVo().getId()+"' ");
		pw.println("/>");
	}
	public static CanZhao parseNode(Node node, ValueObject vo) {
		CanZhao cz = null;
		if("canzhao".equalsIgnoreCase(node.getNodeName())){
			cz = new CanZhao();
			NamedNodeMap map = node.getAttributes();
			cz.setName(map.getNamedItem("name").getNodeValue());
			cz.setDefault("true".equalsIgnoreCase(map.getNamedItem("isdefault").getNodeValue()));
			cz.setVo(vo);
		}
		return cz;
	}
	
}
