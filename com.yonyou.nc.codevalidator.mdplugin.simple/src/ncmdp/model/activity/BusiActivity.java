package ncmdp.model.activity;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * 业务活动
 * 
 * @author dingxm 2009-04-02
 */
public class BusiActivity extends Cell {
	private static final long serialVersionUID = 3265830638220114276L;

	public static final String BUSIOPERATION_ADD = "BUSIACTIVITY_BUSIOPRATION_ADD"; //$NON-NLS-1$

	public static final String BUSIOPERATION_REMOVE = "BUSIACTIVITY_BUSIOPRATION_REMOVE"; //$NON-NLS-1$

	public static final String BUSIOPERATION_UPDATE = "BUSIACTIVITY_BUSIOPRATION_UPDATE"; //$NON-NLS-1$

	private static final String OWNER_TYPE = "BUSIACTIVITY_OWNER_TYPE"; //$NON-NLS-1$

	private List<RefBusiOperation> refbusiOpList = new ArrayList<RefBusiOperation>();

	private ArrayList<XMLElement> alPropertys = new ArrayList<XMLElement>();

	private String version = ""; //$NON-NLS-1$

	private boolean isAuthorization = true;

	private boolean isService = false;

	public static final String IMPL_VERSION = "IMPL_VESION"; //$NON-NLS-1$

	private static final String AUTHORIZATION = "authorization"; //$NON-NLS-1$

	private static final String BUSIACTIVITY_ISSERVICE = "isService"; //$NON-NLS-1$

	private Type ownType = null;

	public BusiActivity() {
		super("BusiActivity"); //$NON-NLS-1$
	}

	public BusiActivity(String name) {
		super(name);
	}

	/****/
	public void addOperation(RefBusiOperation refBusiOp) {
		refbusiOpList.add(refBusiOp);
		fireStructureChange(BUSIOPERATION_ADD, refBusiOp);
	}

	// public void addOperation(int index, RefBusiOperation refBusiOp) {
	// refbusiOpList.add(index, refBusiOp);
	// fireStructureChange(BUSIOPERATION_ADD, refBusiOp);
	// }

	public void removeOperation(RefBusiOperation refBusiOp) {
		refbusiOpList.remove(refBusiOp);
		fireStructureChange(BUSIOPERATION_REMOVE, refBusiOp);
	}

	public void removeAllOperation() {
		List<RefBusiOperation> toDel = new ArrayList<RefBusiOperation>();
		for (RefBusiOperation refBusiOp : refbusiOpList) {
			fireStructureChange(BUSIOPERATION_REMOVE, refBusiOp);
		}
		refbusiOpList.removeAll(toDel);
	}

	public void fireOperationUpdate(RefBusiOperation refBusiOp) {
		fireStructureChange(BUSIOPERATION_UPDATE, refBusiOp);
	}

	public List<RefBusiOperation> getRefBusiOperations() {
		return refbusiOpList;
	}

	/****/
	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		if (getOwnType() != null) {
			getOwnType().setElementAttribute(ele);
		}
		ele.setAttribute("version", getVersion()); //$NON-NLS-1$
		ele.setAttribute("isAuthorization", isAuthorization() ? "true" //$NON-NLS-1$ //$NON-NLS-2$
				: "false"); //$NON-NLS-1$
		ele.setAttribute(BUSIACTIVITY_ISSERVICE, isService() ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append("isAuthorization='") //$NON-NLS-1$
				.append(this.isAuthorization() ? "true" : "false").append("' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sb.append(BUSIACTIVITY_ISSERVICE + "='") //$NON-NLS-1$
				.append(this.isService() ? "true" : "false").append("' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sb.append("version='").append(this.getVersion()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		if (getOwnType() != null) {
			sb.append(getOwnType().genXMLAttrString());
		}
		return sb.toString();

	}

	/**
	 * start
	 */
	public Element createElement(Document doc, JGraph graph) {
		Element ele = doc.createElement("busiActivity"); //$NON-NLS-1$
		ele.setAttribute("componentID", graph.getId()); //$NON-NLS-1$
		this.setElementAttribute(ele);
		//
		Element operListEle = doc.createElement("refBusiOperationlist"); //$NON-NLS-1$
		ele.appendChild(operListEle);
		List<RefBusiOperation> opers = getRefBusiOperations();
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
		pw.print(tabStr + "<busiActivity "); //$NON-NLS-1$
		pw.print(" componentID='" + graph.getId() + "' "); //$NON-NLS-1$ //$NON-NLS-2$
		pw.print(genXMLAttrString());
		pw.println(">"); //$NON-NLS-1$
		pw.println(tabStr + "\t<refBusiOperationlist>"); //$NON-NLS-1$
		List<RefBusiOperation> opers = getRefBusiOperations();
		for (int i = 0; i < opers.size(); i++) {
			opers.get(i).printXMLString(pw, tabStr + "\t\t", false, getId()); //$NON-NLS-1$
		}
		pw.println(tabStr + "\t</refBusiOperationlist>"); //$NON-NLS-1$
		for (int i = 0; i < getAlPropertys().size(); i++) {
			pw.println(getAlPropertys().get(i).serializedXMLString(
					tabStr + "\t")); //$NON-NLS-1$
		}
		pw.println(tabStr + "</busiActivity>"); //$NON-NLS-1$
	}

	private static void parseActivity(Node node, BusiActivity bo) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if ("refBusiOperationlist".equalsIgnoreCase(child.getNodeName())) { //$NON-NLS-1$
				NodeList nl2 = child.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					Node childj = nl2.item(j);
					RefBusiOperation oper = RefBusiOperation.parseNode(childj);
					if (oper != null)
						bo.getRefBusiOperations().add(oper);
				}
			}
		}

	}

	protected static void parseOperationProperty(Node node, BusiActivity bo) {
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

	protected static void parseNodeAttr(Node node, BusiActivity bo) {
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
			if (map.getNamedItem(BUSIACTIVITY_ISSERVICE) != null) {
				String service = map.getNamedItem(BUSIACTIVITY_ISSERVICE)
						.getNodeValue();
				bo.setService("true".equalsIgnoreCase(service)); //$NON-NLS-1$
			}

			// if (map.getNamedItem("transprop") != null)
			// bo.setTransProp(map.getNamedItem("transprop").getNodeValue());
			if (map.getNamedItem("version") != null) //$NON-NLS-1$
				bo.setVersion(map.getNamedItem("version").getNodeValue()); //$NON-NLS-1$

			Type type = Type.parseType(map);
			if (type != null) {
				bo.setOwnType(type);
			}
		}
	}

	public static Cell parseNode(Node node) {
		BusiActivity bo = null;
		String name = node.getNodeName();
		if ("BusiActivity".equalsIgnoreCase(name)) { //$NON-NLS-1$
			bo = new BusiActivity();
			BusiActivity.parseNodeAttr(node, bo);
			parseActivity(node, bo);
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
		if (getRefBusiOperations().size() > 0) {
			for (RefBusiOperation oper : getRefBusiOperations()) {
				String err = oper.validate();
				if (err != null)
					sb.append(err);
				String id = oper.getId();
				if (!StringUtil.isEmptyWithTrim(id) && !idList.contains(id)) {
					idList.add(id);
				} else {
					sb.append(Messages.BusiActivity_46 + id + "\r\n"); //$NON-NLS-2$
				}
			}
		}
		// 如果是服务，来自于同一个接口 校验
		if (isService()) {
			List<BusiOperation> busiOPList = getBusiOp();
			String curOPItfid = null;
			boolean isSame = true;
			for (BusiOperation busiOP : busiOPList) {
				if (!isSame) {
					sb.append(Messages.BusiActivity_48
							+ getDisplayName() + "\r\n"); //$NON-NLS-1$
					break;
				}
				List<RefOperation> refOpList = busiOP.getOperations();
				for (RefOperation refOp : refOpList) {
					if (StringUtil.isEmptyWithTrim(curOPItfid)) {
						curOPItfid = refOp.getOpItfID();
					} else {
						if (!curOPItfid.equalsIgnoreCase(refOp.getOpItfID())) {
							isSame = false;
							break;
						}
					}
				}
			}
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	/**
	 * 获得本活动引用的所有的业务操作
	 * 
	 * @return
	 */
	public List<BusiOperation> getBusiOp() {
		List<RefBusiOperation> refbusiOPList = getRefBusiOperations();
		List<BusiOperation> busiOPList = getGraph().getAllBusiOperation();
		List<BusiOperation> ownerBusiOPList = new ArrayList<BusiOperation>();
		Set<String> bsiopIDSet = new HashSet<String>();
		for (RefBusiOperation refbusiOP : refbusiOPList) {// 数量很少，直接循环
			String busiopID = refbusiOP.getRealBusiOpid();
			if (bsiopIDSet.contains(busiopID)) {
				continue;
			}
			for (BusiOperation busiOP : busiOPList) {
				if (busiopID.equalsIgnoreCase(busiOP.getId())) {
					bsiopIDSet.add(busiopID);
					ownerBusiOPList.add(busiOP);
					continue;
				}
			}
		}
		return ownerBusiOPList;
	}

	// public List<RefOperation> getBusiOp(){
	// List<RefOperation> operations
	// }

	public BusiActivity copy() {
		BusiActivity bo = new BusiActivity();
		copy0(bo);
		bo.setAuthorization(this.isAuthorization());
		bo.setService(this.isService());
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
//		PropertyDescriptor[] pds = new PropertyDescriptor[4];
//		pds[0] = new CheckboxPropertyDescriptor(AUTHORIZATION, Messages.BusiActivity_51);
//		pds[1] = new CheckboxPropertyDescriptor(BUSIACTIVITY_ISSERVICE, Messages.BusiActivity_52);
//		pds[2] = new TextPropertyDescriptor(IMPL_VERSION, Messages.BusiActivity_53);
//		pds[3] = new TypeSelectePropertyDescriptor(OWNER_TYPE, Messages.BusiActivity_54, null);
//		pds[0].setLabelProvider(new CheckboxLableProvider());
//		pds[1].setLabelProvider(new CheckboxLableProvider());
//		for (int i = 0; i < pds.length; i++) {
//			pds[i].setCategory(Messages.BusiActivity_55);
//		}
//		al.addAll(Arrays.asList(pds));
//		return al.toArray(new IPropertyDescriptor[0]);
//	}

	/**
	 * end***************************
	 */
	@Override
	public Object getPropertyValue(Object id) {
		if (IMPL_VERSION.equals(id)) {
			return getVersion();
		} else if (AUTHORIZATION.equals(id)) {
			return new Boolean(isAuthorization());
		} else if (BUSIACTIVITY_ISSERVICE.equals(id)) {
			return new Boolean(isService());
		} else if (OWNER_TYPE.equals(id)) {
			return getOwnType();
		} else {
			return super.getPropertyValue(id);
		}
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (IMPL_VERSION.equals(id)) {
			setVersion((String) value);
		} else if (AUTHORIZATION.equals(id)) {
			setAuthorization(((Boolean) value).booleanValue());
		} else if (BUSIACTIVITY_ISSERVICE.equals(id)) {
			setService(((Boolean) value).booleanValue());
		} else if (OWNER_TYPE.equals(id)) {
			setOwnType((Type) value);
		} else {
			super.setPropertyValue(id, value);
		}
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

	public boolean isService() {
		return isService;
	}

	public void setService(boolean isService) {
		boolean old = this.isService;
		this.isService = isService;
		firePropertyChange(BUSIACTIVITY_ISSERVICE, new Boolean(old),
				new Boolean(isService));
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

	public Type getOwnType() {
		return ownType;
	}

	public void setOwnType(Type ownType) {
		// this.ownType = ownType;
		Type old = this.ownType;
		this.ownType = ownType;
		firePropertyChange(OWNER_TYPE, old, ownType);
	}

	@Override
	public void dealIncDevForIndustry() {
		setSource(true);

		for (RefBusiOperation refBusiOp : refbusiOpList) {
			refBusiOp.setSource(true);
		}
	}
}
