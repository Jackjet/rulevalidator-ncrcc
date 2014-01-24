package ncmdp.model.activity;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.model.Type;
import ncmdp.model.property.XMLElement;
import ncmdp.serialize.XMLSerialize;
import ncmdp.tool.basic.StringUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 业务操作
 * 
 * @author dingxm
 * 
 */
public class BusiOperation extends Cell {
	private static final long serialVersionUID = 3083730638220114276L;

	public static final String OPERATION_ADD = "BusiOperation_opration_add"; //$NON-NLS-1$

	public static final String OPERATION_REMOVE = "BusiOperation_opration_remove"; //$NON-NLS-1$

	public static final String OPERATION_UPDATE = "BusiOperation_opration_update"; //$NON-NLS-1$

	public static final String IMPL_VERSION = "IMPL_VESION"; //$NON-NLS-1$

	public static final String AUTHORIZATION = "authorization"; //$NON-NLS-1$

	public static final String ISBUSIACTIVITY = "isbusiactivity"; //$NON-NLS-1$

	private static final String OWNER_TYPE = "OWNER_TYPE"; //$NON-NLS-1$
	public static final String LOGTYPE = "LOGTYPE"; //$NON-NLS-1$
	public static final String SERVICE_NEEDLOG = "service_needlog"; //$NON-NLS-1$
	private boolean isAuthorization = true;

	private boolean isBusiActivity = false;

	private boolean needLog = false;
	private String logType = Constant.LOG_TYPE_NONE;
	private List<RefOperation> operations = new ArrayList<RefOperation>();

	private ArrayList<XMLElement> alPropertys = new ArrayList<XMLElement>();

	private String version = ""; //$NON-NLS-1$

	private Type ownType = null;

	public static final String NODE_NAME = "busioperation"; //$NON-NLS-1$

	public BusiOperation() {
		super("BusiOperation"); //$NON-NLS-1$
	}

	public BusiOperation(String name) {
		super(name);
	}

	public Type getOwnType() {
		return ownType;
	}

	public void setOwnType(Type ownType) {
		// this.ownType = ownType;
		Type old = this.ownType;
		this.ownType = ownType;
		firePropertyChange(OWNER_TYPE, old, ownType);
	}

	public boolean isAuthorization() {
		return isAuthorization;
	}

	public void setAuthorization(boolean isAuthorization) {
		boolean old = this.isAuthorization;
		this.isAuthorization = isAuthorization;
		firePropertyChange(AUTHORIZATION, new Boolean(old), new Boolean(
				isAuthorization));
	}

	public boolean isNeedLog() {
		return needLog;
	}

	public void setNeedLog(boolean needLog) {
		boolean old = this.needLog;
		this.needLog = needLog;
		firePropertyChange(SERVICE_NEEDLOG, new Boolean(old), new Boolean(
				needLog));
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public boolean isBusiActivity() {
		return isBusiActivity;
	}

	public void setBusiActivity(boolean isBusiActivity) {
		boolean old = this.isBusiActivity;
		this.isBusiActivity = isBusiActivity;
		firePropertyChange(ISBUSIACTIVITY, new Boolean(old), new Boolean(
				isBusiActivity));
	}

	public void addOperation(RefOperation operation) {
		operations.add(operation);
		fireStructureChange(OPERATION_ADD, operation);
	}

	// public void addOperation(int index, RefOperation operation) {
	// operations.add(index, operation);
	// fireStructureChange(OPERATION_ADD, operation);
	// }

	public void removeOperation(RefOperation operation) {
		operations.remove(operation);
		fireStructureChange(OPERATION_REMOVE, operation);
	}

	public void removeAllOperation() {
		List<RefOperation> toDel = new ArrayList<RefOperation>();
		for (RefOperation operation : operations) {
			fireStructureChange(OPERATION_REMOVE, operation);
		}
		operations.removeAll(toDel);
	}

	public void fireOperationUpdate(RefOperation operation) {
		fireStructureChange(OPERATION_UPDATE, operation);
	}

	public List<RefOperation> getOperations() {
		return operations;
	}

	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		if (getOwnType() != null) {
			getOwnType().setElementAttribute(ele);
		}
		ele.setAttribute("version", getVersion()); //$NON-NLS-1$
		ele.setAttribute("isAuthorization", isAuthorization() ? "true" //$NON-NLS-1$ //$NON-NLS-2$
				: "false"); //$NON-NLS-1$
		ele.setAttribute("isBusiActivity", isBusiActivity() ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ele.setAttribute("logtype", getLogType()); //$NON-NLS-1$
		ele.setAttribute("needlog", isNeedLog() ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append("isAuthorization='") //$NON-NLS-1$
				.append(this.isAuthorization() ? "true" : "false").append("' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sb.append("isBusiActivity='") //$NON-NLS-1$
				.append(this.isBusiActivity() ? "true" : "false").append("' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sb.append("needlog='").append(this.isNeedLog() ? "true" : "false") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		.append("' "); //$NON-NLS-1$
		sb.append("logtype='").append(this.getLogType()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("version='").append(this.getVersion()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		if (getOwnType() != null) {
			sb.append(getOwnType().genXMLAttrString());
		}
		return sb.toString();

	}

	public Element createElement(Document doc, JGraph graph) {
		Element ele = doc.createElement(NODE_NAME);
		ele.setAttribute("componentID", graph.getId()); //$NON-NLS-1$
		this.setElementAttribute(ele);
		//
		Element operListEle = doc.createElement("refoperationlist"); //$NON-NLS-1$
		ele.appendChild(operListEle);
		List<RefOperation> opers = getOperations();
		for (int i = 0; i < opers.size(); i++) {
			operListEle.appendChild(opers.get(i).createElement(doc, false,
					getId()));
		}
		for (int i = 0; i < getAlPropertys().size(); i++) {
			ele.appendChild(getAlPropertys().get(i).createElement(doc));
		}

		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, JGraph graph) {
		pw.print(tabStr + "<" + NODE_NAME); //$NON-NLS-1$
		pw.print(" componentID='" + graph.getId() + "' "); //$NON-NLS-1$ //$NON-NLS-2$
		pw.print(genXMLAttrString());
		pw.println(">"); //$NON-NLS-1$
		pw.println(tabStr + "\t<refoperationlist>"); //$NON-NLS-1$
		List<RefOperation> opers = getOperations();
		for (int i = 0; i < opers.size(); i++) {
			opers.get(i).printXMLString(pw, tabStr + "\t\t", false, getId()); //$NON-NLS-1$
		}
		pw.println(tabStr + "\t</refoperationlist>"); //$NON-NLS-1$
		for (int i = 0; i < getAlPropertys().size(); i++) {
			pw.println(getAlPropertys().get(i).serializedXMLString(
					tabStr + "\t")); //$NON-NLS-1$
		}
		pw.println(tabStr + "</" + NODE_NAME + ">"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected static void parseOperation(Node node, BusiOperation bo) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if ("refoperationlist".equalsIgnoreCase(child.getNodeName())) { //$NON-NLS-1$
				NodeList nl2 = child.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					Node childj = nl2.item(j);
					RefOperation oper = RefOperation.parseNode(childj);
					if (oper != null)
						bo.getOperations().add(oper);
				}
			}
		}

	}

	protected static void parseOperationProperty(Node node, BusiOperation bo) {
		NodeList nl = node.getChildNodes();

		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if (Constant.XML_TYPE_PROPERTY
					.equalsIgnoreCase(child.getNodeName())) {
				XMLElement ele = XMLElement.parseToXmlElement(child);
				bo.getAlPropertys().add(ele);
			}
		}

	}

	protected static void parseNodeAttr(Node node, BusiOperation bo) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			Cell.parseNodeAttr(node, bo);
			// if (map.getNamedItem("implClsName") != null)
			// bo.setImplClsName(map.getNamedItem("implClsName").getNodeValue());
			// if (map.getNamedItem("remote") != null) {
			// String strRemote = map.getNamedItem("remote").getNodeValue();
			// bo.setRemote("true".equalsIgnoreCase(strRemote));
			// }
			// if (map.getNamedItem("singleton") != null) {
			// String strSingleton =
			// map.getNamedItem("singleton").getNodeValue();
			// bo.setSingleton("true".equalsIgnoreCase(strSingleton));
			// }

			if (map.getNamedItem("isAuthorization") != null) { //$NON-NLS-1$
				String strAuthorization = map.getNamedItem("isAuthorization") //$NON-NLS-1$
						.getNodeValue();
				bo.setAuthorization("true".equalsIgnoreCase(strAuthorization)); //$NON-NLS-1$
			}
			if (map.getNamedItem("isBusiOpearation") != null) { //$NON-NLS-1$
				String strIsBusiOpearation = map.getNamedItem(
						"isBusiOpearation").getNodeValue(); //$NON-NLS-1$
				bo.setBusiActivity("true".equalsIgnoreCase(strIsBusiOpearation)); //$NON-NLS-1$
			}

			// if (map.getNamedItem("transprop") != null)
			// bo.setTransProp(map.getNamedItem("transprop").getNodeValue());
			if (map.getNamedItem("version") != null) //$NON-NLS-1$
				bo.setVersion(map.getNamedItem("version").getNodeValue()); //$NON-NLS-1$
			if (map.getNamedItem("needlog") != null) { //$NON-NLS-1$
				String strNeedLog = map.getNamedItem("needlog").getNodeValue(); //$NON-NLS-1$
				bo.setNeedLog("true".equalsIgnoreCase(strNeedLog)); //$NON-NLS-1$
			}
			if (map.getNamedItem("logtype") != null) //$NON-NLS-1$
				bo.setLogType(map.getNamedItem("logtype").getNodeValue()); //$NON-NLS-1$
			Type type = Type.parseType(map);
			if (type != null)
				bo.setOwnType(type);
		}
	}

	public static Cell parseNode(Node node) {
		BusiOperation bo = null;
		String name = node.getNodeName();
		if (NODE_NAME.equalsIgnoreCase(name)) {
			bo = new BusiOperation();
			BusiOperation.parseNodeAttr(node, bo);
			parseOperation(node, bo);
			parseOperationProperty(node, bo);
			XMLSerialize.getInstance().register(bo);
		}
		return bo;
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		String msg = super.validate();
		if (msg != null)
			sb.append(msg + "\r\n"); //$NON-NLS-1$
		ArrayList<String> idList = new ArrayList<String>();
		if (getOperations().size() > 0) {
			for (RefOperation oper : getOperations()) {
				String err = oper.validate();
				if (err != null)
					sb.append(err);
				String id = oper.getId();
				if (!StringUtil.isEmptyWithTrim(id) && !idList.contains(id)) {
					idList.add(id);
				} else {
					sb.append(Messages.BusiOperation_66 + id + "\r\n"); //$NON-NLS-2$
				}
			}
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	public BusiOperation copy() {
		BusiOperation bo = new BusiOperation();
		copy0(bo);
		bo.setNeedLog(this.isNeedLog());
		bo.setLogType(this.getLogType());
		bo.setAuthorization(this.isAuthorization());
		bo.setBusiActivity(this.isBusiActivity());
		bo.setOwnType(this.getOwnType());
		return bo;
	}

//	public IPropertyDescriptor[] getPropertyDescriptors() {
//		IPropertyDescriptor[] desc = super.getPropertyDescriptors();
//		int count = desc == null ? 0 : desc.length;
//		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
//		for (int i = 0; i < count; i++) {
//			if (!desc[i].getId().equals(PROP_VISIBILITY)) {
//				if (desc[i].getId().equals("class_full_name")) { //$NON-NLS-1$
//					// desc[i] = new TextPropertyDescriptor("class_full_name",
//					// "对应的接口类名");
//					// ((TextPropertyDescriptor) desc[i]).setCategory("基本");
//					// 基类中有“类名”这个属性。业务操作 这里跳过，不处理
//					continue;
//				}
//				al.add(desc[i]);
//			}
//		}
//		PropertyDescriptor[] pds = new PropertyDescriptor[6];
//		pds[0] = new CheckboxPropertyDescriptor(AUTHORIZATION, Messages.BusiOperation_69);
//		pds[1] = new CheckboxPropertyDescriptor(ISBUSIACTIVITY, Messages.BusiOperation_70);
//		pds[2] = new TextPropertyDescriptor(IMPL_VERSION, Messages.BusiOperation_71);
//		pds[3] = new TypeSelectePropertyDescriptor(OWNER_TYPE, Messages.BusiOperation_72, null);
//		pds[4] = new CheckboxPropertyDescriptor(SERVICE_NEEDLOG, Messages.BusiOperation_73);
//		pds[5] = new ObjectComboBoxPropertyDescriptor(LOGTYPE, Messages.BusiOperation_74,
//				Constant.LOG_TYPES);
//		
//		pds[0].setLabelProvider(new CheckboxLableProvider());
//		pds[1].setLabelProvider(new CheckboxLableProvider());
//		pds[4].setLabelProvider(new CheckboxLableProvider());
//		for (int i = 0; i < pds.length; i++) {
//			pds[i].setCategory(Messages.BusiOperation_75);
//		}
//		al.addAll(Arrays.asList(pds));
//		return al.toArray(new IPropertyDescriptor[0]);
//	}

	@Override
	public Object getPropertyValue(Object id) {
		if (IMPL_VERSION.equals(id)) {
			return getVersion();
		} else if (AUTHORIZATION.equals(id)) {
			return new Boolean(isAuthorization());
		} else if (ISBUSIACTIVITY.equals(id)) {
			return new Boolean(isBusiActivity());
		} else if (OWNER_TYPE.equals(id)) {
			return getOwnType();
		}else if (SERVICE_NEEDLOG.equals(id)) {
			return new Boolean(isNeedLog());
		} else if (LOGTYPE.equals(id)) {
			return getLogType();
		}  else {
			return super.getPropertyValue(id);
		}
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (IMPL_VERSION.equals(id)) {
			setVersion((String) value);
		} else if (AUTHORIZATION.equals(id)) {
			setAuthorization(((Boolean) value).booleanValue());
		} else if (ISBUSIACTIVITY.equals(id)) {
			setBusiActivity(((Boolean) value).booleanValue());
		} else if (OWNER_TYPE.equals(id)) {
			setOwnType((Type) value);
		}else if (SERVICE_NEEDLOG.equals(id)) {
			setNeedLog(((Boolean) value).booleanValue());
		}  else if (LOGTYPE.equals(id)) {
			setLogType((String) value);
		}  else {
			super.setPropertyValue(id, value);
		}
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ArrayList<XMLElement> getAlPropertys() {
		return alPropertys;
	}

	public HashSet<String> getAllRefOpItfIDs() {
		HashSet<String> idSet = new HashSet<String>();
		for (RefOperation operation : getOperations()) {
			if (!idSet.contains(operation.getOpItfID())) {
				idSet.add(operation.getOpItfID());
			}
		}
		return idSet;
	}

	@Override
	public void dealIncDevForIndustry() {
		setSource(true);
		for (RefOperation refOP : operations) {
			refOP.setSource(true);
		}
	}
}
