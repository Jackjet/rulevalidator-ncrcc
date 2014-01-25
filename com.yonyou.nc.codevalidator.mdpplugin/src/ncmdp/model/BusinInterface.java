package ncmdp.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ncmdp.serialize.XMLSerialize;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BusinInterface extends Cell {
	private static final long serialVersionUID = 1294385576170231552L;
	public static final String PROP_ADD_BUSIATTR = "prop_add_busiattr"; 
	public static final String PROP_REMOVE_BUSIATTR = "prop_remove_busiattr"; 
	public static final String PROP_UPDATE_BUSIATTR = "prop_update_busiattr"; 
	public static final String PROP_MAP_VALUEOBJECT = "prop_map_valueobject"; 

	public static final String BUSIITF_IMPCLS = "busiitf_impcls"; 

	private String bizItfImpClassName = ""; 

	private List<BusiItfAttr> attrs = new ArrayList<BusiItfAttr>();

	public BusinInterface() {
		super("businInterface"); 
	}

	public BusinInterface(String name) {
		super(name);
	}

	public void addBusiAttr(BusiItfAttr attr) {
		attrs.add(attr);
		fireStructureChange(PROP_ADD_BUSIATTR, attr);
	}

	public void addBusiAttr(int index, BusiItfAttr attr) {
		attrs.add(index, attr);
		fireStructureChange(PROP_ADD_BUSIATTR, attr);
	}

	public void removeBusiAttr(BusiItfAttr attr) {
		attrs.remove(attr);
		fireStructureChange(PROP_REMOVE_BUSIATTR, attr);
	}

	public void fireUpdateBusiAttr(BusiItfAttr attr) {
		fireStructureChange(PROP_UPDATE_BUSIATTR, attr);
	}

	public List<BusiItfAttr> getBusiItAttrs() {
		return attrs;
	}

	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		ele.setAttribute("bizItfImpClassName", getBizItfImpClassName()); 
	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append(" bizItfImpClassName='" + getBizItfImpClassName() + "' ");  
		return sb.toString();

	}

	public Element createElement(Document doc, JGraph graph) {
		Element ele = doc.createElement("busiIterface"); 
		ele.setAttribute("componentID", graph.getId()); 
		setElementAttribute(ele);
		ele.appendChild(createBusiAttrsElement(doc));
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, JGraph graph) {
		pw.print(tabStr + "<busiIterface "); 
		pw.print(" componentID='" + graph.getId() + "' ");  
		pw.print(genXMLAttrString());
		pw.println(">"); 
		printAttr(pw, tabStr + "\t"); 
		pw.println(tabStr + "</busiIterface>"); 
	}

	private Element createBusiAttrsElement(Document doc) {
		Element ele = doc.createElement("busiItfAttrs"); 
		for (int i = 0; i < getBusiItAttrs().size(); i++) {
			BusiItfAttr attr = getBusiItAttrs().get(i);
			ele.appendChild(attr.createElement(doc, i, getId()));
		}
		return ele;
	}

	private void printAttr(PrintWriter pw, String tabStr) {
		pw.println(tabStr + "<busiItfAttrs>"); 
		for (int i = 0; i < getBusiItAttrs().size(); i++) {
			BusiItfAttr attr = getBusiItAttrs().get(i);
			attr.printXMLString(pw, tabStr, i, getId());
		}
		pw.println(tabStr + "</busiItfAttrs>"); 

	}

	private static void parseBusiItfAttr(BusinInterface itf, Node node) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			BusiItfAttr attr = BusiItfAttr.parseNode(child);
			if (attr != null) {
				itf.getBusiItAttrs().add(attr);
			}
		}
	}

	protected static void parseNodeAttr(Node node, BusinInterface inf) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			Cell.parseNodeAttr(node, inf);
			if (map.getNamedItem("bizItfImpClassName") != null) { 
				inf.setBizItfImpClassName(map
						.getNamedItem("bizItfImpClassName").getNodeValue()); 
			}

		}
	}

	public static BusinInterface parseNode(Node node) {
		BusinInterface inf = null;
		String name = node.getNodeName();
		if ("busiIterface".equalsIgnoreCase(name)) { 
			inf = new BusinInterface();
			BusinInterface.parseNodeAttr(node, inf);
			NodeList nl = node.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node child = nl.item(i);
				if ("busiItfAttrs".equalsIgnoreCase(child.getNodeName())) { 
					parseBusiItfAttr(inf, child);
				}
			}
			XMLSerialize.getInstance().register(inf);
		}
		return inf;
	}

	protected void copy0(BusinInterface intf) {
		super.copy0(intf);
		intf.setBizItfImpClassName(this.getBizItfImpClassName());
		List<BusiItfAttr> al = this.getBusiItAttrs();
		for (int i = 0; i < al.size(); i++) {
			intf.getBusiItAttrs().add(al.get(i).copy());
		}
	}

	public BusinInterface copy() {
		BusinInterface vo = new BusinInterface();
		copy0(vo);
		return vo;
	}

	public String getBizItfImpClassName() {
		return bizItfImpClassName;
	}

	public void setBizItfImpClassName(String bizItfImpClassName) {
		this.bizItfImpClassName = bizItfImpClassName;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[1];
		pds[0] = new TextPropertyDescriptor(BUSIITF_IMPCLS, Messages.BusinInterface_25);
		pds[0].setCategory(Messages.BusinInterface_26);
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (BUSIITF_IMPCLS.equals(id)) {
			return getBizItfImpClassName();
		} else
			return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (BUSIITF_IMPCLS.equals(id)) {
			setBizItfImpClassName((String) value);
		} else
			super.setPropertyValue(id, value);
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		String msg = super.validate();
		if (msg != null)
			sb.append(msg + "\r\n"); 
		ArrayList<String> al1 = new ArrayList<String>();
		ArrayList<String> al2 = new ArrayList<String>();
		if (getBusiItAttrs().size() > 0) {
			BusiItfAttr attr = getBusiItAttrs().get(0);
			String err = attr.validate();
			if (err != null)
				sb.append(err);
			al1.add(attr.getName());
			al2.add(attr.getDisplayName());
		}
		for (int i = 1; i < getBusiItAttrs().size(); i++) {
			BusiItfAttr attr = getBusiItAttrs().get(i);
			String err = attr.validate();
			if (err != null)
				sb.append(err);
			String name = attr.getName();
			if (name != null && !al1.contains(name)) {
				al1.add(name);
			} else {
				sb.append(Messages.BusinInterface_28 + name + "\r\n"); 
			}
			String disPlay = attr.getDisplayName();
			if (disPlay != null && !al2.contains(disPlay)) {
				al2.add(disPlay);
			} else {
				sb.append(Messages.BusinInterface_30 + disPlay + "\r\n"); 
			}
		}
		String programTag = getGraph().getEndStrForIndustryElement();
		if (!getFullClassName().endsWith(programTag)) {
			sb.append(Messages.BusinInterface_32 + getName() + "::" + getDisplayName() 
					+ Messages.BusinInterface_34 + programTag + Messages.BusinInterface_35);
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}
}
