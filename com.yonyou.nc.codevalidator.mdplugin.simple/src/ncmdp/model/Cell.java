package ncmdp.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ncmdp.serialize.XMLSerialize;
import ncmdp.tool.NCMDPTool;
import ncmdp.wizard.multiwizard.util.IMultiElement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 所有实体、枚举、属性、接口等等模型的父类
 * @author wangxmn
 *
 */
public class Cell extends ElementObj implements IMultiElement {

	private static final long serialVersionUID = 4977465583470274858L;

	public static final String PROP_CLASS_FULL_NAME = "class_full_name"; 

	public static final String PROP_TARGET_CONNECTION = "target_connection"; 

	public static final String PROP_SOURCE_CONNECTION = "source_connection"; 

	public static final String PROP_CELL_LOCATION = "cell_location"; 

	public static final String PROP_CELL_SIZE = "cell_size"; 

	public static final String PROP_VISIBILITY = "visibility"; 

	private String fullClassName = ""; 

	private String resid = ""; 

	private String visibility = Constant.VISIBILITY_PUBLIC;

	private List<Connection> targetConnections = new ArrayList<Connection>();

	private List<Connection> sourceConnections = new ArrayList<Connection>();

//	private Point location = new Point(0, 0);
//
//	private Dimension size = new Dimension(80, 100);

	protected Type relationType = null;//本实体对应的类型

//	private Guide horizontalGuide, verticalGuide;

	private JGraph graph = null;

	/**
	 * 将本实体转化为类型，用于其它开发者作为类型使用
	 * @return
	 */
	public Type converToType() {
		if (relationType == null) {
			relationType = new Type();
			relationType.setCell(this);
		}
		relationType.setDisplayName(getDisplayName());
		relationType.setTypeId(getId());
		relationType.setName(getName());
		return relationType;
	}

	public Cell() {
		super();
	}

	/**
	 * 新建元素
	 * 
	 * @param name
	 */
	public Cell(String name) {
		super(name);//设置了名称、时间以及id等
		initDevInfo();
	}

	/**
	 * 设置开发者信息
	 */
	private void initDevInfo() {
//		if (NCMDPEditor.getActiveMDPEditor() != null) {
//			if (NCMDPEditor.getActiveMDPEditor().getModel()
//					.isIndustryIncrease()) {// 增量开发，新增实体需要 设置versiontype
//				setSource(false);
//			} else {
//				setSource(true);
//			}
////			setVersionType(MDPUtil.getDevInfo());//设置versiontype
//		}
		
		setSource(true);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		String old = this.visibility;
		this.visibility = visibility;
		firePropertyChange(PROP_VISIBILITY, old, visibility);
	}

	/**
	 * 该实体作为conn的原目标
	 * @param conn
	 */
	public void addSourceConn(Connection conn) {
		sourceConnections.add(conn);
		fireStructureChange(PROP_SOURCE_CONNECTION, conn);
	}
	
	public void addSourceConns(List<Connection> conns){
		if(conns==null||conns.isEmpty()){
			return;
		}
		sourceConnections.addAll(conns);
		fireStructureChange(PROP_SOURCE_CONNECTION,null);
	}

	/**
	 * 该实体作为conn的终点
	 * @param conn
	 */
	public void addTargetConn(Connection conn) {
		targetConnections.add(conn);
		fireStructureChange(PROP_TARGET_CONNECTION, conn);
	}

	public void removeSourceConn(Connection conn) {
		sourceConnections.remove(conn);
		fireStructureChange(PROP_SOURCE_CONNECTION, conn);
	}
	
	public void removeSourceConns(List<Connection> conns){
		sourceConnections.removeAll(conns);
		fireStructureChange(PROP_SOURCE_CONNECTION, conns.get(0));
	}

	public void removeTargetConn(Connection conn) {
		targetConnections.remove(conn);
		fireStructureChange(PROP_TARGET_CONNECTION, conn);

	}

	public List<Connection> getSourceConnections() {
		return new ArrayList<Connection>(sourceConnections);
	}

	public List<Connection> getTargetConnections() {
		return new ArrayList<Connection>(targetConnections);
	}

//	public Point getLocation() {
//		return location;
//	}
//
//	public void setLocation(Point location) {
//		Point oldP = this.location;
//		this.location = location;
//		firePropertyChange(PROP_CELL_LOCATION, oldP, location);
//		adjustConnBendpointAfterSetLocation(oldP, location);
//	}
//
//	public Dimension getSize() {
//		return size;
//	}
//
//	public void setSize(Dimension size) {
//		Dimension oldD = this.size;
//		this.size = size;
//		firePropertyChange(PROP_CELL_SIZE, oldD, size);
//	}
//
//	private void adjustConnBendpointAfterSetLocation(Point oldP, Point newP) {
//		List<Connection> sourceConn = getSourceConnections();
//		int dx = newP.x - oldP.x;
//		int dy = newP.y - oldP.y;
//		for (int i = 0; i < sourceConn.size(); i++) {
//			Connection conn = sourceConn.get(i);
//			if (conn.getTarget().equals(conn.getSource())) {
//				List<Point> list = conn.getBendPoints();
//				for (int j = 0; j < list.size(); j++) {
//					Point p = list.get(j);
//					p.x += dx;
//					p.y += dy;
//				}
//				conn.fireBendPointUpdate();
//			}
//		}
//	}
//
//	@Override
//	public IPropertyDescriptor[] getPropertyDescriptors() {
//		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
//		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
//		PropertyDescriptor[] pds = new PropertyDescriptor[3];
//		pds[0] = new ComboBoxPropertyDescriptor(PROP_VISIBILITY,
//				Messages.Cell_8, Constant.VISIBILITYS);
//		pds[0].setCategory(Messages.Cell_9);
//		pds[1] = new TextPropertyDescriptor(PROP_CLASS_FULL_NAME,
//				Messages.Cell_10);
//		pds[1].setCategory(Messages.Cell_11);
//		pds[2] = new TextPropertyDescriptor("resid", Messages.Cell_13); 
//		pds[2].setCategory(Messages.Cell_14);
//		al.addAll(Arrays.asList(pds));
//		return al.toArray(new IPropertyDescriptor[0]);
//	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROP_VISIBILITY.equals(id)) {
			return NCMDPTool.getVisibilityIndex(getVisibility());
		} else if (PROP_CLASS_FULL_NAME.equals(id)) {
			return getFullClassName();
		} else if ("resid".equals(id)) { 
			return getResid();
		} else
			return super.getPropertyValue(id);
	}

	@Override
	public boolean isPropertySet(Object id) {
		if (PROP_ELEMENT_NAME.equals(id))
			return true;
		return super.isPropertySet(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (PROP_VISIBILITY.equals(id)) {
			Integer i = (Integer) value;
			setVisibility(Constant.VISIBILITYS[i.intValue()]);
		} else if (PROP_CLASS_FULL_NAME.equals(id)) {
			setFullClassName((String) value);
		} else if ("resid".equals(id)) { 
			setResid((String) value);
		} else
			super.setPropertyValue(id, value);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		ele.setAttribute("fullClassName", getFullClassName()); 
		ele.setAttribute("visibility", getVisibility()); 
//		ele.setAttribute("x", getLocation().x + "");  
//		ele.setAttribute("y", getLocation().y + "");  
//		ele.setAttribute("width", getSize().width + "");  
//		ele.setAttribute("height", getSize().height + "");  
		ele.setAttribute("resid", getResid()); 

	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append(" fullClassName='" + getFullClassName() + "' ");  
		sb.append("visibility='").append(this.getVisibility()).append("' ");  
//		sb.append("x='").append(this.getLocation().x).append("' ");  
//		sb.append("y='").append(this.getLocation().y).append("' ");  
//		sb.append("width='").append(this.getSize().width).append("' ");  
//		sb.append("height='").append(this.getSize().height).append("' ");  
		sb.append("resid='").append(this.getResid()).append("' ");  

		return sb.toString();

	}

	protected static void parseNodeAttr(Node node, Cell cell) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			ElementObj.parseNodeAttr(node, cell);
			cell.setVisibility(map.getNamedItem("visibility").getNodeValue()); 
//			int x = Integer.parseInt(map.getNamedItem("x").getNodeValue()); 
//			int y = Integer.parseInt(map.getNamedItem("y").getNodeValue()); 
////			cell.setLocation(new Point(x, y));
//			int wid = Integer
//					.parseInt(map.getNamedItem("width").getNodeValue()); 
//			int hei = Integer.parseInt(map.getNamedItem("height") 
//					.getNodeValue());
//			cell.setSize(new Dimension(wid, hei));
			if (map.getNamedItem("fullClassName") != null) { 
				cell.setFullClassName(map.getNamedItem("fullClassName") 
						.getNodeValue());
			}
			// /**yuyonga add*/
			// if(map.getNamedItem("className") != null){
			// cell.setClassName(map.getNamedItem("className").getNodeValue());
			// }
			// /**end*/
			if (map.getNamedItem("resid") != null) { 
				cell.setResid(map.getNamedItem("resid").getNodeValue()); 
			}
		}
	}

	public static Cell parseNode(Node node) {
		Cell cell = null;
		String name = node.getNodeName();
		if ("cell".equalsIgnoreCase(name)) { 
			cell = new Cell();
			Cell.parseNodeAttr(node, cell);
			XMLSerialize.getInstance().register(cell);
		}
		return cell;
	}

	public Element createElement(Document doc, JGraph graph) {
		Element ele = doc.createElement("cell"); 
		ele.setAttribute("componentID", graph.getId()); 
		this.setElementAttribute(ele);
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, JGraph graph) {
		pw.print(tabStr + "<cell "); 
		String componentID = graph.getId();
		pw.print(" componentID='" + componentID + "' ");  
		pw.print(genXMLAttrString());
		pw.println(">"); 
		pw.println(tabStr + "</cell>"); 
	}

	public JGraph getGraph() {
		return graph;
	}

	public void setGraph(JGraph graph) {
		this.graph = graph;
	}

	public String getFullClassName() {
		if (fullClassName == null || fullClassName.trim().length() == 0) {
			return NCMDPTool.getCellFullClassName(getGraph(), this);
		}
		return fullClassName;
	}

	public String getCurFullClassName() {
		return fullClassName;
	}

	public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}

	/**
	 * 当前元素是否需要检查 名称 以及 表名称 以及 全类名 不检查的情况：增量开发下原有的元素
	 */
	public boolean needCheckCurNameForPro() {
		return !(getGraph().isIndustryIncrease() && isSource());
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();

		String programTag = getGraph().getEndStrForIndustryElement();
		if (needCheckCurNameForPro()) {
			if (!getName().endsWith(programTag)) {
				sb.append(Messages.Cell_59 + getName() + Messages.Cell_60
						+ getDisplayName() + Messages.Cell_61 + programTag
						+ Messages.Cell_62);
			}
		}
		String msg = super.validate();
		if (msg != null)
			sb.append(msg + "\r\n"); 
		List<Connection> sourConList = getSourceConnections();
		ArrayList<Attribute> al = new ArrayList<Attribute>();
		for (int i = 0; i < sourConList.size(); i++) {
			Connection con = sourConList.get(i);
			if (con instanceof Relation) {
				Relation relation = (Relation) con;
				Attribute attr = relation.getSrcAttribute();
				if (attr != null) {
					if (al.contains(attr)) {
						sb.append(Messages.Cell_64 + attr.getDisplayName()
								+ Messages.Cell_65);
					} else {
						al.add(attr);
					}
				}
			}
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	public void dealBeforeSave() {
		// 保存前做一些处理
	}

	protected void copy0(Cell cell) {
		cell.setCreator(this.getCreator());
		cell.setDesc(this.getDesc());
		cell.setDisplayName(this.getDisplayName());
		cell.setFullClassName(this.getFullClassName());
//		cell.setLocation(this.getLocation());
		cell.setModifier(this.getModifier());
		cell.setName(this.getName());
//		cell.setSize(this.getSize());
		cell.setVersionType(this.getVersionType());
		cell.setIndustryChanged(this.isIndustryChanged());
		cell.setCreateIndustry(this.getCreateIndustry());
		cell.setModifyIndustry(this.getModifyIndustry());
		cell.setSource(this.isSource());
		cell.setVisibility(this.getVisibility());
	}

	public Cell copy() {
		Cell cell = new Cell();
		copy0(cell);
		return cell;
	}

	public boolean showInExplorerTree() {
		return true;
	}

	public String getResid() {
		return resid;
	}

	public void setResid(String resId) {
		this.resid = resId;
	}

//	public Guide getHorizontalGuide() {
//		return horizontalGuide;
//	}
//
//	public void setHorizontalGuide(Guide horizontalGuide) {
//		this.horizontalGuide = horizontalGuide;
//	}
//
//	public Guide getVerticalGuide() {
//		return verticalGuide;
//	}
//
//	public void setVerticalGuide(Guide verticalGuide) {
//		this.verticalGuide = verticalGuide;
//	}

	@Override
	public String getElementType() {
		return Messages.Cell_66;
	}

	public void dealIncDevForIndustry() {
	}
}
