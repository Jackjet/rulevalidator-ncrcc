package ncmdp.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ncmdp.common.MDPConstants;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.OpInterface;
import ncmdp.tool.basic.StringUtil;
import ncmdp.ui.industry.Industry;
import ncmdp.util.MDPLogger;
import ncmdp.wizard.multiwizard.util.IMultiElement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JGraph extends ElementObj implements IMultiElement {

	private static final long serialVersionUID = 5555845181575916698L;

	public static final String PROP_CHILD_ADD = "prop_child_add"; 
	public static final String PROP_CHILD_REMOVE = "prop_child_remove";
	public static final String PROP_MAIN_ENTITY = "prop_main_entity";
	public static final String GEN_CODE_STYLE = "GEN_CODE_STYLE";
	public static final String CONN_ROUTER_TYPE = "CONN_ROUTER_TYPE";
	public static final String COM_INDUSTRY = "COM_INDUSTRY";
	public static final String COM_PROGRAMCODE = "COM_PROGRAMCODE";
	public static final String JGRAPH_XML_TAG = "component";
	public static final String CELL_LIST = "celllist";
	public static final String CONNECTION_LIST = "connectlist";
	public static final String RULES = "rulers";
	public static final String CELL_REMOVE_LOG = "cellRemoveLog";

	private String nameSpace = "";

	private String ownModule = "";

	private String resModuleName = "";

	private String resid = "";

	private String conRouterType = Constant.CONN_ROUTER_MANUAL;

	private Industry industry = new Industry("0", Messages.JGraph_12); 

	/* 开发维度编码 */
	private String programCode = ""; 

	//在server启动时进行加载
	private boolean isPreLoad = false;

	/* 是否维度增量开发 */
	private boolean isIndustryIncrease = false;

	private long version = 0l;

	private String genCodeStyle = Constant.GENCODE_STYLE_NC;

//	private Ruler topRuler = null, leftRuler = null;;

	private List<Cell> cells = new ArrayList<Cell>();
	
	private Entity mainEntity = null;

	private boolean isbizmodel = false;

	/** yuyonga 日志信息 */
	private HashMap<String, String> map = new HashMap<String, String>();

	private JGraph(boolean isbizmodel) {
		super("component");
		this.isbizmodel = isbizmodel;
	}
	
	public static JGraph getBMFJGraph(){
		return new JGraph(false);
	}
	
	public static JGraph getBPFJGraph(){
		return new JGraph(true);
	}

	/**
	 * 添加新元素
	 * @param cell
	 * @return
	 */
	public boolean addCell(Cell cell) {
		if (cell instanceof Reference) {//当实体为引用时
			String refID = ((Reference) cell).getRefId();
			List<Cell> cells = getCells();
			for (int i = 0; i < cells.size(); i++) {
				Cell temp = cells.get(i);
				if (temp instanceof Reference) {
					if (refID.equals(((Reference) temp).getRefId())) {
						return false;
					}
				} else if (refID.equals(temp.getId())) {
					return false;
				}
			}
		}
		cell.setGraph(this);
		boolean b = false;
		try{
			b = cells.add(cell);
		}catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		}
		if (b) {
			//触发新增元素事件
			fireStructureChange(PROP_CHILD_ADD, cell);
		}
		return b;
	}

	/**
	 * 移除元素触发的事件
	 * @param cell
	 * @return
	 */
	public boolean removeCell(Cell cell) {
		boolean b = cells.remove(cell);
		cell.setGraph(null);
		if (b) {
			fireStructureChange(PROP_CHILD_REMOVE, cell);
		}
		return b;
	}

	public List<Cell> getCells() {
		return cells;
	}
	
	/**
	 * 解析元素
	 */
	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		ele.setAttribute("namespace", getNameSpace()); 
		ele.setAttribute("ownModule", getOwnModule()); 
		ele.setAttribute("mainEntity", getMainEntity() == null ? ""  
				: getMainEntity().getId());
		ele.setAttribute("preLoad", isPreLoad() ? "true" : "false");   
		ele.setAttribute("industryIncrease", isIndustryIncrease() ? "true"  
				: "false");
		ele.setAttribute("fromSourceBmf", isFromSourceBmf() ? "true" : "false");   
		ele.setAttribute("isbizmodel", isbizmodel() ? "true" : "false");   
		ele.setAttribute("resModuleName", getResModuleName()); 
		ele.setAttribute("industry", getIndustry() == null ? "" : getIndustry()  
				.getCode());
		ele.setAttribute("industryName", getIndustry() == null ? ""  
				: getIndustry().getName());
		ele.setAttribute("resid", getResid()); 
		ele.setAttribute("version", getVersion() + "");  
		ele.setAttribute("programcode", getProgramCode() + "");  
		ele.setAttribute("gencodestyle", getGenCodeStyle()); 
		ele.setAttribute("conRouterType", getConRouterType()); 
	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append("namespace='").append(this.getNameSpace()).append("' ");  
		sb.append("ownModule='").append(this.getOwnModule()).append("' ");  
		sb.append("mainEntity='") 
				.append(getMainEntity() == null ? "" : getMainEntity().getId()) 
				.append("' "); 
		sb.append("industry='") 
				.append(getIndustry() == null ? "" : getIndustry().getCode()) 
				.append("' "); 
		sb.append("industryName='") 
				.append(getIndustry() == null ? "" : getIndustry().getName()) 
				.append("' "); 
		sb.append("preLoad='").append(this.isPreLoad() ? "true" : "false")   
				.append("' "); 
		sb.append("industryIncrease='") 
				.append(this.isIndustryIncrease() ? "true" : "false")  
				.append("' "); 
		sb.append("fromSourceBmf='") 
				.append(this.isFromSourceBmf() ? "true" : "false").append("' ");   
		sb.append("isbizmodel='").append(this.isbizmodel() ? "true" : "false")   
				.append("' "); 
		sb.append("resModuleName='").append(this.getResModuleName()) 
				.append("' "); 
		sb.append("resid='").append(this.getResid()).append("' ");  
		sb.append("version='").append(this.getVersion()).append("' ");  
		sb.append("programcode='").append(this.getProgramCode()).append("' ");  
		sb.append("gencodestyle='").append(getGenCodeStyle()).append("' ");  

		return sb.toString();

	}

	public Element createElement(Document doc) {
		Element ele = doc.createElement(JGRAPH_XML_TAG);
		setElementAttribute(ele);
		ArrayList<Connection> cons = new ArrayList<Connection>();
		ArrayList<Cell> al = new ArrayList<Cell>();
		List<Cell> cells = getCells();
		Element cellListEle = doc.createElement("celllist"); 
		ele.appendChild(cellListEle);
		for (int i = 0; i < cells.size(); i++) {
			Cell cell = cells.get(i);
			if (cell instanceof BusinInterface) {
				cellListEle.appendChild(cell.createElement(doc, this));
			} else {
				al.add(cell);
			}
			List<Connection> sourCons = cell.getSourceConnections();
			for (int j = 0; j < sourCons.size(); j++) {
				Connection con = sourCons.get(j);
				if (!cons.contains(con))
					cons.add(con);
			}
			List<Connection> targetCons = cell.getTargetConnections();
			for (int j = 0; j < targetCons.size(); j++) {
				Connection con = targetCons.get(j);
				if (!cons.contains(con))
					cons.add(con);
			}
		}
		for (int i = 0; i < al.size(); i++) {
			cellListEle.appendChild(al.get(i).createElement(doc, this));
		}
		Element connListEle = doc.createElement("connectlist"); 
		ele.appendChild(connListEle);
		for (int i = 0; i < cons.size(); i++) {
			Connection con = cons.get(i);
			connListEle.appendChild(con.createElement(doc, getId()));

		}
		//
		Element refDependsEle = doc.createElement("refdepends"); 
		ele.appendChild(refDependsEle);
		Element refdependLoseIDs = doc.createElement("refdependLoseIDs"); 
		ele.appendChild(refdependLoseIDs);
		createRefDependEle(doc, refDependsEle, refdependLoseIDs);
		/** yuyonga add */
		Element cellRemoveLog = doc.createElement("cellRemoveLog"); 
		ele.appendChild(cellRemoveLog);
		createRefRemoveLog(doc, cellRemoveLog, map);
		/** end */
		Element rulersEle = doc.createElement("rulers"); 
//		createRulersEle(doc, rulersEle);
		ele.appendChild(rulersEle);
		return ele;
	}
//
//	public void setCellRemoveLog(String cellid, String targetPath) {
//		if (map.get(cellid) != null) {
//			map.remove(cellid);
//			map.put(cellid, targetPath);
//		} else {
//			map.put(cellid, targetPath);
//		}
//	}
//
////	private void createRulersEle(Document doc, Element rulersEle) {
////		Element leftRulerEle = getLeftRuler().createElement(doc);
////		Element topRulerEle = getTopRuler().createElement(doc);
////		rulersEle.appendChild(topRulerEle);
////		rulersEle.appendChild(leftRulerEle);
////
////	}
//
	/** yuyonga */
	private void createRefRemoveLog(Document doc, Element refremovelog,
			Map<String, String> map) {
		for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
			Object key = i.next();
			Object value = map.get(key);
			Element ele = doc.createElement("celllog"); 
			ele.setAttribute("compomentID", (String) key); 
			ele.setAttribute("targetpath", (String) value); 
			refremovelog.appendChild(ele);
		}
	}

	private void createRefDependEle(Document doc, Element refDependsEle,
			Element refdependLoseIDs) {
		//获得所有实体属性中费基本类型的类型
		List<Cell> list = getCellsByClass(ValueObject.class);//获得所有的实体
		HashSet<String> idSet = new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			ValueObject vo = (ValueObject) list.get(i);
			HashSet<String> ids = vo.getAllNonBaseTypeIds();
			idSet.addAll(ids);
		}
		//获得所有的引用类型，如引用的实体引用的接口等
		List<Cell> bitfList = getCellsByClass(Reference.class);
		for (int i = 0; i < bitfList.size(); i++) {
			Reference ref = (Reference) bitfList.get(i);
			Cell refCell = ref.getReferencedCell();
			if (refCell != null)
				idSet.add(refCell.getId());
		}
		//业务活动
		// 获取BusiOperation引用到的 OpItfID 暂不考虑跨组件引用，所以这里暂时没用 @dingxm 2010-01-19
		List<Cell> busiOperationList = getCellsByClass(BusiOperation.class);
		for (int i = 0; i < busiOperationList.size(); i++) {
			BusiOperation busiActive = (BusiOperation) busiOperationList.get(i);
			HashSet<String> ids = busiActive.getAllRefOpItfIDs();
			idSet.addAll(ids);
		}
		List<Cell> opItfList = getCellsByClass(OpInterface.class);
		for (int i = 0; i < opItfList.size(); i++) {
			OpInterface opItf = (OpInterface) opItfList.get(i);
			HashSet<String> ids = opItf.getAllependType();
			idSet.addAll(ids);
		}
		//业务活动
		if (idSet.size() > 0) {
			Iterator<String> iter = idSet.iterator();
//			List<String> hadInclude = new ArrayList<String>();
			List<String> losedIds = new ArrayList<String>();
			StringBuilder sbLosedIds = new StringBuilder();
			while (iter.hasNext()) {
				String id = iter.next();
				if (getCellByID(id) != null)
					continue;
//				String filePath = MDPCachePool.getInstance()
//						.getFilePathByIDAndIndustry(id, null);
//				if (!StringUtil.isEmptyWithTrim(filePath)) {
//					if (!hadInclude.contains(filePath)) {
//						Element ele = doc.createElement("dependfile"); 
//						ele.setAttribute("entityid", id); 
//						refDependsEle.appendChild(ele);
//						hadInclude.add(filePath);
//					}
//				} else {
					if (!losedIds.contains(id)) {
						Element ele = doc.createElement("losedid"); 
						ele.setAttribute("id", id); 
						refdependLoseIDs.appendChild(ele);
						losedIds.add(id);
						sbLosedIds.append(id).append("\r\n"); 
					}
//				}
			}
			if (sbLosedIds.length() > 0) {
//				MessageDialog.openInformation(Display.getCurrent()
//						.getActiveShell(), Messages.JGraph_97, Messages.JGraph_98
//						+ sbLosedIds.toString());
			}
		}
	}

	public void printXMLString(PrintWriter pw, String tabStr) {
		pw.print(tabStr + "<" + JGRAPH_XML_TAG + " ");  
		pw.print(genXMLAttrString());
		pw.println(">"); 
		ArrayList<Connection> cons = new ArrayList<Connection>();
		ArrayList<Cell> al = new ArrayList<Cell>();
		List<Cell> cells = getCells();
		pw.println(tabStr + "\t<celllist>"); 
		for (int i = 0; i < cells.size(); i++) {
			Cell cell = cells.get(i);
			if (cell instanceof BusinInterface) {
				cell.printXMLString(pw, tabStr + "\t\t", this); 
			} else {
				al.add(cell);
			}
			List<Connection> sourCons = cell.getSourceConnections();
			for (int j = 0; j < sourCons.size(); j++) {
				Connection con = sourCons.get(j);
				if (!cons.contains(con))
					cons.add(con);
			}
			List<Connection> targetCons = cell.getTargetConnections();
			for (int j = 0; j < targetCons.size(); j++) {
				Connection con = targetCons.get(j);
				if (!cons.contains(con))
					cons.add(con);
			}
		}
		for (int i = 0; i < al.size(); i++) {
			al.get(i).printXMLString(pw, tabStr + "\t\t", this); 
		}
		pw.println(tabStr + "\t</celllist>"); 
		pw.println(tabStr + "\t<connectlist>"); 
		for (int i = 0; i < cons.size(); i++) {
			Connection con = cons.get(i);
			con.printXMLString(pw, tabStr + "\t\t", getId()); 
		}
		pw.println(tabStr + "\t</connectlist>"); 
		pw.println(tabStr + "</" + JGRAPH_XML_TAG + ">");  
	}

	/**
	 * 解析component节点
	 * @param node
	 * @param graph
	 */
	protected static void parseNodeAttr(Node node, JGraph graph) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			ElementObj.parseNodeAttr(node, graph);
			if (map.getNamedItem("namespace") != null) { 
				graph.setNameSpace(map.getNamedItem("namespace").getNodeValue()); 
			}
			if (map.getNamedItem("ownModule") != null) { 
				graph.setOwnModule(map.getNamedItem("ownModule").getNodeValue()); 
			}
			if (map.getNamedItem("preLoad") != null) { 
				graph.setPreLoad("true".equals(map.getNamedItem("preLoad")  
						.getNodeValue()));
			}
			if (map.getNamedItem("industryIncrease") != null) { 
				graph.setIndustryIncrease("true".equals(map.getNamedItem( 
						"industryIncrease").getNodeValue())); 
			}
			if (map.getNamedItem("isbizmodel") != null) { 
				graph.setIsbizmodel("true".equals(map 
						.getNamedItem("isbizmodel").getNodeValue())); 
			}
			if (map.getNamedItem("resModuleName") != null) { 
				graph.setResModuleName(map.getNamedItem("resModuleName") 
						.getNodeValue());
			}
			if (map.getNamedItem("industry") != null 
					&& map.getNamedItem("industryName") != null) { 
				graph.setIndustry(new Industry(map.getNamedItem("industry") 
						.getNodeValue(), map.getNamedItem("industryName") 
						.getNodeValue()));
			}
			if (map.getNamedItem("programcode") != null) { 
				graph.setProgramCode(map.getNamedItem("programcode") 
						.getNodeValue());
			}
			if (map.getNamedItem("resid") != null) { 
				graph.setResid(map.getNamedItem("resid").getNodeValue()); 
			}
			if (map.getNamedItem("version") != null) { 
				String strVer = map.getNamedItem("version").getNodeValue(); 
				graph.setVersion(Long.parseLong(strVer));
			}
			if (map.getNamedItem("gencodestyle") != null) { 
				String style = map.getNamedItem("gencodestyle").getNodeValue(); 
				graph.setGenCodeStyle(style);
			}
			if (map.getNamedItem("conRouterType") != null) { 
				String type = map.getNamedItem("conRouterType").getNodeValue(); 
				graph.setConRouterType(type);
			}
		}
	}

	/**
	 * 设置主实体
	 * @param node
	 * @param graph
	 */
	private static void parseMainEntity(Node node, JGraph graph) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			if (map.getNamedItem("mainEntity") != null) { 
				String mainEntityId = map.getNamedItem("mainEntity") 
						.getNodeValue();
				Cell cell = graph.getCellByID(mainEntityId);
				if (cell instanceof Entity) {
					graph.setMainEntity((Entity) cell);
				}
			}
		}
	}

	/**
	 * 解析各个节点
	 * @param node
	 * @param loadLazy
	 * @return
	 */
	public static JGraph parseNode(Node node, boolean loadLazy) {
		JGraph graph = null;
		String name = node.getNodeName();
		if (JGRAPH_XML_TAG.equalsIgnoreCase(name)) {//解析component节点
			graph = new JGraph(false);//
			parseNodeAttr(node, graph);
			NodeList nl = node.getChildNodes();
			if (loadLazy) {//懒加载
				for (int i = 0; i < nl.getLength(); i++) {
					Node child = nl.item(i);
					String cName = child.getNodeName();
					if (CELL_LIST.equalsIgnoreCase(cName)) { 
						parseCells(child, graph, true);//解析各个实体
					}
				}
			} else {//加载所有实体
				for (int i = 0; i < nl.getLength(); i++) {
					Node child = nl.item(i);
					String cName = child.getNodeName();
					if (CELL_LIST.equalsIgnoreCase(cName)) { 
						parseCells(child, graph, false);
					} else if (CONNECTION_LIST.equalsIgnoreCase(cName)) { 
						parseCons(child, graph);
					} else if (RULES.equalsIgnoreCase(cName)) { 
//						parseRulers(child, graph);
					} else if (CELL_REMOVE_LOG.equalsIgnoreCase(cName)) { 
						parseLogs(child, graph);
					}
				}
			}
			parseMainEntity(node, graph);
		}
		return graph;
	}

	private static void parseLogs(Node node, JGraph graph) {
		NodeList nl = node.getChildNodes();
		int count = nl.getLength();
		for (int i = 1; i < count; i++) {
			Node child = nl.item(i);
			if (child.getNodeType() == 1) {
				String id = child.getAttributes().getNamedItem("compomentID") 
						.getNodeValue();
				String path = child.getAttributes().getNamedItem("targetpath") 
						.getNodeValue();
				graph.getMap().put(id, path);
			}
		}
	}

//	private static void parseRulers(Node node, JGraph graph) {
//		NodeList nl = node.getChildNodes();
//		int count = nl.getLength();
//		for (int i = 0; i < count; i++) {
//			Node child = nl.item(i);
//			Ruler ruler = Ruler.parseNode(child);
//			if (ruler != null) {
//				if (ruler.isHorizontal()) {
//					graph.setTopRuler(ruler);
//				} else {
//					graph.setLeftRuler(ruler);
//				}
//			}
//		}
//	}

	/**
	 * 解析各个celllist标签下所有子节点
	 * @param node
	 * @param graph
	 * @param loadLazy
	 */
	private static void parseCells(Node node, JGraph graph, boolean loadLazy) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			String name = child.getNodeName();
			Cell cell = null;
			if ("cell".equalsIgnoreCase(name)) { 
				cell = Cell.parseNode(child);
			} else if ("opinterface".equalsIgnoreCase(name)) { 
				cell = OpInterface.parseNode(child);
			} else if ("service".equalsIgnoreCase(name)) { 
				cell = Service.parseNode(child);
			} else if ("busiOperation".equalsIgnoreCase(name)) { 
				cell = BusiOperation.parseNode(child);
			} else if ("busiActivity".equalsIgnoreCase(name)) { 
				cell = BusiActivity.parseNode(child);
			} else if ("valueobject".equalsIgnoreCase(name)) { 
				cell = ValueObject.parseNode(child);
			} else if ("entity".equalsIgnoreCase(name)) { 
				cell = Entity.parseNode(child, loadLazy);
			} else if ("Reference".equalsIgnoreCase(name)) { 
				cell = Reference.parseNode(child);
			} else if ("Enumerate".equalsIgnoreCase(name)) { 
				cell = Enumerate.parseNode(child);
			} else if ("notecell".equalsIgnoreCase(name)) { 
				cell = Note.parseNode(child);
			} else if ("busiIterface".equals(name)) { 
				cell = BusinInterface.parseNode(child);
			}
			if (cell != null) {
				cell.setGraph(graph);
				graph.getCells().add(cell);
			}
		}
	}

	private static void parseCons(Node node, JGraph graph) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			String name = child.getNodeName();
			@SuppressWarnings("unused")
			Connection con = null;
			if ("connection".equalsIgnoreCase(name)) { 
				con = Connection.parseNode(child);
			} else if ("dependConnection".equalsIgnoreCase(name)) { 
				con = DependConnection.parseNode(child);
			} else if ("convergeConnection".equalsIgnoreCase(name)) { 
				con = ConvergeConnection.parseNode(child);
			} else if ("ExtendConnection".equalsIgnoreCase(name)) { 
				con = ExtendConnection.parseNode(child);
			} else if ("relation".equalsIgnoreCase(name)) { 
				con = Relation.parseNode(child);
			} else if ("AggregationRelation".equalsIgnoreCase(name)) { 
				con = AggregationRelation.parseNode(child);
			} else if ("noteConnection".equals(name)) { 
				con = NoteConnection.parseNode(child);
			} else if ("busiitfconnection".equals(name)) { 
				con = BusiItfImplConnection.parseNode(child);
			}
			// if (con != null) {
			// System.out
			// .println("parse node " + name + " is null connection");
			// }
		}
	}

	private List<Cell> getNoRefAndNoteCellsList() {
		ArrayList<Cell> al = new ArrayList<Cell>();
		for (int i = 0; i < getCells().size(); i++) {
			Cell cell = getCells().get(i);
			if (!(cell instanceof Reference || cell instanceof Note)) {
				al.add(cell);
			}
		}
		return al;
	}

	//
	public void dealBeforeSave() {
		// 保存前做一些处理,
		//如果是非基础行业的开发，就要进行设置行业的改变和修改行业信息
		if (getIndustry() != null
				&& !MDPConstants.BASE_INDUSTRY.equalsIgnoreCase(getIndustry()
						.getCode())) {
			//改变行业编码，从所有元数据元素的最上层父类进行改变，表明行业的编码可以被修改
			setIndustryChanged(true);
			//设置修改该文件的行业
			setModifyIndustry(getIndustry().getCode());
		}
		// 实体
		List<Cell> cellsWithoutRefAndNote = getNoRefAndNoteCellsList();
		for (int i = 0; i < cellsWithoutRefAndNote.size(); i++) {
			Cell cell = cellsWithoutRefAndNote.get(i);
			cell.dealBeforeSave();
		}
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		String msg = super.validate();
		if (msg != null)
			sb.append(msg + "\r\n");
		if(getEntitys().size()==1&&getMainEntity()==null){
			this.mainEntity = getEntityByIndex(0);
		}
		if (containEntity() && getMainEntity() == null) {
			sb.append(Messages.JGraph_169);
		}
		if (getOwnModule() == null) {
			sb.append(Messages.JGraph_170);
		}
		if(StringUtil.isEmpty(getNameSpace())){
			sb.append(Messages.JGraph_96);
		}
		String stx = getEndStrForIndustryElement();
		if (!isIndustryIncrease && !getName().endsWith(stx)) {
			sb.append(Messages.JGraph_171).append(stx).append(Messages.JGraph_172);
		}
		ArrayList<String> al1 = new ArrayList<String>();
		ArrayList<String> al2 = new ArrayList<String>();
		ArrayList<Connection> al3 = new ArrayList<Connection>();
		List<Cell> cellsWithoutRefAndNote = getNoRefAndNoteCellsList();
		if (cellsWithoutRefAndNote.size() > 0) {
			Cell cell = cellsWithoutRefAndNote.get(0);
			String err = cell.validate();
			if (err != null)
				sb.append(err);
			al1.add(cell.getName());
			al2.add(cell.getDisplayName());
			for (int i = 0; i < cell.getSourceConnections().size(); i++) {
				Connection con = cell.getSourceConnections().get(i);
				if (!al3.contains(con)) {
					al3.add(con);
				}
			}
			for (int i = 0; i < cell.getTargetConnections().size(); i++) {
				Connection con = cell.getTargetConnections().get(i);
				if (!al3.contains(con)) {
					al3.add(con);
				}
			}
		}
		for (int i = 1; i < cellsWithoutRefAndNote.size(); i++) {
			Cell cell = cellsWithoutRefAndNote.get(i);
			String err = cell.validate();
			if (err != null)
				sb.append(err);
			String name = cell.getName();
			if (name != null && !al1.contains(name)) {
				al1.add(name);
			} else {
				sb.append(Messages.JGraph_173 + name + "\r\n"); 
			}
			String disPlay = cell.getDisplayName();
			if (disPlay != null && !al2.contains(disPlay)) {
				al2.add(disPlay);
			} else {
				sb.append(Messages.JGraph_175 + disPlay + "\r\n"); 
			}
			for (int j = 0; j < cell.getSourceConnections().size(); j++) {
				Connection con = cell.getSourceConnections().get(j);
				if (!al3.contains(con)) {
					al3.add(con);
				}
			}
			for (int j = 0; j < cell.getTargetConnections().size(); j++) {
				Connection con = cell.getTargetConnections().get(j);
				if (!al3.contains(con)) {
					al3.add(con);
				}
			}
		}
		for (int i = 0; i < al3.size(); i++) {
			String err = al3.get(i).validate();
			if (err != null) {
				sb.append(err + "\r\n"); 
			}
		}
		String m = validateCellRelation();
		if (m != null)
			sb.append(m);
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	private boolean containEntity() {
		boolean b = false;
		List<Cell> list = getCells();
		for (int i = 0; i < list.size(); i++) {
			Cell cell = list.get(i);
			if (cell instanceof Entity) {
				b = true;
				break;
			}
		}
		return b;
	}

	private String validateCellRelation() {
		List<Cell> list = getCells();
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			Cell cell = list.get(i);
			if (cell instanceof Entity) {
				List<Attribute> atts = ((Entity) cell).getProps();
				for (int j = 0; j < atts.size(); j++) {
					Attribute attr = atts.get(j);
					Type t = attr.getType();
					if (t != null && !al.contains(t.getTypeId())) {
						al.add(t.getTypeId());
						if (!al.contains(cell.getId()))
							al.add(cell.getId());
					}
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			Cell cell = list.get(i);
			if (cell instanceof Entity) {
				if (!cell.equals(getMainEntity()) && !al.contains(cell.getId())) {
					sb.append(Messages.JGraph_178 + cell.getDisplayName() + Messages.JGraph_179);
				}
			}
		}
		if (sb.length() > 0) {
			return sb.toString();
		} else {
			return null;
		}
	}

	public Cell getCellByID(String id) {
		Cell cell = null;
		List<Cell> list = getCells();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId().equals(id)) {
				cell = list.get(i);
				break;
			}
		}
		return cell;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getOwnModule() {
		return ownModule;
	}

	public void setOwnModule(String ownModule) {
		this.ownModule = ownModule;
	}

	private List<Entity> getEntitys() {
		List<Cell> list = getCells();
		List<Entity> al = new ArrayList<Entity>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof Entity) {
				al.add((Entity) list.get(i));
			}
		}
		return al;
	}

//	private String[] getEntitysNames() {
//		List<Entity> list = getEntitys();
//		List<String> al = new ArrayList<String>();
//		al.add(""); 
//		for (int i = 0; i < list.size(); i++) {
//			al.add(((Entity) list.get(i)).getDisplayName());
//		}
//		return al.toArray(new String[0]);
//	}

	private Entity getEntityByIndex(int index) {
		if (index == -1 || index >= getEntitys().size())
			return null;
		return getEntitys().get(index);
	}

	private Integer getEntityIndex(Entity entity) {
		int index = -1;
		index = getEntitys().indexOf(entity);
		return Integer.valueOf(index + 1);
	}

//	/**
//	 * 右侧的属性视图的属性信息
//	 */
//	public IPropertyDescriptor[] getPropertyDescriptors() {
//		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
//		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
//		List<PropertyDescriptor> cur = new ArrayList<PropertyDescriptor>();
//		cur.add(new TextPropertyDescriptor("namespace", Messages.JGraph_182)); 
//		cur.add(new TextPropertyDescriptor("ownModule", Messages.JGraph_184)); // 允许修改 
//		if (!isbizmodel()) {
//			cur.add(new ComboBoxPropertyDescriptor(PROP_MAIN_ENTITY, Messages.JGraph_185,
//					getEntitysNames()));
//			cur.add(new ObjectComboBoxPropertyDescriptor(GEN_CODE_STYLE,
//					Messages.JGraph_186, Constant.GENCODE_STYLES));
//		}
//		cur.add(new NotEditableTextPropertyDescriptor(COM_INDUSTRY, Messages.JGraph_187));
//		cur.add(new NotEditableTextPropertyDescriptor("industryIncrease", 
//				Messages.JGraph_189));
//		cur.add(new ObjectComboBoxPropertyDescriptor("propispreload", Messages.JGraph_191, 
//				new Boolean[] { Boolean.TRUE, Boolean.FALSE }));
//		cur.add(new TextPropertyDescriptor(Messages.JGraph_192, Messages.JGraph_193));
//		cur.add(new TextPropertyDescriptor("resModuleName", Messages.JGraph_195)); 
//		cur.add(new NotEditableTextPropertyDescriptor("version", Messages.JGraph_197)); 
//		cur.add(new ObjectComboBoxPropertyDescriptor(CONN_ROUTER_TYPE, Messages.JGraph_198,
//				Constant.CONN_ROUTER_TYPES));
//		for (PropertyDescriptor pro : cur) {
//			pro.setCategory(Messages.JGraph_199);
//		}
//		al.addAll(cur);
//		return al.toArray(new IPropertyDescriptor[0]);
//	}

	@Override
	public Object getPropertyValue(Object id) {
		if ("namespace".equals(id)) { 
			return getNameSpace();
		} else if ("ownModule".equals(id)) { 
			return getOwnModule();
		} else if (PROP_MAIN_ENTITY.equals(id)) {
			return getEntityIndex(getMainEntity());
		} else if ("propispreload".equals(id)) { 
			return new Boolean(isPreLoad());
		} else if ("industryIncrease".equals(id)) { 
			return new Boolean(isIndustryIncrease());
		} else if ("fromSourceBmf".equals(id)) { 
			return new Boolean(isFromSourceBmf());
		} else if ("isbizmodel".equals(id)) { 
			return new Boolean(isbizmodel());
		} else if ("resModuleName".equals(id)) { 
			return getResModuleName();
		} else if ("resid".equals(id)) { 
			return getResid();
		} else if ("version".equals(id)) { 
			return getVersion();
		} else if (GEN_CODE_STYLE.equals(id)) {
			return getGenCodeStyle();
		} else if (CONN_ROUTER_TYPE.equals(id)) {
			return getConRouterType();
		} else if (COM_INDUSTRY.equals(id)) {
			return getIndustry();
		} else if ("programcode".equals(id)) { 
			return getProgramCode();
		}
		else
			return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if ("namespace".equals(id)) { 
			setNameSpace((String) value);
		} else if ("ownModule".equals(id)) { 
			setOwnModule((String) value);
		} else if (PROP_MAIN_ENTITY.equals(id)) {
			Integer integer = (Integer) value;
			setMainEntity(getEntityByIndex(integer.intValue() - 1));
		} else if ("propispreload".equals(id)) { 
			setPreLoad(((Boolean) value).booleanValue());
		} else if ("industryIncrease".equals(id)) { 
			setIndustryIncrease(((Boolean) value).booleanValue());
		} else if ("isbizmodel".equals(id)) { 
			setIsbizmodel(((Boolean) value).booleanValue());
		} else if ("resModuleName".equals(id)) { 
			setResModuleName((String) value);
		} else if ("resid".equals(id)) { 
			setResid((String) value);
		} else if (GEN_CODE_STYLE.equals(id)) {
			setGenCodeStyle((String) value);
		} else if (CONN_ROUTER_TYPE.equals(id)) {
			setConRouterType((String) value);
		} else if (COM_INDUSTRY.equals(id)) {
			setIndustry((Industry) value);
		} else if ("programcode".equals(id)) { 
			setProgramCode((String) value);
		} else
			super.setPropertyValue(id, value);
	}

	public Entity getMainEntity() {
		return mainEntity;
	}

	public void setMainEntity(Entity mainEntity) {
		this.mainEntity = mainEntity;
	}

	public boolean isPreLoad() {
		return isPreLoad;
	}

	public void setPreLoad(boolean isPreLoad) {
		this.isPreLoad = isPreLoad;
	}

	public boolean isIndustryIncrease() {
		return isIndustryIncrease;
	}

	public void setIndustryIncrease(boolean isIndustryIncrease) {
		this.isIndustryIncrease = isIndustryIncrease;
	}

	public boolean isFromSourceBmf() {
		return true;
	}

	public void setFromSourceBmf(boolean fromSourceBmf) {
		// 暂不做事情
	}

	public String getResModuleName() {
		if (resModuleName == null || resModuleName.trim().length() == 0) {
			return getName();
		}
		return resModuleName;
	}

	public void setResModuleName(String resModuleName) {
		this.resModuleName = resModuleName;
	}

	public String getResid() {
		return resid;
	}

	public void setResid(String resid) {
		this.resid = resid;
	}

	public long getVersion() {
		return version;
	}

	private void setVersion(long version) {
		this.version = version;
	}

	public void updateVersion() {
		version = version + 1;
	}

	public List<Service> getAllService() {
		List<Service> list = new ArrayList<Service>();
		for (int i = 0; i < getCells().size(); i++) {
			Cell cell = getCells().get(i);
			if (cell.getClass().equals(Service.class)) {
				list.add((Service) cell);
			}
		}
		return list;
	}

	public List<OpInterface> getAllOpInterface() {
		List<OpInterface> list = new ArrayList<OpInterface>();
		for (int i = 0; i < getCells().size(); i++) {
			Cell cell = getCells().get(i);
			if (cell.getClass().equals(OpInterface.class)) {
				list.add((OpInterface) cell);
			}
		}
		return list;
	}

	public List<BusiOperation> getAllBusiOperation() {
		List<BusiOperation> list = new ArrayList<BusiOperation>();
		for (int i = 0; i < getCells().size(); i++) {
			Cell cell = getCells().get(i);
			if (cell.getClass().equals(BusiOperation.class)) {
				list.add((BusiOperation) cell);
			}
		}
		return list;
	}

	public List<BusiOperation> getAllToRefBusiOperation() {
		List<BusiOperation> list = new ArrayList<BusiOperation>();
//		for (int i = 0; i < getCells().size(); i++) {
//			Cell cell = getCells().get(i);
//			if (cell.getClass().equals(BusiOperation.class)) {
//				list.add((BusiOperation) cell);
//			} else if (cell.getClass().equals(OpInterface.class)) {
//				OpInterface opItf = (OpInterface) cell;
//				if (opItf.isBusiOperation()) {
//					list.add(BusiOperationUtil.taransOpitf2BusiOperation(opItf));
//				}
//				for (Operation operation : opItf.getOperations()) {
//					if (operation.isBusiActivity()) {
//						list.add(BusiOperationUtil
//								.taransOperation2BusiOperation(operation));
//					}
//				}
//			}
//		}
		return list;
	}

	public String getGenCodeStyle() {
		return genCodeStyle;
	}

	public void setGenCodeStyle(String genCodeStyle) {
		this.genCodeStyle = genCodeStyle;
	}

	public String getConRouterType() {
		return conRouterType;
	}

	public void setConRouterType(String conRouterType) {
		String old = this.conRouterType;
		this.conRouterType = conRouterType;
		firePropertyChange(CONN_ROUTER_TYPE, old, conRouterType);

	}

	public List<Cell> getCellsByClass(Class<?> cls) {
		List<Cell> list = new ArrayList<Cell>();
		for (int i = 0; i < getCells().size(); i++) {
			Cell cell = getCells().get(i);
			if (cls.isInstance(cell)) {
				list.add(cell);
			}
		}
		return list;
	}

//	public Ruler getRuler(boolean isHorizontal) {
//		if (isHorizontal) {
//			return getTopRuler();
//		} else {
//			return getLeftRuler();
//		}
//	}
//
//	public Ruler getLeftRuler() {
//		if (leftRuler == null) {
//			leftRuler = new Ruler(false);
//		}
//		return leftRuler;
//	}
//
//	private void setLeftRuler(Ruler leftRuler) {
//		this.leftRuler = leftRuler;
//	}
//
//	public Ruler getTopRuler() {
//		if (topRuler == null) {
//			topRuler = new Ruler(true);
//		}
//		return topRuler;
//	}
//
//	private void setTopRuler(Ruler topRuler) {
//		this.topRuler = topRuler;
//	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

	public boolean isbizmodel() {
		return isbizmodel;
	}

	public void setIsbizmodel(boolean isbizmodel) {
		this.isbizmodel = isbizmodel;
	}

	public Industry getIndustry() {
		return industry;
	}

	public void setIndustry(Industry industry) {
		this.industry = industry;
	}

	@Override
	public String getElementType() {
		return "组件"; 
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	/**
	 * 在行业增量开发时，对文件做预处理
	 * 
	 * @param industry2
	 * @param programTag
	 */
	public void dealIncDevForIndustry(Industry industry2, String versiontype,
			String programCode) {
		setIndustry(industry2);
		setProgramCode(programCode);
		setIndustryIncrease(true);
		for (Cell cell : cells) {
			cell.dealIncDevForIndustry();
		}
		setVersionType(versiontype);
	}

	/**
	 * 与开发维度相关的 元素后缀名
	 * 
	 * @return
	 */
	public String getEndStrForIndustryElement() {
		if (!MDPConstants.BASE_VERSIONTYPE.equalsIgnoreCase(getVersionType())) {
			String tag = MDPConstants.INDUSTRY_VERSIONTYPE
					.equalsIgnoreCase(getVersionType()) ? getIndustry()
					.getCode() : getProgramCode();
			return "_" + getVersionType() + tag; 
		}
		return ""; 
	}
}
