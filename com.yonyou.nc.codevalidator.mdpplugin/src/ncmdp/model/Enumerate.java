package ncmdp.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ncmdp.serialize.XMLSerialize;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.TypeSelectePropertyDescriptor;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 枚举类型，目前支持两种方式，String和Integer
 * @author wangxmn
 *
 */
public class Enumerate extends Cell {

	private static final long serialVersionUID = -8287054845488430183L;

	public static final String PROP_RETURN_TYPE = Messages.Enumerate_0;

	public static final String ENUMITEM_ADD = "ADD_ENUMITEM"; 

	public static final String ENUMITEM_REMOVE = "REMOVE_ENUMITEM"; 

	public static final String ENUMITEM_UPDATE = "UPDATE_ENUMITEM"; 

	private String customImplCls = "";// 自定义枚举值实现类 

	private Type returnType = null;

	private List<EnumItem> items = new ArrayList<EnumItem>();

	public Enumerate() {
		super("enum"); 
		Type[] enumTypes = NCMDPTool.getBaseEnumTypes();
		if (enumTypes != null && enumTypes.length > 0) {
			setReturnType(enumTypes[0]);
		}
	}

	public Type getReturnType() {
		return returnType;
	}

	public List<EnumItem> getEnumItems() {
		return items;
	}

	public void setReturnType(Type returnType) {
		Type old = this.returnType;
		this.returnType = returnType;
		firePropertyChange(PROP_RETURN_TYPE, old, this.returnType);
	}

	public void addEnumItem(EnumItem item) {
		items.add(item);
		fireStructureChange(ENUMITEM_ADD, item);
	}

	public void addEnumItem(int index, EnumItem item) {
		items.add(index, item);
		fireStructureChange(ENUMITEM_ADD, item);
	}

	public void removeEnumItem(EnumItem item) {
		items.remove(item);
		fireStructureChange(ENUMITEM_REMOVE, item);
	}

	public void fireEnumItemUpdate(EnumItem item) {
		fireStructureChange(ENUMITEM_UPDATE, item);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[2];
		pds[0] = new TypeSelectePropertyDescriptor(PROP_RETURN_TYPE, Messages.Enumerate_6,
				NCMDPTool.getBaseEnumTypes(), false);
		pds[1] = new TextPropertyDescriptor("customImplCls", Messages.Enumerate_8); 
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROP_RETURN_TYPE.equals(id)) {
			return getReturnType();
		} else if ("customImplCls".equals(id)) { 
			return getCustomImplCls();
		} else
			return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (PROP_RETURN_TYPE.equals(id)) {
			setReturnType((Type) value);
		} else if ("customImplCls".equals(id)) { 
			setCustomImplCls((String) value);
		} else
			super.setPropertyValue(id, value);
	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		if (getReturnType() != null)
			sb.append(getReturnType().genXMLAttrString());
		return sb.toString();

	}

	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		if (getReturnType() != null)
			getReturnType().setElementAttribute(ele);
		ele.setAttribute("modInfoClassName", getCustomImplCls()); 
	}

	public Element createElement(Document doc, JGraph graph) {
		Element ele = doc.createElement("Enumerate"); 
		ele.setAttribute("componentID", graph.getId()); 
		setElementAttribute(ele);
		Element enumlistEle = doc.createElement("enumitemlist"); 
		ele.appendChild(enumlistEle);
		List<EnumItem> items = getEnumItems();
		for (int i = 0; i < items.size(); i++) {
			enumlistEle.appendChild(items.get(i).createElement(doc, getId(), i));
		}
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, JGraph graph) {
		pw.print(tabStr + "<Enumerate "); 
		pw.print(" componentID='" + graph.getId() + "' ");  //$NON-NLS-2$
		pw.print(genXMLAttrString());
		pw.println(">"); 
		pw.println(tabStr + "\t<enumitemlist>"); 
		List<EnumItem> items = getEnumItems();
		for (int i = 0; i < items.size(); i++) {
			items.get(i).printXMLString(pw, tabStr + "\t\t", getId(), i); 
		}
		pw.println(tabStr + "\t</enumitemlist>"); 
		pw.println(tabStr + "</Enumerate>"); 
	}

	public static void parseNodeAttr(Node node, Enumerate enumerate) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			Cell.parseNodeAttr(node, enumerate);
			Type type = Type.parseType(map);
			if (type != null)
				enumerate.setReturnType(type);
			if (map.getNamedItem("modInfoClassName") != null) { 
				enumerate.setCustomImplCls(map.getNamedItem("modInfoClassName") 
						.getNodeValue());
			}
		}
	}

	public static Enumerate parseNode(Node node) {
		Enumerate enumerate = null;
		String name = node.getNodeName();
		if ("Enumerate".equalsIgnoreCase(name)) { 
			enumerate = new Enumerate();
			Enumerate.parseNodeAttr(node, enumerate);
			parseEnumItems(node, enumerate);
			XMLSerialize.getInstance().register(enumerate);
		}
		return enumerate;
	}

	protected static void parseEnumItems(Node node, Enumerate enumerate) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if ("enumitemlist".equalsIgnoreCase(child.getNodeName())) { 
				NodeList nl2 = child.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					Node node2 = nl2.item(j);
					EnumItem item = EnumItem.parseNode(node2);
					if (item != null)
						enumerate.getEnumItems().add(item);
				}
			}
		}

	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		String msg = super.validate();
		if (msg != null)
			sb.append(msg + "\r\n"); 
		if (getReturnType() == null) {
			return Messages.Enumerate_28;
		}
		List<String> al1 = new ArrayList<String>();
		List<String> al2 = new ArrayList<String>();
		if (getEnumItems().size() > 0) {
			EnumItem item = getEnumItems().get(0);
			String err = item.validate();
			if (err != null)
				sb.append(err);
			al1.add(item.getEnumValue());
			al2.add(item.getEnumDisplay());
		}
		for (int i = 1; i < getEnumItems().size(); i++) {
			EnumItem item = getEnumItems().get(i);
			String err = item.validate();
			if (err != null)
				sb.append(err);
			String value = item.getEnumValue();
			if (value != null && !al1.contains(value)) {
				al1.add(value);
			} else {
				sb.append(Messages.Enumerate_29 + value + "\r\n"); //$NON-NLS-2$
			}
			String disPlay = item.getEnumDisplay();
			if (disPlay != null && !al2.contains(disPlay)) {
				al2.add(disPlay);
			} else {
				sb.append(Messages.Enumerate_31 + disPlay + "\r\n"); //$NON-NLS-2$
			}
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	protected void copy0(Enumerate enu) {
		super.copy0(enu);
		enu.setReturnType(this.getReturnType());
		List<EnumItem> list = this.getEnumItems();
		for (int i = 0; i < list.size(); i++) {
			enu.getEnumItems().add(list.get(i).copy());
		}
	}

	public Enumerate copy() {
		Enumerate enu = new Enumerate();
		copy0(enu);
		return enu;
	}

	private String getCustomImplCls() {
		return customImplCls;
	}

	private void setCustomImplCls(String customImplCls) {
		this.customImplCls = customImplCls;
	}

	@Override
	public String getElementType() {
		return Messages.Enumerate_33;
	}

	@Override
	public void dealIncDevForIndustry() {
		setSource(true);
		for (EnumItem item : items) {
			item.dealIncDevForIndustry();
		}
	}
}
