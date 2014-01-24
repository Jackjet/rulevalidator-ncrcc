package ncmdp.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ncmdp.serialize.XMLSerialize;
import ncmdp.ui.ResizeableComboBoxPropertyDescriptor;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
/**
 * 关联，聚合的父类
 * @author wangxmn
 *
 */
public class Relation extends Connection {
	private static final long serialVersionUID = 1794771154424833502L;

	public static final String SOURCE_CONSTRAINT = "sourceConstraint"; 

	public static final String TARGET_CONSTRAINT = "targetConstrain"; 

	public static final String PROP_SOURCE_ATTR = "relation_sourc_attr"; 

	private String sourceConstraint = ""; 

	private String targetConstraint = ""; 

	private Attribute srcAttribute = null;

	public Relation(Cell source, Cell target) {
		super(source, target);
		setName("relation"); 
		setDisplayName(""); 
		setSourceConstraint(Constant.MULTI_1);
		setTargetConstraint(Constant.MULTI_1);
	}

	public String getSourceConstraint() {
		return sourceConstraint;
	}

	public void disConnect() {
		super.disConnect();
		srcAttribute = null;
		sourceConstraint = null;
		targetConstraint = null;
	}

	public Attribute getSrcAttribute() {
		return srcAttribute;
	}

	public void setSourcAttribute(Attribute attr) {
		setSrcAttribute(attr, false);
	}

	private void setSrcAttribute(Attribute attr, boolean isSerialized) {
		Attribute old = this.srcAttribute;
		srcAttribute = attr;
		Cell target = getTarget();
		if (!isSerialized && target != null && srcAttribute != null) {
			srcAttribute.setType(target.converToType());
			if (getTargetConstraint() != null && getTargetConstraint().indexOf("n") != -1) { 
				srcAttribute.setTypeStyle("ARRAY"); 
				srcAttribute.setAccessStrategy("nc.md.model.access.BodyOfAggVOAccessor"); 
			} else {
				srcAttribute.setTypeStyle("REF"); 
			}
			srcAttribute.setLength(""); 
		}
		firePropertyChange(PROP_SOURCE_ATTR, old, attr);
		getSource().fireStructureChange("", null); 
	}

	public void setSourceConstraint(String sourceConstraint) {
		String oldStr = this.sourceConstraint;
		this.sourceConstraint = sourceConstraint;
		firePropertyChange(SOURCE_CONSTRAINT, oldStr, sourceConstraint);
	}

	private void setSourceConstraintWhenSerialized(String sourceConstraint) {
		this.sourceConstraint = sourceConstraint;
	}

	public String getTargetConstraint() {
		return targetConstraint;
	}

	public void setTargetConstraint(String targetConstrain) {
		String oldStr = this.targetConstraint;
		this.targetConstraint = targetConstrain;
		if (srcAttribute != null) {
			if (getTargetConstraint() != null && getTargetConstraint().indexOf("n") != -1) { 
				srcAttribute.setTypeStyle("ARRAY"); 
			} else {
				srcAttribute.setTypeStyle("REF"); 
			}
		}
		firePropertyChange(TARGET_CONSTRAINT, oldStr, targetConstrain);
	}

	private void setTargetConstraintWhenSerialized(String targetConstrain) {
		this.targetConstraint = targetConstrain;
	}

	private String[] getSrcPropDisplaynames() {

		List<Attribute> attrs = null;
		Cell source = getSource();
		if (source instanceof ValueObject)
			attrs = ((ValueObject) source).getProps();

		int count = attrs == null ? 0 : attrs.size();
		String[] strs = new String[count + 1];
		strs[0] = ""; 
		for (int i = 1; i <= count; i++) {
			strs[i] = attrs.get(i - 1).getDisplayName();
		}
		return strs;
	}

	private Attribute getAttributeByIndex(int index) {
		if (index == -1)
			return null;
		List<Attribute> attrs = null;
		Cell source = getSource();
		if (source instanceof ValueObject)
			attrs = ((ValueObject) source).getProps();
		Attribute attr = null;
		if (attrs != null)
			attr = attrs.get(index);
		return attr;

	}

	private Integer getAttributeIndex(Attribute attr) {
		List<Attribute> attrs = null;
		Cell source = getSource();
		if (source instanceof ValueObject)
			attrs = ((ValueObject) source).getProps();
		int i = -1;
		if (attrs != null)
			i = attrs.indexOf(attr);
		return Integer.valueOf(i + 1);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] superIPD = super.getPropertyDescriptors();
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>(Arrays.asList(superIPD));

		PropertyDescriptor[] ipds = new PropertyDescriptor[3];
		ipds[0] = new TextPropertyDescriptor(SOURCE_CONSTRAINT, Messages.Relation_17);
		ipds[0].setCategory(Messages.Relation_18);
		ipds[1] = new TextPropertyDescriptor(TARGET_CONSTRAINT, Messages.Relation_19);
		ipds[1].setCategory(Messages.Relation_20);
		ipds[2] = new ResizeableComboBoxPropertyDescriptor("srcPropName", Messages.Relation_22, 
				getSrcPropDisplaynames());
		ipds[2].setCategory(Messages.Relation_23);
		al.addAll(Arrays.asList(ipds));
		return al.toArray(new IPropertyDescriptor[0]);

	}

	@Override
	public Object getPropertyValue(Object id) {
		if (SOURCE_CONSTRAINT.equals(id))
			return getSourceConstraint();
		else if (TARGET_CONSTRAINT.equals(id))
			return getTargetConstraint();
		else if ("srcPropName".equals(id)) { 
			return getAttributeIndex(srcAttribute);
		} else {
			return super.getPropertyValue(id);
		}
	}

	@Override
	public boolean isPropertySet(Object id) {
		if (SOURCE_CONSTRAINT.equals(id) || TARGET_CONSTRAINT.equals(id) || "srcPropName".equals(id)) 
			return true;
		return super.isPropertySet(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (SOURCE_CONSTRAINT.equals(id)) {
			setSourceConstraint((String) value);
		} else if (TARGET_CONSTRAINT.equals(id)) {
			setTargetConstraint((String) value);
		} else if ("srcPropName".equals(id)) { 
			Integer integer = (Integer) value;
			setSrcAttribute(getAttributeByIndex(integer.intValue() - 1), false);
		} else {
			super.setPropertyValue(id, value);
		}
	}

	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		ele.setAttribute("sourceConstraint", getSourceConstraint()); 
		ele.setAttribute("targetConstraint", getTargetConstraint()); 
		ele.setAttribute("srcAttributeid", srcAttribute == null ? "" : srcAttribute.getId());  

	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append("sourceConstraint='").append(this.getSourceConstraint()).append("' ");  
		sb.append("targetConstraint='").append(this.getTargetConstraint()).append("' ");  
		sb.append("srcAttributeid='").append(srcAttribute == null ? "" : srcAttribute.getId()).append(  
				"' "); 
		return sb.toString();

	}

	public Element createElement(Document doc, String componetId) {
		Element ele = doc.createElement("relation"); 
		ele.setAttribute("componentID", componetId); 
		setElementAttribute(ele);
		super.appendBendPointEle(doc, ele);
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, String componentId) {
		pw.print(tabStr + "<relation "); 
		pw.print(" componentID='" + componentId + "' ");  
		pw.print(genXMLAttrString());
		pw.println(">"); 
		super.printBendPointXML(pw, tabStr + "\t"); 
		pw.println(tabStr + "</relation>"); 
	}

	public static void parseNodeAttr(Node node, Relation con) {
		Connection.parseNodeAttr(node, con);
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			con.setSourceConstraintWhenSerialized(map.getNamedItem("sourceConstraint").getNodeValue()); 
			con.setTargetConstraintWhenSerialized(map.getNamedItem("targetConstraint").getNodeValue()); 
		}
	}

	protected static void parseSrcAttr(Node node, Relation con, ValueObject vo) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			String srcAttId = map.getNamedItem("srcAttributeid").getNodeValue(); 
			Attribute attr = vo.getAttrById(srcAttId);
			con.setSrcAttribute(attr, true);
		}

	}

	public static Relation parseNode(Node node) {
		Relation con = null;
		String name = node.getNodeName();
		if ("relation".equalsIgnoreCase(name)) { 
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				String srcId = map.getNamedItem("source").getNodeValue(); 
				String tarId = map.getNamedItem("target").getNodeValue(); 
				Cell src = XMLSerialize.getInstance().getCell(srcId);
				Cell tar = XMLSerialize.getInstance().getCell(tarId);
				con = new Relation(src, tar);
				if (src instanceof ValueObject) {
					parseSrcAttr(node, con, (ValueObject) src);
				}
				Relation.parseNodeAttr(node, con);
				Connection.parseConnectionBendPoint(node, con);
			}
		}
		return con;
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		String msg = super.validate();
		if (msg != null)
			sb.append(msg + "\r\n"); 
		if (isNull(getSourceConstraint())) {
			sb.append(Messages.Relation_53);
		}
		if (isNull(getTargetConstraint())) {
			sb.append(Messages.Relation_54);
		}
		if (getSrcAttribute() == null) {
			sb.append(Messages.Relation_55);
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}
}
