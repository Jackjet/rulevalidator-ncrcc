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
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OpInterface extends Cell {
	private static final long serialVersionUID = 3083730638220114276L;

	public static final String OPERATION_ADD = "operation_add"; //$NON-NLS-1$

	public static final String OPERATION_REMOVE = "operation_remove"; //$NON-NLS-1$

	public static final String OPERATION_UPDATE = "operatino_update"; //$NON-NLS-1$

	public static final String IMPL_CLS_NAME = "IMPL_CLS_NAME"; //$NON-NLS-1$

	public static final String IMPL_VERSION = "IMPL_VESION"; //$NON-NLS-1$

	private String implClsName = ""; //$NON-NLS-1$

	private List<Operation> operations = new ArrayList<Operation>();

	private List<OPItfImpl> opItfImpl = new ArrayList<OPItfImpl>();

	//
	public static final String SERVICE_REMOTE = "service_remote"; //$NON-NLS-1$

	public static final String SERVICE_SINGLETON = Messages.OpInterface_7;
	

	public static final String AUTHORIZATION = "authorization"; //$NON-NLS-1$

	public static final String ISBUSIOPRATION = "isbusioperation"; //$NON-NLS-1$

	public static final String ISBUSIACTIVITY = "isbusiactivity"; //$NON-NLS-1$

	public static final String TRANS_PROP = "TRANS_PROP"; //$NON-NLS-1$
	
	public static final String OWNER_TYPE = "OWNER_TYPE"; //$NON-NLS-1$

	private boolean remote = true;

	private boolean singleton = true;

	private boolean isAuthorization = true;

	private boolean isBusiOperation = false;

	private boolean isBusiActivity = false;
	private Type ownType = null;

	private String transProp = Constant.TRANS_PROP_NONE;


	private ArrayList<XMLElement> alPropertys = new ArrayList<XMLElement>();

	// private XMLElement property = new XMLElement(Constant.XML_TYPE_PROPERTY);
	private String version = ""; //$NON-NLS-1$

	/** Industry */
	//
	public OpInterface() {
		super("OpInterface"); //$NON-NLS-1$
	}

	public OpInterface(String name) {
		super(name);
	}

	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		boolean old = this.remote;
		this.remote = remote;
		firePropertyChange(SERVICE_REMOTE, new Boolean(old),
				new Boolean(remote));
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

	public boolean isBusiOperation() {
		return isBusiOperation;
	}

	public void setBusiOperation(boolean isBusiOperation) {
		boolean old = this.isBusiOperation;
		this.isBusiOperation = isBusiOperation;
		firePropertyChange(ISBUSIOPRATION, new Boolean(old), new Boolean(
				isBusiOperation));
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

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		boolean old = this.singleton;
		this.singleton = singleton;
		firePropertyChange(SERVICE_SINGLETON, new Boolean(old), new Boolean(
				singleton));
	}


	public String getTransProp() {
		return transProp;
	}

	public void setTransProp(String transProp) {
		this.transProp = transProp;
	}

	public void addOperation(Operation operation) {
		operations.add(operation);
		fireStructureChange(OPERATION_ADD, operation);
	}

	public void addOperation(int index, Operation operation) {
		operations.add(index, operation);
		fireStructureChange(OPERATION_ADD, operation);
	}

	public void removeOperation(Operation operation) {
		operations.remove(operation);
		fireStructureChange(OPERATION_REMOVE, operation);
	}

	public void fireOperationUpdate(Operation operation) {
		fireStructureChange(OPERATION_UPDATE, operation);
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public Operation getOperationByID(String id) {
		for (Operation opration : operations) {
			if (opration != null && id.equalsIgnoreCase(opration.getId())) {
				return opration;
			}
		}
		return null;
	}

	public HashSet<String> getAllependType() {
		HashSet<String> set = new HashSet<String>();
		if (operations.size() > 0) {
			for (Operation op : operations) {
				Type type = op.getReturnType();
				if (type != null && !NCMDPTool.isBaseType(type.getTypeId())) {
					set.add(type.getTypeId());
				}
			}
		}
		return set;
	}

	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		if (getOwnType() != null) {
			getOwnType().setElementAttribute(ele);
		}
		ele.setAttribute("implClsName", getImplClsName()); //$NON-NLS-1$
		ele.setAttribute("remote", isRemote() ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ele.setAttribute("singleton", isSingleton() ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		ele.setAttribute("isAuthorization", isAuthorization() ? "true" //$NON-NLS-1$ //$NON-NLS-2$
				: "false"); //$NON-NLS-1$
		ele.setAttribute("isBusiOperation", isBusiOperation() ? "true" //$NON-NLS-1$ //$NON-NLS-2$
				: "false"); //$NON-NLS-1$
		ele.setAttribute("isBusiActivity", isBusiActivity() ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ele.setAttribute("transprop", getTransProp()); //$NON-NLS-1$
		ele.setAttribute("version", getVersion()); //$NON-NLS-1$

	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append("implClsName='").append(this.getImplClsName()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("remote='").append(this.isRemote() ? "true" : "false") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				.append("' "); //$NON-NLS-1$
		sb.append("singleton='").append(this.isSingleton() ? "true" : "false") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				.append("' "); //$NON-NLS-1$
		
		sb.append("isAuthorization='") //$NON-NLS-1$
				.append(this.isAuthorization() ? "true" : "false").append("' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sb.append("isBusiOperation='") //$NON-NLS-1$
				.append(this.isBusiOperation() ? "true" : "false").append("' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sb.append("isBusiActivity='") //$NON-NLS-1$
				.append(this.isBusiActivity() ? "true" : "false").append("' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sb.append("transprop='").append(this.getTransProp()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		
		if (getOwnType() != null) {
			sb.append(getOwnType().genXMLAttrString());
		}
		sb.append("version='").append(this.getVersion()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$

		return sb.toString();

	}

	public Element createElement(Document doc, JGraph graph) {
		Element ele = doc.createElement("opinterface"); //$NON-NLS-1$
		ele.setAttribute("componentID", graph.getId()); //$NON-NLS-1$
		this.setElementAttribute(ele);
		//
		Element operListEle = doc.createElement("operationlist"); //$NON-NLS-1$
		ele.appendChild(operListEle);
		List<Operation> opers = getOperations();
		for (int i = 0; i < opers.size(); i++) {
			operListEle.appendChild(opers.get(i).createElement(doc, false,
					getId()));
		}
		Element opImplListEle = doc.createElement("opImpllist"); //$NON-NLS-1$
		ele.appendChild(opImplListEle);
		List<OPItfImpl> opItf = getOpItfImpl();
		for (int i = 0; i < opItf.size(); i++) {
			opImplListEle.appendChild(opItf.get(i).createElement(doc, false,
					getId()));
		}

		for (int i = 0; i < getAlPropertys().size(); i++) {
			ele.appendChild(getAlPropertys().get(i).createElement(doc));
		}

		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, JGraph graph) {
		pw.print(tabStr + "<opinterface "); //$NON-NLS-1$
		pw.print(" componentID='" + graph.getId() + "' "); //$NON-NLS-1$ //$NON-NLS-2$
		pw.print(genXMLAttrString());
		pw.println(">"); //$NON-NLS-1$
		pw.println(tabStr + "\t<operationlist>"); //$NON-NLS-1$
		List<Operation> opers = getOperations();
		for (int i = 0; i < opers.size(); i++) {
			opers.get(i).printXMLString(pw, tabStr + "\t\t", false, getId()); //$NON-NLS-1$
		}
		pw.println(tabStr + "\t</operationlist>"); //$NON-NLS-1$

		pw.println(tabStr + "\t<opImpllist>"); //$NON-NLS-1$
		List<OPItfImpl> opItf = getOpItfImpl();
		for (int i = 0; i < opItf.size(); i++) {
			opItf.get(i).printXMLString(pw, tabStr + "\t\t", false, getId()); //$NON-NLS-1$
		}
		pw.println(tabStr + "\t</opImpllist>"); //$NON-NLS-1$

		// if(getAlPropertys().si){
		// pw.println(getProperty().serializedXMLString(tabStr+"\t"));
		// }
		for (int i = 0; i < getAlPropertys().size(); i++) {
			pw.println(getAlPropertys().get(i).serializedXMLString(
					tabStr + "\t")); //$NON-NLS-1$
		}
		pw.println(tabStr + "</opinterface>"); //$NON-NLS-1$
	}

	protected static void parseOperation(Node node, OpInterface bo) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if ("operationlist".equalsIgnoreCase(child.getNodeName())) { //$NON-NLS-1$
				NodeList nl2 = child.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					Node childj = nl2.item(j);
					Operation oper = Operation.parseNode(childj);
					if (oper != null)
						bo.getOperations().add(oper);
				}
			}
		}

	}

	protected static void parseOpImpl(Node node, OpInterface bo) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if ("opImpllist".equalsIgnoreCase(child.getNodeName())) { //$NON-NLS-1$
				NodeList nl2 = child.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					Node childj = nl2.item(j);
					OPItfImpl opImpl = OPItfImpl.parseNode(childj);
					if (opImpl != null)
						bo.getOpItfImpl().add(opImpl);
				}
			}
		}
	}

	protected static void parseOperationProperty(Node node, OpInterface bo) {
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

	protected static void parseNodeAttr(Node node, OpInterface bo) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			Cell.parseNodeAttr(node, bo);
			if (map.getNamedItem("implClsName") != null) //$NON-NLS-1$
				bo.setImplClsName(map.getNamedItem("implClsName") //$NON-NLS-1$
						.getNodeValue());
			if (map.getNamedItem("remote") != null) { //$NON-NLS-1$
				String strRemote = map.getNamedItem("remote").getNodeValue(); //$NON-NLS-1$
				bo.setRemote("true".equalsIgnoreCase(strRemote)); //$NON-NLS-1$
			}
			if (map.getNamedItem("singleton") != null) { //$NON-NLS-1$
				String strSingleton = map.getNamedItem("singleton") //$NON-NLS-1$
						.getNodeValue();
				bo.setSingleton("true".equalsIgnoreCase(strSingleton)); //$NON-NLS-1$
			}
			
			if (map.getNamedItem("isAuthorization") != null) { //$NON-NLS-1$
				String strAuthorization = map.getNamedItem("isAuthorization") //$NON-NLS-1$
						.getNodeValue();
				bo.setAuthorization("true".equalsIgnoreCase(strAuthorization)); //$NON-NLS-1$
			}
			if (map.getNamedItem("isBusiOperation") != null) { //$NON-NLS-1$
				String strIsBusiOperation = map.getNamedItem("isBusiOperation") //$NON-NLS-1$
						.getNodeValue();
				bo.setBusiOperation("true".equalsIgnoreCase(strIsBusiOperation)); //$NON-NLS-1$
			}
			if (map.getNamedItem("isBusiActivity") != null) { //$NON-NLS-1$
				String strIsBusiActivity = map.getNamedItem("isBusiActivity") //$NON-NLS-1$
						.getNodeValue();
				bo.setBusiActivity("true".equalsIgnoreCase(strIsBusiActivity)); //$NON-NLS-1$
			}
			if (map.getNamedItem("transprop") != null) //$NON-NLS-1$
				bo.setTransProp(map.getNamedItem("transprop").getNodeValue()); //$NON-NLS-1$
			
			if (map.getNamedItem("version") != null) //$NON-NLS-1$
				bo.setVersion(map.getNamedItem("version").getNodeValue()); //$NON-NLS-1$

			Type type = Type.parseType(map);
			if (type != null)
				bo.setOwnType(type);

		}
	}

	public static Cell parseNode(Node node) {
		OpInterface bo = null;

		String name = node.getNodeName();
		if ("opinterface".equalsIgnoreCase(name)) { //$NON-NLS-1$
			bo = new OpInterface();
			OpInterface.parseNodeAttr(node, bo);
			parseOperation(node, bo);
			parseOpImpl(node, bo);
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
		
		String programTag = getGraph().getEndStrForIndustryElement();
		if(needCheckCurNameForPro()){
			if (!getFullClassName().endsWith(programTag)) {
				sb.append(Messages.OpInterface_100 + getName() + "::" + getDisplayName() + Messages.OpInterface_102 //$NON-NLS-2$
						+ programTag + Messages.OpInterface_103);
			}
		}
		
		// 校验实现类
		if (getOpItfImpl().size() > 0) {
			ArrayList<String> opImplName = new ArrayList<String>();
			ArrayList<String> opImplClassName = new ArrayList<String>();
			for (OPItfImpl opImpl : getOpItfImpl()) {
				// 名称
				if (StringUtil.isEmptyWithTrim(opImpl.getName())) {
					sb.append(Messages.OpInterface_104 + opImpl.getName() + "\r\n"); //$NON-NLS-2$
				} else {
					if (opImplName.contains(opImpl.getName())) {
						sb.append(Messages.OpInterface_106 + opImpl.getName() + "\r\n"); //$NON-NLS-2$
					} else {
						opImplName.add(opImpl.getName());
					}
				}
				// 实现类 名称
				if (StringUtil.isEmptyWithTrim(opImpl.getFullClassName())) {
					sb.append(Messages.OpInterface_108 + opImpl.getFullClassName()
							+ "\r\n"); //$NON-NLS-1$
				} else {
					if (opImplClassName.contains(opImpl.getFullClassName())) {
						sb.append(Messages.OpInterface_110 + opImpl.getFullClassName()
								+ "\r\n"); //$NON-NLS-1$
					} else {
						opImplClassName.add(opImpl.getFullClassName());
					}
				}
			}
		}
		// 校验操作
		ArrayList<String> al1 = new ArrayList<String>();
		ArrayList<String> al2 = new ArrayList<String>();
		if (getOperations().size() > 0) {
			Operation oper = getOperations().get(0);
			String err = oper.validate();
			if (err != null)
				sb.append(err);
			al1.add(oper.getName());
			al2.add(oper.getDisplayName());
		}
		for (int i = 1; i < getOperations().size(); i++) {
			Operation oper = getOperations().get(i);
			String err = oper.validate();
			if (err != null)
				sb.append(err);
			String name = oper.getName();
			if (name != null && !al1.contains(name)) {
				al1.add(name);
			} else {
				sb.append(Messages.OpInterface_112 + name + "\r\n"); //$NON-NLS-2$
			}
			String disPlay = oper.getDisplayName();
			if (disPlay != null && !al2.contains(disPlay)) {
				al2.add(disPlay);
			} else {
				sb.append(Messages.OpInterface_114 + disPlay + "\r\n"); //$NON-NLS-2$
			}
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	protected void copy0(OpInterface bo) {
		super.copy0(bo);
		List<Operation> list = getOperations();
		for (int i = 0; i < list.size(); i++) {
			bo.getOperations().add(list.get(i).copy());
		}
		List<OPItfImpl> opImplList = getOpItfImpl();
		for (int i = 0; i < opImplList.size(); i++) {
			bo.getOpItfImpl().add(opImplList.get(i).copy());
		}
		bo.setOwnType(this.getOwnType());
		bo.setRemote(this.isRemote());
		bo.setAuthorization(this.isAuthorization());
		bo.setBusiOperation(this.isBusiOperation());
		bo.setBusiActivity(this.isBusiActivity());
		bo.setSingleton(this.isSingleton());
		
		bo.setTransProp(this.getTransProp());
		
		bo.setImplClsName(this.getImplClsName());
		bo.setVersion(this.getVersion());
	}

	public OpInterface copy() {
		OpInterface bo = new OpInterface();
		copy0(bo);
		return bo;
	}

//	public IPropertyDescriptor[] getPropertyDescriptors() {
//		IPropertyDescriptor[] desc = super.getPropertyDescriptors();
//		int count = desc == null ? 0 : desc.length;
//		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
//		for (int i = 0; i < count; i++) {
//			if (!desc[i].getId().equals(PROP_VISIBILITY)) {
//				if (desc[i].getId().equals("class_full_name")) { //$NON-NLS-1$
//					desc[i] = new TextPropertyDescriptor("class_full_name", //$NON-NLS-1$
//							Messages.OpInterface_118);
//					((TextPropertyDescriptor) desc[i]).setCategory(Messages.OpInterface_119);
//				}
//				al.add(desc[i]);
//			}
//		}
//		PropertyDescriptor[] pds = new PropertyDescriptor[9];
//		pds[0] = new TextPropertyDescriptor(IMPL_CLS_NAME, Messages.OpInterface_120);
//		pds[1] = new CheckboxPropertyDescriptor(SERVICE_REMOTE, Messages.OpInterface_121);
//		pds[2] = new CheckboxPropertyDescriptor(SERVICE_SINGLETON, Messages.OpInterface_122);
//		pds[3] = new CheckboxPropertyDescriptor(AUTHORIZATION, Messages.OpInterface_123);
//		pds[4] = new CheckboxPropertyDescriptor(ISBUSIOPRATION, Messages.OpInterface_124);
//		pds[5] = new CheckboxPropertyDescriptor(ISBUSIACTIVITY, Messages.OpInterface_125);
//		pds[6] = new ObjectComboBoxPropertyDescriptor(TRANS_PROP, Messages.OpInterface_126,
//				Constant.TRANS_PROPS);
//		pds[7] = new TypeSelectePropertyDescriptor(OWNER_TYPE, Messages.OpInterface_127, null);
//		pds[8] = new TextPropertyDescriptor(IMPL_VERSION, Messages.OpInterface_128);
//		
//		
//		
//		
//		pds[1].setLabelProvider(new CheckboxLableProvider());
//		pds[2].setLabelProvider(new CheckboxLableProvider());
//		pds[3].setLabelProvider(new CheckboxLableProvider());
//		pds[4].setLabelProvider(new CheckboxLableProvider());
//		pds[5].setLabelProvider(new CheckboxLableProvider());
//		
//		for (int i = 0; i < pds.length; i++) {
//			pds[i].setCategory(Messages.OpInterface_129);
//		}
//		al.addAll(Arrays.asList(pds));
//		return al.toArray(new IPropertyDescriptor[0]);
//	}

	@Override
	public Object getPropertyValue(Object id) {
		if (IMPL_CLS_NAME.equals(id)) {
			return getImplClsName();
		} else if (SERVICE_REMOTE.equals(id)) {
			return new Boolean(isRemote());
		} else if (SERVICE_SINGLETON.equals(id)) {
			return new Boolean(isSingleton());
		} else if (AUTHORIZATION.equals(id)) {
			return new Boolean(isAuthorization());
		} else if (ISBUSIOPRATION.equals(id)) {
			return new Boolean(isBusiOperation());
		} else if (ISBUSIACTIVITY.equals(id)) {
			return new Boolean(isBusiActivity());
		} else if (OWNER_TYPE.equals(id)) {
			return getOwnType();
		} else if (TRANS_PROP.equals(id)) {
			return getTransProp();
		} else if (IMPL_VERSION.equals(id)) {
			return getVersion();
		} else {
			return super.getPropertyValue(id);
		}
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (IMPL_CLS_NAME.equals(id)) {
			setImplClsName((String) value);
		} else if (SERVICE_REMOTE.equals(id)) {
			setRemote(((Boolean) value).booleanValue());
		} else if (SERVICE_SINGLETON.equals(id)) {
			setSingleton(((Boolean) value).booleanValue());
		} else if (AUTHORIZATION.equals(id)) {
			setAuthorization(((Boolean) value).booleanValue());
		} else if (ISBUSIOPRATION.equals(id)) {
			setBusiOperation(((Boolean) value).booleanValue());
		} else if (ISBUSIACTIVITY.equals(id)) {
			setBusiActivity(((Boolean) value).booleanValue());
		} else if (OWNER_TYPE.equals(id)) {
			setOwnType((Type) value);
		} else if (TRANS_PROP.equals(id)) {
			setTransProp((String) value);
		}else if (IMPL_VERSION.equals(id)) {
			setVersion((String) value);
		} else {
			super.setPropertyValue(id, value);
		}
	}

	public String getImplClsName() {
		return implClsName;
	}

	public void setImplClsName(String implClsName) {
		this.implClsName = implClsName;
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

	@Override
	public void setName(String name) {
		super.setName(name);
		if (getGraph() != null) {
			setFullClassName("nc.itf." + getGraph().getNameSpace() + "." + name); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public String getFullClassName() {
		if (StringUtil.isEmptyWithTrim(getCurFullClassName())) {
			setFullClassName("nc.itf." + getGraph().getNameSpace() + "." //$NON-NLS-1$ //$NON-NLS-2$
					+ getName());
		}
		return getCurFullClassName();
	}

	public List<OPItfImpl> getOpItfImpl() {
		return opItfImpl;
	}

	public void addOPItfImpl(OPItfImpl opImpl) {
		opItfImpl.add(opImpl);
	}

	public void removeOPItfImpl(OPItfImpl opImpl) {
		opItfImpl.remove(opImpl);
	}

	@Override
	public void dealIncDevForIndustry() {
		setSource(true);
	}
}
