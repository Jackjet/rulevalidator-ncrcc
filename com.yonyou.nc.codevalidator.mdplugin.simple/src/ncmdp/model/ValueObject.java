package ncmdp.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ncmdp.model.Accessor.AccProp;
import ncmdp.model.activity.Operation;
import ncmdp.serialize.XMLSerialize;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 实体的父类
 * @author wangxmn
 *
 */
public class ValueObject extends Cell {
	private static final long serialVersionUID = -251744362621096597L;

	public static final String PROP_ADD_CELL_PROPS = "add_cell_props"; 

	public static final String PROP_REMOVE_CELL_PROPS = "remove_cell_props"; 

	public static final String PROP_UPDATE_CELL_PROPS = "update_cell_props"; 

	public static final String ADD_OPERATION = "add_operation"; 

	public static final String REMOVE_OPERATION = "remove_operation"; 

	public static final String UPDATE_OPERATION = "update_operation"; 

	public static final String PROP_BEAN_TYPE = "bean_type"; 

	public static final String STEREO_TYPE = "class_type"; 

	public static final String PROP_TABLENAME = "table_name"; 

	public static final String PROP_SUPER_ENTITY = "SUPER_ENTITY"; 

	public static final String ADD_BUSIITF = "add_busiitf"; 

	public static final String REMOVE_BUSIITF = "remove_busiitf"; 

	public static final String PROP_IS_AUTHEN = "prop_is_authen"; 

	public static final String PROP_BUSIITFIMPLCLSNAME = "prop_busiItfImplClsName"; 

	public static final String PROP_IS_CREATESQL = "prop_is_createsql"; 

	public static final String ENTITY_IS_EXTEND = "entity_is_extend"; 

	public static final String USER_DEF_CLSNAME = "userDef_clsName"; 

	private List<Attribute> props = new ArrayList<Attribute>();

	private List<Operation> alOperations = new ArrayList<Operation>();

	// private Accessor[] accessors = NCMDPTool.getAccessors();
	// private StereoType stereoType = null;

	private List<StereoType> stereoTypeList = null;

	private Accessor access = null;

	private String tableName = ""; 

	private ValueObject superEntity = null;

	private Boolean isAuthen = Boolean.TRUE;

	private List<BusinInterface> alBusiItfs = new ArrayList<BusinInterface>();

	private HashMap<String, Attribute> hmBusiattrAttrMap = new HashMap<String, Attribute>();

	private HashMap<String, Attribute[]> hmBusiattrAttrExtendMap = new HashMap<String, Attribute[]>();

	private ArrayList<CanZhao> alCanzhao = new ArrayList<CanZhao>();

	private Boolean isCreateSQL = Boolean.TRUE;

	private Boolean isExtendBean = Boolean.FALSE;

	private String busiItfImplClsName = ""; 

	private String userDefClassName = ""; 

	public ValueObject(String name) {
		super(name);
	}

	public ValueObject() {
		super("ValueObject"); 
	}

	private Accessor[] allUserableAccessors = null;

	private Accessor[] getUseableAccessors() {
		// 取交集？
		if (getStereoTypeList() != null && getStereoTypeList().size() > 0) {
			return NCMDPTool.getUseableAccessors(getStereoTypeList()).toArray(
					new Accessor[] {});
		} else {
			if (allUserableAccessors == null) {
				allUserableAccessors = NCMDPTool.getAccessors();
			}
			return allUserableAccessors;
		}
	}

	public Accessor getAccess() {

		if (access == null) {
			Accessor[] accessors = getUseableAccessors();
			if (accessors != null && accessors.length > 0) {
				return accessors[0];
			}
		}
		return access;
	}

	public void setAccess(Accessor access) {
		this.access = access;
	}

	public void addBusiItf(BusinInterface itf) {
		if (!alBusiItfs.contains(itf)) {
			alBusiItfs.add(itf);
			fireStructureChange(ADD_BUSIITF, itf);
		}
	}

	public void removeBusiItf(BusinInterface itf) {
		alBusiItfs.remove(itf);
		fireStructureChange(REMOVE_BUSIITF, itf);
	}

	public List<BusinInterface> getBusiItfs() {
		return alBusiItfs;
	}

	public void setBusiattrAttrMap(BusiItfAttr busiattr, Attribute attr) {
		hmBusiattrAttrMap.put(busiattr.getId(), attr);
	}

	HashMap<String, Attribute> getBusiattrAttrMap() {
		return hmBusiattrAttrMap;
	}

	public Attribute getBusiattrAtrrMap(BusiItfAttr busiattr) {
		return hmBusiattrAttrMap.get(busiattr.getId());
	}

	public void setBusiattrAttrExtendMap(BusiItfAttr busiattr,
			Attribute[] attrs, String property) {
		Attribute[] atts = hmBusiattrAttrExtendMap.get(busiattr.getId());
		if (atts != null) {
//			if (property
//					.equals(ncmdp.views.BusinessItfAttrsMapModifier.colNames[2])) {
//				atts[0] = attrs[0];
//			} else if (property
//					.equals(ncmdp.views.BusinessItfAttrsMapModifier.colNames[3])) {
//				atts[1] = attrs[1];
//			}
			if (atts[0] != null) {
				if (!atts[0].getTypeStyle().equals("REF") 
						&& !atts[0].getTypeStyle().equals("ARRAY")) { 
					atts[1] = null;
			} else {
			}
				atts[1] = null;
			}
			hmBusiattrAttrExtendMap.put(busiattr.getId(), atts);
		} else {
			hmBusiattrAttrExtendMap.put(busiattr.getId(), attrs);
		}
	}

	public HashMap<String, Attribute[]> getBusiattrAttrExtendMap() {
		return hmBusiattrAttrExtendMap;
	}

	public Attribute[] getBusiattrAtrrExtendMap(BusiItfAttr busiattr) {
		return hmBusiattrAttrExtendMap.get(busiattr.getId());
	}

	public void addOperation(Operation operation) {
		alOperations.add(operation);
		fireStructureChange(ADD_OPERATION, operation);
	}

	public void addOperation(int index, Operation operation) {
		alOperations.add(index, operation);
		fireStructureChange(ADD_OPERATION, operation);
	}

	public void removeOperation(Operation operation) {
		alOperations.remove(operation);
		fireStructureChange(REMOVE_OPERATION, operation);
	}

	public void fireOperationUpdate(Operation operation) {
		fireStructureChange(UPDATE_OPERATION, operation);
	}

	public List<Operation> getOperations() {
		return alOperations;
	}

	public ValueObject getSuperEntity() {
		return superEntity;
	}

	public void setSuperEntity(ValueObject superEntity) {
		this.superEntity = superEntity;
	}

	public List<Attribute> getProps() {
		return props;
	}

	public void addProp(Attribute prop) {
		props.add(prop);
		fireStructureChange(PROP_ADD_CELL_PROPS, prop);
	}

	public void addProp(int index, Attribute prop) {
		props.add(index, prop);
		fireStructureChange(PROP_ADD_CELL_PROPS, prop);
	}

	public void removeProp(Attribute prop) {
		props.remove(prop);
		List<Connection> sourConns = getSourceConnections();
		for (int i = 0; i < sourConns.size(); i++) {
			Connection con = sourConns.get(i);
			if (con instanceof Relation) {
				Relation relation = (Relation) con;
				if (prop.equals(relation.getSrcAttribute())) {
					relation.setSourcAttribute(null);
				}
			}
		}
		fireStructureChange(PROP_REMOVE_CELL_PROPS, prop);
	}

	public void firePropUpdate(Attribute prop) {
		fireStructureChange(PROP_UPDATE_CELL_PROPS, prop);
	}

	public Attribute getAttrById(String id) {
		Attribute attr = null;
		List<Attribute> list = getProps();
		for (int i = 0; i < list.size(); i++) {
			Attribute temp = list.get(i);
			if (temp.getId().equals(id)) {
				attr = temp;
				break;
			}
		}
		return attr;
	}

	public Attribute getAttrExtendById(Attribute attrExtend, String id) {
		Attribute attr = null;
		if (attrExtend != null) {
			if (attrExtend.getType().getCell() instanceof Entity) {
				Entity entity = (Entity) attrExtend.getType().getCell();
				if (entity == null) {
					return null;
				}
				List<Attribute> list = entity.getProps();
				for (int i = 0; i < list.size(); i++) {
					Attribute temp = list.get(i);
					if (temp.getId().equals(id)) {
						attr = temp;
						break;
					}
				}
			} else if (attrExtend.getType().getCell() instanceof Enumerate) {

			}
		}
		return attr;
	}

//	@Override
//	public IPropertyDescriptor[] getPropertyDescriptors() {
//		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
//		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
//		Accessor acc = getAccess();
//		int propcount = acc == null ? 0 : getAccess().getPropmap().size();
//		PropertyDescriptor[] pds = new PropertyDescriptor[9 + propcount];
//		// pds[0]= new ObjectComboBoxPropertyDescriptor("class_type","类别",
//		// StereoTypeConfig.getStereoTypes());
//		pds[0] = new StereoTypeSelectDescriptor(STEREO_TYPE, Messages.ValueObject_23,
//				getStereoTypeList());
//		pds[0].setCategory(Messages.ValueObject_24);
//		pds[1] = new ObjectComboBoxPropertyDescriptor(PROP_BEAN_TYPE, Messages.ValueObject_25,
//				getUseableAccessors());
//		pds[1].setCategory(Messages.ValueObject_26);
//		if (propcount > 0) {
//			HashMap<String, AccProp> hm = acc.getPropmap();
//			String[] keys = hm.keySet().toArray(new String[0]);
//			for (int i = 0; i < keys.length; i++) {
//				AccProp prop = hm.get(keys[i]);
//				pds[i + 2] = new TextPropertyDescriptor(keys[i],
//						prop.getDisplayName());
//				pds[i + 2].setCategory(Messages.ValueObject_27);
//
//			}
//		}
//		pds[propcount + 2] = new NotEditableTextPropertyDescriptor(
//				"prop_cls_name", Messages.ValueObject_29); 
//		pds[propcount + 2].setCategory(Messages.ValueObject_30);
//		pds[propcount + 3] = new TextPropertyDescriptor(PROP_TABLENAME, Messages.ValueObject_31);
//		pds[propcount + 3].setCategory(Messages.ValueObject_32);
//		// pds[propcount+4] = new CheckboxPropertyDescriptor(PROP_IS_AUTHEN,
//		// "是否授权");
//		pds[propcount + 4] = new ObjectComboBoxPropertyDescriptor(
//				PROP_IS_AUTHEN, Messages.ValueObject_33, new Boolean[] { Boolean.TRUE,
//						Boolean.FALSE });
//		pds[propcount + 4].setCategory(Messages.ValueObject_34);
//		pds[propcount + 5] = new TextPropertyDescriptor(
//				PROP_BUSIITFIMPLCLSNAME, Messages.ValueObject_35);
//		pds[propcount + 5].setCategory(Messages.ValueObject_36);
//		pds[propcount + 6] = new TextPropertyDescriptor(USER_DEF_CLSNAME,
//				Messages.ValueObject_37);
//		pds[propcount + 6].setCategory(Messages.ValueObject_38);
//		pds[propcount + 7] = new ObjectComboBoxPropertyDescriptor(
//				PROP_IS_CREATESQL, Messages.ValueObject_39, new Boolean[] { Boolean.TRUE,
//						Boolean.FALSE });
//		pds[propcount + 7].setCategory(Messages.ValueObject_40);
//		pds[propcount + 8] = new ObjectComboBoxPropertyDescriptor(
//				ENTITY_IS_EXTEND, Messages.ValueObject_41, new Boolean[] { Boolean.TRUE,
//						Boolean.FALSE });
//		pds[propcount + 8].setCategory(Messages.ValueObject_42);
//
//		al.addAll(Arrays.asList(pds));
//		return al.toArray(new IPropertyDescriptor[0]);
//	}

	private boolean isAccessParam(Object id) {
		if (getAccess() == null) {
			return false;
		} else {
			return getAccess().getPropmap().containsKey(id);
		}
	}

	public String getCellDefaultAggFullClassName() {
		String clsFullName = getFullClassName();
		int index = clsFullName.lastIndexOf('.');
		return clsFullName.substring(0, index + 1) + "Agg" 
				+ clsFullName.substring(index + 1);

	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROP_BEAN_TYPE.equals(id)) {
			Accessor acc = getAccess();
			// Accessor[] accessors = getUseableAccessors();
			// int count = accessors == null ? 0 : accessors.length;
			// for (int i = 0; i < count; i++) {
			// if(acc.getName().equals(accessors[i].getName())){
			// return accessors[i];
			// }
			// }
			return acc;
		} else if (PROP_TABLENAME.equals(id)) {
			return getTableName();
		} else if (PROP_IS_AUTHEN.equals(id)) {
			return isAuthen();
		} else if (PROP_IS_CREATESQL.equals(id)) {
			return isCreateSQL();
		} else if (ENTITY_IS_EXTEND.equals(id)) {
			return isExtendBean();
		}

		else if (PROP_SUPER_ENTITY.equals(id)) {
			return superEntity == null ? "" : superEntity.getDisplayName(); 
		} else if ("prop_cls_name".equals(id)) { 
			return getFullClassName();
		} else if (isAccessParam(id)) {
			AccProp prop = getAccess().getPropmap().get(id);
			if (prop != null && Accessor.WRAPCLSNAME.equals(id)) {
				String value = prop.getValue();
				if (value == null || value.trim().length() == 0) {
					return getCellDefaultAggFullClassName();
				}
			}
			return prop == null ? "" : prop.getValue(); 
		} else if (STEREO_TYPE.equals(id)) {
			return getStereoTypeList();
		} else if (PROP_BUSIITFIMPLCLSNAME.equals(id)) {
			return getBusiItfImplClsName();
		} else if (USER_DEF_CLSNAME.equals(id)) {
			return getUserDefClsName();
		} else
			return super.getPropertyValue(id);
	}

	@Override
	public boolean isPropertySet(Object id) {
		if (PROP_ELEMENT_NAME.equals(id))
			return true;
		return super.isPropertySet(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPropertyValue(Object id, Object value) {
		if (PROP_BEAN_TYPE.equals(id)) {
			setAccess((Accessor) value);
		} else if (PROP_TABLENAME.equals(id)) {
			setTableName((String) value);
		} else if (PROP_IS_AUTHEN.equals(id)) {
			setAuthen((Boolean) value);
		} else if (PROP_IS_CREATESQL.equals(id)) {
			setCreateSQL((Boolean) value);
		} else if (ENTITY_IS_EXTEND.equals(id)) {
			setIsExtendBean((Boolean) value);
		} else if (STEREO_TYPE.equals(id)) {
			List<StereoType> st = (List<StereoType>) value;
			setStereoTypeList(st);
			if (st != null && st.size() > 0) {
				if (NCMDPTool.getUseableAccessors(st).size() > 0) {
					setAccess(NCMDPTool.getUseableAccessors(st).get(0));
				} else {
					setAccess(null);
				}
			}
		} else if (isAccessParam(id)) {
			AccProp prop = getAccess().getPropmap().get(id);
			prop.setValue((String) value);
		} else if (PROP_BUSIITFIMPLCLSNAME.equals(id)) {
			setBusiItfImplClsName((String) value);
		} else if (USER_DEF_CLSNAME.equals(id)) {
			setUserDefClsName((String) value);
		} else
			super.setPropertyValue(id, value);
	}

	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		ele.setAttribute("stereoType", 
				NCMDPTool.getStrByStereoTypeList(getStereoTypeList()));
		ele.setAttribute("tableName", getTableName()); 
		ele.setAttribute("isAuthen", isAuthen() ? "true" : "false");   
		ele.setAttribute("isCreateSQL", isCreateSQL() ? "true" : "false");   
		ele.setAttribute("isExtendBean", isExtendBean() ? "true" : "false");   
		ele.setAttribute("czlist", getCanzhaoListStr()); 
		ele.setAttribute("bizItfImpClassName", getBusiItfImplClsName()); 
		ele.setAttribute("userDefClassName", getUserDefClsName()); 
	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append("stereoType='") 
				.append(NCMDPTool.getStrByStereoTypeList(getStereoTypeList()))
				.append("' "); 
		sb.append("tableName='").append(this.getTableName()).append("' ");  
		sb.append("isAuthen='").append(this.isAuthen() ? "true" : "false")   
				.append("' "); 
		sb.append("isCreateSQL='") 
				.append(this.isCreateSQL() ? "true" : "false").append("' ");   
		sb.append("isExtendBean='") 
				.append(this.isExtendBean() ? "true" : "false").append("' ");   

		sb.append("czlist='").append(getCanzhaoListStr()).append("' ");  
		return sb.toString();

	}

	private String getCanzhaoListStr() {
		CanZhao def = null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getAlCanzhao().size(); i++) {
			CanZhao cz = getAlCanzhao().get(i);
			if (cz.isDefault()) {
				def = cz;
			} else {
				if (sb.length() > 0)
					sb.append(";"); 
				// sb.append(cz.getName());
				sb.append(XMLUtil.getXMLString(cz.getName()));
			}
		}
		String str = null;
		if (def != null) {
			str = def.getName();
			if (sb.length() > 0)
				str += ";" + sb.toString(); 
		} else {
			str = sb.toString();
		}
		return str;
	}

	protected void appendCanzhaoEle(Document doc, Element parent) {
		Element czlistEle = doc.createElement("canzhaolist"); 
		parent.appendChild(czlistEle);
		List<CanZhao> list = getAlCanzhao();
		for (int i = 0; i < list.size(); i++) {
			czlistEle.appendChild(list.get(i).createElement(doc));
		}

	}

	protected void printCanzhao(PrintWriter pw, String tabStr) {
		pw.println(tabStr + "\t<canzhaolist>"); 
		List<CanZhao> list = getAlCanzhao();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).printXMLString(pw, tabStr + "\t\t"); 
		}
		pw.println(tabStr + "\t</canzhaolist>"); 
	}

	protected void appendAttrAndOperElement(Document doc, Element parent) {
		Element attrListEle = doc.createElement("attributelist"); 
		parent.appendChild(attrListEle);
		List<Attribute> attrs = getProps();
		for (int i = 0; i < attrs.size(); i++) {
			attrListEle
					.appendChild(attrs.get(i).createElement(doc, i, getId()));
		}

		Element operListEle = doc.createElement("operationlist"); 
		parent.appendChild(operListEle);
		List<Operation> opers = getOperations();
		for (int i = 0; i < opers.size(); i++) {
			operListEle.appendChild(opers.get(i).createElement(doc, true,
					getId()));
		}
	}

	protected void printAttrAndOper(PrintWriter pw, String tabStr) {
		pw.println(tabStr + "\t<attributelist>"); 
		List<Attribute> attrs = getProps();
		for (int i = 0; i < attrs.size(); i++) {
			attrs.get(i).printXMLString(pw, tabStr + "\t\t", i, getId()); 
		}
		pw.println(tabStr + "\t</attributelist>"); 

		pw.println(tabStr + "\t<operationlist>"); 
		List<Operation> opers = getOperations();
		for (int i = 0; i < opers.size(); i++) {
			opers.get(i).printXMLString(pw, tabStr + "\t\t", true, getId()); 
		}
		pw.println(tabStr + "\t</operationlist>"); 

	}

	protected void appendBusiItfsAndAttrMapElement(Document doc, Element parent) {
		Element busiitfsEle = doc.createElement("busiitfs"); 

		parent.appendChild(busiitfsEle);
		List<BusinInterface> itfs = getBusiItfs();
		for (int i = 0; i < itfs.size(); i++) {
			BusinInterface itf = itfs.get(i);
			Element itfidEle = doc.createElement("itfid"); 
			itfidEle.appendChild(doc.createTextNode(itf.getId()));
			busiitfsEle.appendChild(itfidEle);
		}
		Element busimapsEle = doc.createElement("busimaps"); 
		parent.appendChild(busimapsEle);
		HashMap<String, Attribute[]> hm = getBusiattrAttrExtendMap();

		for (int i = 0; i < itfs.size(); i++) {
			BusinInterface itf = itfs.get(i);
			List<BusiItfAttr> busiAttrs = itf.getBusiItAttrs();
			for (int j = 0; j < busiAttrs.size(); j++) {
				Element tempEle = doc.createElement("busimap"); 
				busimapsEle.appendChild(tempEle);
				BusiItfAttr busiattr = busiAttrs.get(j);
				String key = busiattr.getId();
				Attribute[] attrs = hm.get(key);
				tempEle.setAttribute("busiitfattrid", key); 
				tempEle.setAttribute("busiitfid", itf.getId()); 
				tempEle.setAttribute("attrid", attrs == null 
						|| attrs[0] == null ? "" : attrs[0].getId()); 
				if (attrs != null && attrs[0] != null & attrs[1] != null) {
					tempEle.setAttribute("attrpath", attrs[0].getName() + "."  
							+ attrs[1].getName());
					tempEle.setAttribute("attrpathid", attrs[1].getId()); 
				} else {
					tempEle.setAttribute("attrpath", "");  
					tempEle.setAttribute("attrpathid", "");  
				}
				tempEle.setAttribute("cellid", getId()); 
			}
		}

	}

	protected void printBusiItfsAndAttrMap(PrintWriter pw, String tabStr) {
		pw.println(tabStr + "\t<busiitfs>"); 
		List<BusinInterface> itfs = getBusiItfs();
		for (int i = 0; i < itfs.size(); i++) {
			BusinInterface itf = itfs.get(i);
			pw.print(tabStr + "\t\t<itfid>"); 
			pw.print(itf.getId());
			pw.println("</itfid>"); 
		}
		pw.println(tabStr + "\t</busiitfs>"); 
		pw.println(tabStr + "\t<busimaps>"); 
		HashMap<String, Attribute> hm = getBusiattrAttrMap();
		for (int i = 0; i < itfs.size(); i++) {
			BusinInterface itf = itfs.get(i);
			List<BusiItfAttr> busiAttrs = itf.getBusiItAttrs();
			for (int j = 0; j < busiAttrs.size(); j++) {
				BusiItfAttr busiattr = busiAttrs.get(j);
				String key = busiattr.getId();
				Attribute attr = hm.get(key);
				pw.print(tabStr + "\t\t<busimap busiitfattrid='"); 
				pw.print(key);
				pw.print("' busiitfid='"); 
				pw.print(itf.getId());
				pw.print("' attrid='"); 
				pw.print(attr == null ? "" : attr.getId()); 
				pw.print("' cellid='"); 
				pw.print(this.getId());
				pw.println("' />"); 
			}
		}

		pw.println(tabStr + "\t</busimaps>"); 
	}

	public Element createElement(Document doc, JGraph graph) {
		Element ele = doc.createElement("valueobject"); 
		String accClsName = getAccess() == null ? "" : getAccess() 
				.getClsFullName();
		// pw.print(" accessorClassName='"+accClsName+"' ");
		ele.setAttribute("accessorClassName", accClsName); 
		ele.setAttribute("componentID", graph.getId()); 
		// pw.print(" componentID='"+graph.getId()+"' ");
		setElementAttribute(ele);
		// pw.print(genXMLAttrString());
		// pw.println(">");
		// printAttrAndOper(pw, tabStr+"\t");
		appendAttrAndOperElement(doc, ele);
		// printBusiItfsAndAttrMap(pw, tabStr+"\t");
		appendBusiItfsAndAttrMapElement(doc, ele);
		// printCanzhao(pw, tabStr+"\t");
		appendCanzhaoEle(doc, ele);
		if (getAccess() != null) {
			ele.appendChild(getAccess().createElement(doc, this));
		}
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, JGraph graph) {
		pw.print(tabStr + "<valueobject "); 
		String accClsName = getAccess() == null ? "" : getAccess() 
				.getClsFullName();
		pw.print(" accessorClassName='" + accClsName + "' ");  
		pw.print(" componentID='" + graph.getId() + "' ");  
		pw.print(genXMLAttrString());
		pw.println(">"); 
		printAttrAndOper(pw, tabStr + "\t"); 
		printBusiItfsAndAttrMap(pw, tabStr + "\t"); 
		printCanzhao(pw, tabStr + "\t"); 
		if (getAccess() != null)
			getAccess().printXMLString(pw, tabStr + "\t", this); 
		pw.println(tabStr + "</valueobject>"); 
	}

	protected static void parseOperationList(Node node, ValueObject vo) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if ("operationlist".equalsIgnoreCase(child.getNodeName())) { 
				NodeList nl2 = child.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					Node nn = nl2.item(j);
					Operation opr = Operation.parseNode(nn);
					if (opr != null)
						vo.getOperations().add(opr);
				}
			}
		}
	}

	public static void parseNodeAttr(Node node, ValueObject vo) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			Cell.parseNodeAttr(node, vo);
			// vo.setBeanType(map.getNamedItem("beantype").getNodeValue());
			if (map.getNamedItem("tableName") != null) { 
				vo.setTableName(map.getNamedItem("tableName").getNodeValue()); 
			}
			if (map.getNamedItem("stereoType") != null) { 
				String stNameStr = map.getNamedItem("stereoType") 
						.getNodeValue();
				List<StereoType> stList = NCMDPTool
						.getStereoTypeListFromString(stNameStr);
				vo.setStereoTypeList(stList);
			}
			if (map.getNamedItem("isAuthen") != null) { 
				String strAuthen = map.getNamedItem("isAuthen").getNodeValue(); 
				vo.setAuthen(strAuthen.equalsIgnoreCase("true") ? Boolean.TRUE 
						: Boolean.FALSE);
			}
			if (map.getNamedItem("isCreateSQL") != null) { 
				String strCreateSQL = map.getNamedItem("isCreateSQL") 
						.getNodeValue();
				vo.setCreateSQL(strCreateSQL.equalsIgnoreCase("true") ? Boolean.TRUE 
						: Boolean.FALSE);
			}
			if (map.getNamedItem("isExtendBean") != null) { 
				String sstIsExtendBean = map.getNamedItem("isExtendBean") 
						.getNodeValue();
				vo.setIsExtendBean(sstIsExtendBean.equalsIgnoreCase("true") ? Boolean.TRUE 
						: Boolean.FALSE);
			}

			if (map.getNamedItem("bizItfImpClassName") != null) { 
				String clsName = map.getNamedItem("bizItfImpClassName") 
						.getNodeValue();
				vo.setBusiItfImplClsName(clsName);
			}
			if (map.getNamedItem("userDefClassName") != null) { 
				String clsName = map.getNamedItem("userDefClassName") 
						.getNodeValue();
				vo.setUserDefClsName(clsName);
			}

		}
	}

	public static void parseCanzhao(Node node, ValueObject vo) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if ("canzhaolist".equalsIgnoreCase(child.getNodeName())) { 
				NodeList cznl = child.getChildNodes();
				for (int j = 0; j < cznl.getLength(); j++) {
					Node cznode = cznl.item(j);
					CanZhao cz = CanZhao.parseNode(cznode, vo);
					if (cz != null)
						vo.addCanzhao(cz);
				}
			}
		}
	}

	public static void parseBusiItfsAndAttrMap(Node node, ValueObject vo) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if ("busiitfs".equalsIgnoreCase(child.getNodeName())) { 
				// NodeList nl2 = child.getChildNodes();
				// for (int j = 0; j < nl2.getLength(); j++) {
				// Node node2 = nl2.item(j);
				// if(node2.getNodeName().equalsIgnoreCase("itfid")){
				// String itfId = node2.getFirstChild().getNodeValue();
				// Cell cell = XMLSerialize.getInstance().getCell(itfId);
				// if(cell instanceof BusinInterface){
				// vo.getBusiItfs().add((BusinInterface)cell);
				// }
				// }
				// }
			}
			if ("busimaps".equalsIgnoreCase(child.getNodeName())) { 
				NodeList nl2 = child.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					Node node2 = nl2.item(j);
					if (node2.getNodeName().equalsIgnoreCase("busimap")) { 
						NamedNodeMap map = node2.getAttributes();
						String itfattrId = map.getNamedItem("busiitfattrid") 
								.getNodeValue();
						String attrid = map.getNamedItem("attrid") 
								.getNodeValue();
						Node nodeItem = map.getNamedItem("attrpathid"); 
						String attrpathid = null;
						if (nodeItem != null) {
							attrpathid = map.getNamedItem("attrpathid") 
									.getNodeValue();
						}
						Attribute attr1 = vo.getAttrById(attrid);
						Attribute attr2 = null;
						if (attr1 != null && attrpathid != null) {
							attr2 = vo.getAttrExtendById(attr1, attrpathid);
						}
						Attribute[] attrs = new Attribute[2];
						attrs[0] = attr1;
						attrs[1] = attr2;
						vo.getBusiattrAttrExtendMap().put(itfattrId, attrs);
					}
				}

			}
		}
	}

	public static ValueObject parseNode(Node node) {
		ValueObject vo = null;
		String name = node.getNodeName();
		if ("valueobject".equalsIgnoreCase(name)) { 
			vo = new ValueObject();
			ValueObject.parseNodeAttr(node, vo);
			parseVOAttrs(node, vo);
			parseOperationList(node, vo);
			parseBusiItfsAndAttrMap(node, vo);
			parseAccessor(node, vo);
			parseCanzhao(node, vo);
			XMLSerialize.getInstance().register(vo);
		}
		return vo;
	}

	protected static void parseAccessor(Node node, ValueObject vo) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if ("accessor".equalsIgnoreCase(child.getNodeName())) { 
				Accessor acc = Accessor.parseNode(child);
				vo.setAccess(acc);
			}
		}
	}

	protected static void parseVOAttrs(Node node, ValueObject vo) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if ("attributelist".equalsIgnoreCase(child.getNodeName())) { 
				NodeList nl2 = child.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					Node node2 = nl2.item(j);
					Attribute attr = Attribute.parseNode(node2);
					if (attr != null)
						vo.getProps().add(attr);
				}
			}
		}

	}

	public void dealBeforeSave() {
		// 保存前做一些处理
		// 属性
		if (getProps().size() > 0) {
		}
		for (int i = 1; i < getProps().size(); i++) {
			Attribute attr = getProps().get(i);
			attr.dealBeforeSave();
		}
	}

	/**
	 * 验证该实体
	 */
	public String validate() {

		String programTag = getGraph().getEndStrForIndustryElement();

		StringBuilder sb = new StringBuilder();
		String msg = super.validate();

		if (msg != null)
			sb.append(msg + "\r\n"); 
		if (getTableName() == null || getTableName().trim().length() == 0) {
			sb.append(Messages.ValueObject_167);
		}
		if (needCheckCurNameForPro()) {
			if (!getTableName().endsWith(programTag)) {
				sb.append(Messages.ValueObject_168 + getTableName() + Messages.ValueObject_169 + programTag
						+ Messages.ValueObject_170);
			}
		}

		if (needCheckCurNameForPro()) {
			if (!getFullClassName().endsWith(programTag)) {
				sb.append(Messages.ValueObject_171 + getName() + "::" + getDisplayName() 
						+ Messages.ValueObject_173 + programTag + Messages.ValueObject_174);
			}
		}

		ArrayList<String> al1 = new ArrayList<String>();
		ArrayList<String> al2 = new ArrayList<String>();
		if (getProps().size() > 0) {
			for (Attribute attr : getProps()) {
				String err = attr.validate(getGraph());
				if (err != null)
					sb.append(err);
			}
		}
		for (Attribute attr : getProps()) {
			String err = attr.validate();
			if (err != null)
				sb.append(err);
			String name = attr.getName();
			if (name != null && !al1.contains(name)) {
				al1.add(name);
			} else {
				sb.append(Messages.ValueObject_175 + name + "\r\n"); 
			}
			String disPlay = attr.getDisplayName();
			if (disPlay != null && !al2.contains(disPlay)) {
				al2.add(disPlay);
			} else {
				sb.append(Messages.ValueObject_177 + disPlay + "\r\n"); 
			}
		}
		al1 = new ArrayList<String>();
		al2 = new ArrayList<String>();
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
				sb.append(Messages.ValueObject_179 + name + "\r\n"); 
			}
			String disPlay = oper.getDisplayName();
			if (disPlay != null && !al2.contains(disPlay)) {
				al2.add(disPlay);
			} else {
				sb.append(Messages.ValueObject_181 + disPlay + "\r\n"); 
			}
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	public String getTableName() {
		if (tableName == null || tableName.trim().length() == 0) {
			return getGraph().getOwnModule() + "_" + getName(); 
		}
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	protected void copy0(ValueObject vo) {
		super.copy0(vo);
		vo.setAccess(this.getAccess().clone());
		vo.setTableName(this.getTableName());
		// if (this.getStereoType() != null)
		// vo.setStereoType((StereoType) this.getStereoType().clone());
		if (this.getStereoTypeList() != null) {
			List<StereoType> list = new ArrayList<StereoType>();
			for (StereoType type : this.getStereoTypeList()) {
				list.add((StereoType) type.clone());
			}
			vo.setStereoTypeList(list);
		}
		List<Attribute> al = this.getProps();
		for (int i = 0; i < al.size(); i++) {
			vo.getProps().add(al.get(i).copy());
		}
		List<Operation> list = this.getOperations();
		for (int i = 0; i < list.size(); i++) {
			vo.getOperations().add(list.get(i).copy());
		}
	}

	public ValueObject copy() {
		ValueObject vo = new ValueObject();
		copy0(vo);
		return vo;
	}

	public Boolean isAuthen() {
		return isAuthen;
	}

	public void setAuthen(Boolean isAuthen) {
		boolean old = this.isAuthen;
		this.isAuthen = isAuthen;
		firePropertyChange(PROP_IS_AUTHEN, old, isAuthen);
	}

	public Boolean isCreateSQL() {
		return isCreateSQL;
	}

	public void setCreateSQL(Boolean isCreateSQL) {
		boolean old = this.isCreateSQL;
		this.isCreateSQL = isCreateSQL;
		firePropertyChange(PROP_IS_CREATESQL, old, isCreateSQL);
	}

	public Boolean isExtendBean() {
		return isExtendBean;
	}

	public void setIsExtendBean(Boolean isExtendBean) {
		boolean old = this.isExtendBean;
		this.isExtendBean = isExtendBean;
		firePropertyChange(ENTITY_IS_EXTEND, old, isExtendBean);
	}

	public ArrayList<CanZhao> getAlCanzhao() {
		return alCanzhao;
	}

	public void addCanzhao(CanZhao cz) {
		cz.setVo(this);
		getAlCanzhao().add(cz);
	}

	public void delCanzhao(CanZhao cz) {
		getAlCanzhao().remove(cz);
		cz.setVo(null);
	}

	public void setDefaultCZ(CanZhao cz) {
		cz.setDefault(true);
		for (int i = 0; i < getAlCanzhao().size(); i++) {
			CanZhao czTemp = getAlCanzhao().get(i);
			if (!cz.equals(czTemp)) {
				czTemp.setDefault(false);
			}
		}
	}

	public String getBusiItfImplClsName() {
		return busiItfImplClsName;
	}

	public void setBusiItfImplClsName(String busiItfImplClsName) {
		this.busiItfImplClsName = busiItfImplClsName;
	}

	public String getUserDefClsName() {
		return userDefClassName;
	}

	public void setUserDefClsName(String userDefClsName) {
		this.userDefClassName = userDefClsName;
	}

	/**
	 * 获得该实体的属性中，所有非基本类型的类型
	 * @return
	 */
	public HashSet<String> getAllNonBaseTypeIds() {
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < getProps().size(); i++) {
			Attribute attr = getProps().get(i);
			Type type = attr.getType();
			if (type != null && !NCMDPTool.isBaseType(type.getTypeId())) {
				set.add(type.getTypeId());
			}
		}
		return set;
	}

	public List<StereoType> getStereoTypeList() {
		if(stereoTypeList==null){
			stereoTypeList = new ArrayList<StereoType>();
		}
		return stereoTypeList;
	}

	public void setStereoTypeList(List<StereoType> stereoTypeList) {
		this.stereoTypeList = stereoTypeList;
	}

	@Override
	public void dealIncDevForIndustry() {
		setSource(true);
		for (Attribute attr : props) {
			attr.dealIncDevForIndustry();
		}
	}
}
