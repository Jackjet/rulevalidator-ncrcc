package ncmdp.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ncmdp.serialize.XMLSerialize;
import ncmdp.ui.NotEditableTextPropertyDescriptor;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Connection extends ElementObj {
	private static final long serialVersionUID = 1794771154424833502L;

	public static final String PROP_BEND_POINT = "relation_bend_point"; 

	private Cell source = null;

	private Cell target = null;

	private List<Point> bendPoints = new ArrayList<Point>();

	private boolean isConnected = false;

	public Connection(Cell source, Cell target) {
		super("connection"); 
		setDisplayName(""); 
		this.source = source;
		this.target = target;
		connect();

	}

	public Cell getSource() {
		return source;
	}

	public Cell getTarget() {
		return target;
	}

	/**
	 * 创建连线
	 */
	public void connect() {
		if (!isConnected) {
			if (source != null)
				source.addSourceConn(this);
			if (target != null)
				target.addTargetConn(this);
			isConnected = true;
		}
	}

	/**
	 * 删除该链接
	 */
	public void disConnect() {
		if (isConnected) {
			source.removeSourceConn(this);
			target.removeTargetConn(this);
			isConnected = false;
		}
	}

	public void addBendPoint(int index, Point point) {
		bendPoints.add(index, point);
		fireStructureChange(PROP_BEND_POINT, point);
	}

	public Point removeBendPoint(int index) {
		Point point = bendPoints.remove(index);
		fireStructureChange(PROP_BEND_POINT, point);
		return point;
	}

	public List<Point> getBendPoints() {
		//		return new ArrayList<Point>(bendPoints);
		return bendPoints;
	}

	//	public void removeAllBendPoints(){
	//		getBendPoints().clear();
	//		fireStructureChange(PROP_BEND_POINT, null);
	//	}
	public void fireBendPointUpdate() {
		fireStructureChange(PROP_BEND_POINT, null);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));

		PropertyDescriptor[] desc = new PropertyDescriptor[2];
		desc[0] = new NotEditableTextPropertyDescriptor("source", Messages.Connection_4); 
		desc[1] = new NotEditableTextPropertyDescriptor("target", Messages.Connection_6); 
		for (int i = 0; i < desc.length; i++) {
			desc[i].setCategory(Messages.Connection_0);
		}
		al.addAll(Arrays.asList(desc));
		return al.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if ("source".equals(id)) { 
			if (source instanceof Reference) {
				Cell cell = ((Reference) source).getReferencedCell();
				if (cell != null) {
					return cell.getDisplayName();
				} else {
					return source.getDisplayName();
				}
			} else {
				return source.getDisplayName();
			}
		} else if ("target".equals(id)) { 
			if (target instanceof Reference) {
				Cell cell = ((Reference) target).getReferencedCell();
				if (cell != null) {
					return cell.getDisplayName();
				} else {
					return target.getDisplayName();
				}
			} else {
				return target.getDisplayName();
			}
		} else {
			return super.getPropertyValue(id);
		}
	}

	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		String sourId = getSource().getId();
		String targetId = getTarget().getId();
		ele.setAttribute("source", sourId); 
		ele.setAttribute("target", targetId); 
		//
		String realSourId = sourId;
		if (getSource() instanceof Reference) {
			realSourId = ((Reference) getSource()).getRefId();
		}
		ele.setAttribute("realsource", realSourId); 
		String realTargetId = targetId;
		if (getTarget() instanceof Reference) {
			realTargetId = ((Reference) getTarget()).getRefId();
		}
		ele.setAttribute("realtarget", realTargetId); 

	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append("source='").append(getSource().getId()).append("' ");  
		sb.append("target='").append(getTarget().getId()).append("' ");  
		return sb.toString();
	}

	public Element createElement(Document doc, String componetId) {
		Element ele = doc.createElement("connection"); 
		ele.setAttribute("componentID", componetId); 
		setElementAttribute(ele);
		appendBendPointEle(doc, ele);
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, String componetId) {
		pw.print(tabStr + "<connection "); 
		pw.print(" componentID='" + componetId + "' ");  
		pw.print(genXMLAttrString());
		pw.println(">"); 
		printBendPointXML(pw, tabStr + "\t"); 
		pw.println(tabStr + "</connection>"); 
	}

	public void appendBendPointEle(Document doc, Element parent) {
		List<Point> points = getBendPoints();
		Element pointsEle = doc.createElement("points"); 
		parent.appendChild(pointsEle);
		//		pw.println(tabStr+"<points>");
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			Element tempEle = doc.createElement("point"); 
			tempEle.setAttribute("x", p.x + "");  
			tempEle.setAttribute("y", p.y + "");  
			pointsEle.appendChild(tempEle);
		}

	}

	public void printBendPointXML(PrintWriter pw, String tabStr) {
		List<Point> points = getBendPoints();
		pw.println(tabStr + "<points>"); 
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			pw.print(tabStr + "\t<point x='"); 
			pw.print(p.x);
			pw.print("' y='"); 
			pw.print(p.y);
			pw.println("'/>"); 
		}
		pw.println(tabStr + "</points>"); 
	}

	protected static void parseNodeAttr(Node node, Connection con) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			ElementObj.parseNodeAttr(node, con);
		}
	}

	protected static void parseConnectionBendPoint(Node node, Connection con) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			String str = child.getNodeName();
			if ("points".equalsIgnoreCase(str)) { 
				NodeList cnl = child.getChildNodes();
				int index = 0;
				for (int j = 0; j < cnl.getLength(); j++) {
					Node pointNode = cnl.item(j);
					if ("point".equalsIgnoreCase(pointNode.getNodeName())) { 
						NamedNodeMap m = pointNode.getAttributes();
						int x = Integer.parseInt(m.getNamedItem("x").getNodeValue()); 
						int y = Integer.parseInt(m.getNamedItem("y").getNodeValue()); 
						con.addBendPoint(index++, new Point(x, y));
					}
				}
			}
		}

	}

	public static Connection parseNode(Node node) {
		Connection con = null;
		String name = node.getNodeName();
		if ("connection".equalsIgnoreCase(name)) { 
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				String srcId = map.getNamedItem("source").getNodeValue(); 
				String tarId = map.getNamedItem("target").getNodeValue(); 
				Cell src = XMLSerialize.getInstance().getCell(srcId);
				Cell tar = XMLSerialize.getInstance().getCell(tarId);
				con = new Connection(src, tar);
				Connection.parseNodeAttr(node, con);
				Connection.parseConnectionBendPoint(node, con);
			}
		}
		return con;
	}

	public String validate() {
		return null;
	}

	public void createConn() {

	}

	public void deleteConn() {

	}

}
