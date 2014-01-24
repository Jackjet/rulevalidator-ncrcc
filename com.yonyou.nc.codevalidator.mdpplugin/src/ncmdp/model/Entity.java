package ncmdp.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ncmdp.serialize.XMLSerialize;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Entity extends ValueObject {
	private static final long serialVersionUID = 481916107330210409L;
	public static final String PROP_KEY_ATTRIBUTE = "KEY_ATTRIBUTE"; 
	public static final String PROP_UPDATE_ALL_ATTR = "updateAllAttr"; 
	public static final String MODINFO_CLASSNAME = "modinfo_classname"; 
	private String modInfoClassName = ""; 
	private Attribute keyAttribute = null;
	public Entity() {
		this("Entity"); //设置该实体的名称为Entity
	}
	public Entity(String name) {
		super(name);
	}
	public void removeProp(Attribute prop) {
		super.removeProp(prop);
		if (prop != null && prop.equals(getKeyAttribute())) {
			setKeyAttribute(null);
		}
	}
	public Attribute getKeyAttribute() {
		return keyAttribute;
	}
	public void setKeyAttribute(Attribute keyAttrbute) {
		Attribute old = this.keyAttribute;
		this.keyAttribute = keyAttrbute;
		dealAttrKeyValue();
		if (this.keyAttribute != null) {
			this.keyAttribute.setNullable(false);
			firePropertyChange(PROP_KEY_ATTRIBUTE, old, keyAttrbute);
		}
	}
	private String[] getAttributeNames() {
		List<Attribute> attrs = getProps();
		String[] strs = new String[attrs.size() + 1];
		strs[0] = ""; 
		for (int i = 0; i < attrs.size(); i++) {
			strs[i + 1] = attrs.get(i).getDisplayName();
		}
		return strs;
	}
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[2];
		pds[0] = new ComboBoxPropertyDescriptor(PROP_KEY_ATTRIBUTE, Messages.Entity_6,
				getAttributeNames());
		pds[1] = new TextPropertyDescriptor(MODINFO_CLASSNAME, Messages.Entity_7);
		pds[0].setCategory(Messages.Entity_8);
		pds[1].setCategory(Messages.Entity_9);
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROP_KEY_ATTRIBUTE.equals(id)) {
			int index = getProps().indexOf(keyAttribute);
			return Integer.valueOf(index + 1);
		} else if (MODINFO_CLASSNAME.equals(id)) {
			return getModInfoClassName();
		} else
			return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (MODINFO_CLASSNAME.equals(id))
			setModInfoClassName((String) value);
		else if (PROP_KEY_ATTRIBUTE.equals(id)) {
			Integer i = (Integer) value;
			if (i > 0)
				setKeyAttribute(getProps().get(i.intValue() - 1));
			else
				setKeyAttribute(null);
		} else
			super.setPropertyValue(id, value);
	}

	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		ele.setAttribute("keyAttributeId", getKeyAttribute() == null ? ""  
				: getKeyAttribute().getId());
		ele.setAttribute("modInfoClassName", getModInfoClassName()); 

	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append("keyAttributeId='") 
				.append(getKeyAttribute() == null ? "" : getKeyAttribute() 
						.getId()).append("' "); 
		sb.append("modInfoClassName='").append(getModInfoClassName()) 
				.append("' "); 
		return sb.toString();

	}

	private void dealAttrKeyValue() {
		Attribute keyAttr = getKeyAttribute();
		List<Attribute> props = getProps();
		for (int i = 0; i < props.size(); i++) {
			Attribute attr = props.get(i);
			if (attr.equals(keyAttr)) {
				attr.setKey(true);
			} else {
				attr.setKey(false);
			}
		}
		fireStructureChange(PROP_UPDATE_ALL_ATTR, null);

	}

	public Element createElement(Document doc, JGraph graph) {
		dealAttrKeyValue();
		// pw.print(tabStr+"<entity ");
		Element ele = doc.createElement("entity"); 
		ele.setAttribute("componentID", graph.getId()); 
		// pw.print(" componentID='"+graph.getId()+"' ");
		String accClsName = getAccess() == null ? "" : getAccess() 
				.getClsFullName();
		ele.setAttribute("accessorClassName", accClsName); 
		// pw.print(" accessorClassName='"+accClsName+"' ");
		ele.setAttribute("isPrimary", 
				this.equals(graph.getMainEntity()) ? "true" : "false");  
		// if(this.equals(graph.getMainEntity())){
		// pw.print(" isPrimary='"+true+"' ");
		// }else{
		// pw.print(" isPrimary='"+false+"' ");
		// }
		setElementAttribute(ele);
		// pw.print(genXMLAttrString());
		// pw.println(">");
		appendAttrAndOperElement(doc, ele);
		// printAttrAndOper(pw, tabStr);
		appendBusiItfsAndAttrMapElement(doc, ele);
		// printBusiItfsAndAttrMap(pw, tabStr);
		appendCanzhaoEle(doc, ele);
		// printCanzhao(pw, tabStr);
		if (getAccess() != null) {
			ele.appendChild(getAccess().createElement(doc, this));
			// getAccess().printXMLString(pw, tabStr+"\t",this);
		}
		// pw.println(tabStr+"</entity>");
		return ele;

	}

	public void printXMLString(PrintWriter pw, String tabStr, JGraph graph) {
		dealAttrKeyValue();
		pw.print(tabStr + "<entity "); 
		pw.print(" componentID='" + graph.getId() + "' ");  
		String accClsName = getAccess() == null ? "" : getAccess() 
				.getClsFullName();
		pw.print(" accessorClassName='" + accClsName + "' ");  
		if (this.equals(graph.getMainEntity())) {
			pw.print(" isPrimary='" + true + "' ");  
		} else {
			pw.print(" isPrimary='" + false + "' ");  
		}
		pw.print(genXMLAttrString());
		pw.println(">"); 
		printAttrAndOper(pw, tabStr);
		printBusiItfsAndAttrMap(pw, tabStr);
		printCanzhao(pw, tabStr);
		if (getAccess() != null)
			getAccess().printXMLString(pw, tabStr + "\t", this); 
		pw.println(tabStr + "</entity>"); 
	}
	public static Entity parseNode(Node node, boolean loadLazy) {
		Entity entity = null;
		String name = node.getNodeName();
		if ("entity".equalsIgnoreCase(name)) { 
			entity = new Entity();
			ValueObject.parseNodeAttr(node, entity);
			ValueObject.parseVOAttrs(node, entity);
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				String keyAttributeId = map.getNamedItem("keyAttributeId") 
						.getNodeValue();
				Attribute attr = entity.getAttrById(keyAttributeId);
				entity.setKeyAttribute(attr);
				if (map.getNamedItem("modInfoClassName") != null) { 
					entity.setModInfoClassName(map.getNamedItem(
							"modInfoClassName").getNodeValue()); 
				}
			}
			if (!loadLazy) {// ::FIXME @dingxm
				ValueObject.parseOperationList(node, entity);
				ValueObject.parseBusiItfsAndAttrMap(node, entity);// 90% so
																	// terrible，FT
			}
			ValueObject.parseAccessor(node, entity);
			ValueObject.parseCanzhao(node, entity);
			XMLSerialize.getInstance().register(entity);
		}
		return entity;
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		String msg = super.validate();
		if (msg != null)
			sb.append(msg + "\r\n"); 
		if (getKeyAttribute() == null) {
			sb.append(Messages.Entity_43 + getDisplayName() + Messages.Entity_44);
		}
		if (getKeyAttribute() != null && getKeyAttribute().isNullable()) {
			sb.append(Messages.Entity_45 + getDisplayName() + Messages.Entity_46
					+ getKeyAttribute().getDisplayName() + Messages.Entity_47);
		}
		
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	protected void copy0(Entity entity) {
		super.copy0(entity);
		entity.setKeyAttribute(this.getKeyAttribute());
		entity.setModInfoClassName(this.getModInfoClassName());
	}

	public Entity copy() {
		Entity entity = new Entity();
		copy0(entity);
		return entity;
	}

	public String getModInfoClassName() {
		return modInfoClassName;
	}

	public void setModInfoClassName(String modInfoClassName) {
		this.modInfoClassName = modInfoClassName;
	}
}
