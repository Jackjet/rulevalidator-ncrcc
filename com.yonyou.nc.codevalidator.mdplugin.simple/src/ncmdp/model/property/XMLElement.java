package ncmdp.model.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ncmdp.model.Constant;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLElement {
	private HashMap<String, XMLAttribute> hmAttrs = new HashMap<String, XMLAttribute>();
	private String text = "";
	private String elementType = Constant.XML_TYPE_PROPERTY;
	private ArrayList<XMLElement> childs = new ArrayList<XMLElement>();
	public XMLElement(String elementType) {
		super();
		this.elementType = elementType;
		initAttrsByType();
	}
	
	private void initAttrsByType(){
		if(getElementType().equalsIgnoreCase(Constant.XML_TYPE_PROPERTY)){
			putAttributes("name", "");
		}else if(getElementType().equalsIgnoreCase(Constant.XML_TYPE_PROP)){
			putAttributes("key", "");
		}else if(getElementType().equalsIgnoreCase(Constant.XML_TYPE_ENTRY)){
			putAttributes("key", "");
		}
	}
	public void putAttributes(String key, String value){
		XMLAttribute attr = hmAttrs.get(key);
		if(attr == null){
			attr = new XMLAttribute(key);
			hmAttrs.put(key, attr);
		}
		attr.setValue(value);
	}
	public String getElementType() {
		return elementType;
	}
	public List<XMLAttribute> getAttributes(){
		XMLAttribute[] attrs =hmAttrs.values().toArray(new XMLAttribute[0]);
		return Arrays.asList(attrs);
	}
	public void addXMLElement(XMLElement element){
		childs.add(element);
	}
	public void addXMLElement(int index, XMLElement element){
		childs.add(index,element);
	}
	public void removeXMLElement(XMLElement element){
		childs.remove(element);
	}
	public ArrayList<XMLElement> getChilds(){
		return childs;
	}
	public Element createElement(Document doc){
		Element ele = doc.createElement(getElementType());
		Iterator<XMLAttribute> attrs = hmAttrs.values().iterator();
		while(attrs.hasNext()){
			XMLAttribute attr = attrs.next();
			ele.setAttribute(attr.getKey(), attr.getValue());
		}
		ele.appendChild(doc.createTextNode(getText()));
		if(getChilds().size() != 0){
			for (int i = 0; i < getChilds().size(); i++) {
				XMLElement xmlele = getChilds().get(i);
				ele.appendChild(xmlele.createElement(doc));
			}
		}
		return ele;

	}
	public String serializedXMLString(String tabStr){
		
		StringBuilder sb = new StringBuilder();
		sb.append(tabStr).append("<").append(getElementType()).append(" ");
		Iterator<XMLAttribute> attrs = hmAttrs.values().iterator();
		while(attrs.hasNext()){
			XMLAttribute attr = attrs.next();
			sb.append(attr.genXMLStr());
		}
		sb.append(">");
		sb.append(getText());
		if(getChilds().size() != 0){
			sb.append("\r\n");
			for (int i = 0; i < getChilds().size(); i++) {
				XMLElement ele = getChilds().get(i);
				sb.append(ele.serializedXMLString(tabStr+"\t")).append("\r\n");  
			}
			sb.append(tabStr).append("</").append(getElementType()).append(">");
		}else{
			sb.append("</").append(getElementType()).append(">");
		}
		
		return sb.toString();
	}
	public static XMLElement parseToXmlElement(Node node){
		String eleType = node.getNodeName();
		XMLElement ele = new XMLElement(eleType);
		NamedNodeMap map = node.getAttributes();
		int len = map == null ? 0 : map.getLength();
		for (int i = 0; i < len; i++) {
			Node attrNode = map.item(i);
			ele.putAttributes(attrNode.getNodeName(), attrNode.getNodeValue());
		}
		NodeList childs = node.getChildNodes();
		len = childs == null ? 0 : childs.getLength();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) { 
			Node child = childs.item(i);
			if(child instanceof Element){
				ele.addXMLElement(parseToXmlElement(child));
			}else{
				sb.append(child.getNodeValue().trim());
			}
		}
		ele.setText(sb.toString().trim());
		return ele;
	}
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public ArrayList<String> getAvailableChildElementType(){
		ArrayList<String> al = new ArrayList<String>();
		if(getElementType().equals(Constant.XML_TYPE_PROPERTY)
				||getElementType().equals(Constant.XML_TYPE_LIST)
				||getElementType().equals(Constant.XML_TYPE_SET)
				||getElementType().equals(Constant.XML_TYPE_ENTRY)){
			al.add(Constant.XML_TYPE_REF);
			al.add(Constant.XML_TYPE_VALUE);
			al.add(Constant.XML_TYPE_PROPS);
			al.add(Constant.XML_TYPE_LIST);
			al.add(Constant.XML_TYPE_MAP);
			al.add(Constant.XML_TYPE_SET);
		}else if(getElementType().equals(Constant.XML_TYPE_PROPS)){
			al.add(Constant.XML_TYPE_PROP);
		}else if(getElementType().equals(Constant.XML_TYPE_MAP)){
			al.add(Constant.XML_TYPE_ENTRY);
		}else if(getElementType().equals(Constant.XML_TYPE_PROP)){
			al.add(Constant.XML_TYPE_REF);
			al.add(Constant.XML_TYPE_VALUE);
		}
		return al;
	}
}
