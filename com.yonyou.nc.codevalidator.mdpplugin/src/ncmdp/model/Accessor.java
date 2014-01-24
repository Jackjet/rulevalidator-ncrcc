package ncmdp.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Accessor implements Cloneable {
	public static final String WRAPCLSNAME = "wrapclsname";

	public static class AccProp implements Cloneable {
		private String name = "";

		private String displayName = "";

		private String value = "";

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public AccProp clone() {
			AccProp prop = null;
			try {
				prop = (AccProp) super.clone();
				prop.setDisplayName(this.getDisplayName());
				prop.setName(this.getName());
			} catch (CloneNotSupportedException e) {
				MDPLogger.error(e.getMessage(), e);
			}
			return prop;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	private String name = "";

	private String displayName = "";

	private String clsFullName = "";

	private HashMap<String, AccProp> propmap = new HashMap<String, AccProp>();

	private ArrayList<String> alProps = new ArrayList<String>();

	public String getClsFullName() {
		return clsFullName;
	}

	public void setClsFullName(String clsFullName) {
		this.clsFullName = clsFullName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, AccProp> getPropmap() {
		return propmap;
	}

	private void setPropmap(HashMap<String, AccProp> hm) {
		propmap = hm;
	}

	public ArrayList<String> getAlProps() {
		return alProps;
	}

	private void setAlProps(ArrayList<String> alProps) {
		this.alProps = alProps;
	}

	public String toString() {
		return getDisplayName();
	}

	public Accessor clone() {
		Accessor acc = null;
		try {
			acc = (Accessor) super.clone();
			acc.setClsFullName(this.getClsFullName());
			acc.setDisplayName(this.getDisplayName());
			acc.setName(this.getName());
			acc.setPropmap((HashMap<String, AccProp>) this.getPropmap().clone());
			acc.setAlProps((ArrayList<String>) this.getAlProps().clone());
		} catch (CloneNotSupportedException e) {
			MDPLogger.error(e.getMessage(), e);
		}
		return acc;
	}

	public static Accessor parseNode(Node node) {
		String tag = node.getNodeName();
		Accessor acc = null;
		if ("accessor".equals(tag)) {
			acc = new Accessor();
			NamedNodeMap nnmap = node.getAttributes();
			if (nnmap.getNamedItem("name") != null)
				acc.setName(nnmap.getNamedItem("name").getNodeValue());
			if (nnmap.getNamedItem("displayName") != null)
				acc.setDisplayName(nnmap.getNamedItem("displayName").getNodeValue());
			if (nnmap.getNamedItem("classFullname") != null)
				acc.setClsFullName(nnmap.getNamedItem("classFullname").getNodeValue());

			NodeList nl = node.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node child = nl.item(i);
				String childTag = child.getNodeName();
				if ("properties".equals(childTag)) {
					NodeList propnl = child.getChildNodes();
					for (int j = 0; j < propnl.getLength(); j++) {
						Node propnode = propnl.item(j);
						String propnodeTag = propnode.getNodeName();
						if ("property".equals(propnodeTag)) {
							NamedNodeMap map = propnode.getAttributes();
							String name = map.getNamedItem("name").getNodeValue();
							String value = map.getNamedItem("value").getNodeValue();
							String displayName = null;
							if (map.getNamedItem("displayName") != null) {
								displayName = map.getNamedItem("displayName").getNodeValue();
							}
							if (displayName == null || displayName.trim().length() == 0) {
								displayName = name;
							}
							acc.getAlProps().add(name);
							AccProp prop = new AccProp();
							prop.setName(name);
							prop.setDisplayName(displayName);
							prop.setValue(value);
							acc.getPropmap().put(name, prop);
						}
					}
				}
			}
		}
		return acc;
	}

	public Element createElement(Document doc, ValueObject vo) {
		Element ele = doc.createElement("accessor");
		ele.setAttribute("name", getName());
		ele.setAttribute("displayName", getDisplayName());
		ele.setAttribute("classFullname", getClsFullName());
		//
		Element propsEle = doc.createElement("properties");
		HashMap<String, AccProp> hm = getPropmap();
		Iterator<String> iter = hm.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			AccProp prop = hm.get(name);
			String value = prop.getValue();
			if (WRAPCLSNAME.equals(name)) {
				if (value == null || value.trim().length() == 0) {
					value = vo.getCellDefaultAggFullClassName();
				}
			}
			String displayName = prop.getDisplayName();
			Element tempEle = doc.createElement("property");
			tempEle.setAttribute("name", name);
			tempEle.setAttribute("value", value);
			tempEle.setAttribute("sequence", getAlProps().indexOf(name) + "");
			tempEle.setAttribute("classid", vo.getId());
			tempEle.setAttribute("displayName", displayName);
			//
			propsEle.appendChild(tempEle);
		}
		ele.appendChild(propsEle);
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, ValueObject vo) {
		pw.print(tabStr + "<accessor name='");
		pw.print(getName());
		pw.print("' displayName='");
		pw.print(getDisplayName());
		pw.print("' classFullname='");
		pw.print(getClsFullName());
		pw.println("' >");
		pw.println(tabStr + "\t<properties>");
		HashMap<String, AccProp> hm = getPropmap();
		Iterator<String> iter = hm.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			AccProp prop = hm.get(name);
			String value = prop.getValue();
			if (WRAPCLSNAME.equals(name)) {
				if (value == null || value.trim().length() == 0) {
					value = vo.getCellDefaultAggFullClassName();
				}
			}
			String displayName = prop.getDisplayName();
			pw.print(tabStr + "\t\t<property name='");
			pw.print(name);
			pw.print("' value='");
			pw.print(value);
			pw.print("' sequence='");
			pw.print(getAlProps().indexOf(name));
			pw.print("' classid='");
			pw.print(vo.getId());
			pw.print("' displayName='");
			pw.print(displayName);
			pw.println("' />");

		}
		pw.println(tabStr + "\t</properties>");
		pw.println(tabStr + "</accessor>");
	}

	//	@Override
	//	public boolean equals(Object obj) {
	//		if(obj == null)
	//			return false;
	//		if(!(obj instanceof Accessor))
	//			return false;
	//		if(getAlProps().size() != ((Accessor)obj).getAlProps().size())
	//			return false;
	//		return super.equals(obj);
	//	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Accessor) {
			if (!StringUtil.isEmptyWithTrim(name)) {
				return name.equals(((Accessor) obj).getName());
			} else if (StringUtil.isEmptyWithTrim(((Accessor) obj).getName())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
