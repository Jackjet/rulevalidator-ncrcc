package ncmdp.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import ncmdp.editor.NCMDPEditor;
import ncmdp.exporttofea.wizard.Prop;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPUtil;
import ncmdp.wizard.multiwizard.util.IMultiElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 实体属性模型
 * 
 * @author wangxmn
 * 
 */
public class Attribute implements Cloneable, Serializable, Constant, IMultiElement {
	private static final long serialVersionUID = -4647747664838751928L;
	public static final String ACCESS_POWER = "accesspower";
	public static final String ACCESS_POWER_GROUP = "accesspowergroup";
	// property attributes
	private String id = null;
	private Cell parentCell = null;
	private String classID = null;
	// 名称
	private String name = "attrname";
	// 显示名称
	private String displayName = "attrDisplayName";
	// 类型样式
	private String typeStyle = TYPE_STYLES[0];
	// 类型
	private Type type = null;
	private String defaultValue = "";
	private String desc = "";
	private String help = "";
	private String accessStrategy = "";
	private String visibilityKind = Constant.VISIBILITY_PUBLIC;
	private boolean isAuthorization = true;
	private boolean isDefaultDimensionAttribute = false;
	private boolean isDefaultMeasureAttribute = false;
	private boolean isGlobalization = false;
	private boolean isHide = false;
	private boolean isShare = false;
	private boolean isKey = false;
	private boolean isNullable = true;
	private boolean accessPower = false;
	private String accessPowerGroup = "";
	/** yuyonga 增加是否序列化，是否动态属性两个属性 */
	private boolean notSerialize = false;
	private boolean nynamic = false;
	/** end */
	private String dynamicTable = "";
	private boolean isReadOnly = false;
	private String versionType = "0";
	/* 行业 */
	private boolean industryChanged = false;
	private boolean isSource = true;// false;
	private String modifyIndustry = null;
	private String createIndustry = null;
	private String fieldName = "";
	private String fieldType = "";
	private String minValue = "";
	private String maxValue = "";
	private boolean isFixLen = false;
	private String length = "";
	private String precision = "";
	private boolean isCalculateAttr = false;
	private boolean isActive = true;
	private String refModuleName = "";
	private String resid = "";
	/* 是否特性，增加这个属性用于控制特性固化 dingxm@2008-08-31 */
	private Boolean isFeature = false;
	private boolean forLocale = false;
	public static String FORLOCALE = "forLocale";

	// 属性的初始化
	public Attribute() {
		super();
		// 产生位移标志符
		id = NCMDPTool.generateID();
		if (NCMDPTool.getBaseTypes().length > 0) {
			// 设置属性为获得的第一种基本属性
			setType(NCMDPTool.getBaseTypes()[1]);
			setLength(getType().getLength());
			setPrecision(getType().getPrecise());
		}
	}

	public String getModifyIndustry() {
		if (modifyIndustry == null) {
			modifyIndustry = MDPUtil.getDevCode();
		}
		return modifyIndustry;
	}

	public void setModifyIndustry(String modifyIndustry) {
		this.modifyIndustry = modifyIndustry;
	}

	public String getCreateIndustry() {
		if (createIndustry == null) {
			createIndustry = MDPUtil.getDevCode();
		}
		return createIndustry;
	}

	public void setCreateIndustry(String createIndustry) {
		this.createIndustry = createIndustry;
		setModifyIndustry(createIndustry);
	}

	public Cell getParentCell() {
		return parentCell;
	}

	public void setParentCell(Cell parentCell) {
		this.parentCell = parentCell;
	}

	public boolean isForLocale() {
		return forLocale;
	}

	public void setForLocale(boolean forLocale) {
		this.forLocale = forLocale;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isCalculateAttr() {
		return isCalculateAttr;
	}

	public void setCalculateAttr(boolean isCalculateAttr) {
		this.isCalculateAttr = isCalculateAttr;
	}

	public boolean isFixLen() {
		return isFixLen;
	}

	public void setFixLen(boolean isFixLen) {
		this.isFixLen = isFixLen;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getRefModuleName() {
		return refModuleName;
	}

	public void setRefModuleName(String refModuleName) {
		this.refModuleName = refModuleName;
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isHide() {
		return isHide;
	}

	public void setHide(boolean isHide) {
		this.isHide = isHide;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null) {
			name = name.toLowerCase();
		}
		this.name = name;
	}

	public String getTypeStyle() {
		return typeStyle;
	}

	public void setTypeStyle(String type) {
		this.typeStyle = type;
	}

	public String getAccessStrategy() {
		return accessStrategy;
	}

	public void setAccessStrategy(String accessStrategy) {
		this.accessStrategy = accessStrategy;
	}

	public String getClassID() {
		return classID;
	}

	public void setClassID(String classID) {
		this.classID = classID;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	// public String getId() {
	// return id;
	// }
	// public void setId(String id) {
	// this.id = id;
	// }
	public boolean isAuthorization() {
		return isAuthorization;
	}

	public void setAuthorization(boolean isAuthorization) {
		this.isAuthorization = isAuthorization;
	}

	public boolean isDefaultDimensionAttribute() {
		return isDefaultDimensionAttribute;
	}

	public void setDefaultDimensionAttribute(boolean isDefaultDimensionAttribute) {
		this.isDefaultDimensionAttribute = isDefaultDimensionAttribute;
	}

	public boolean isDefaultMeasureAttribute() {
		return isDefaultMeasureAttribute;
	}

	public void setDefaultMeasureAttribute(boolean isDefaultMeasureAttribute) {
		this.isDefaultMeasureAttribute = isDefaultMeasureAttribute;
	}

	public boolean isGlobalization() {
		return isGlobalization;
	}

	public void setGlobalization(boolean isGlobalization) {
		this.isGlobalization = isGlobalization;
	}

	public boolean isKey() {
		return isKey;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	public boolean isAccessPower() {
		return accessPower;
	}

	public void setAccessPower(boolean accessPower) {
		this.accessPower = accessPower;
	}

	public String getAccessPowerGroup() {
		return accessPowerGroup;
	}

	public void setAccessPowerGroup(String accessPowerGroup) {
		this.accessPowerGroup = accessPowerGroup;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public boolean isShare() {
		return isShare;
	}

	public void setShare(boolean isShare) {
		this.isShare = isShare;
	}

	public boolean getIsSequence() {
		return notSerialize;
	}

	public void setIsSequence(boolean notSerialize) {
		this.notSerialize = notSerialize;
	}

	public boolean getIsDynamicAttr() {
		return nynamic;
	}

	public void setIsDynamicAttr(boolean isDynamicAttr) {
		this.nynamic = isDynamicAttr;
	}

	public String getVisibilityKind() {
		return visibilityKind;
	}

	public void setVisibilityKind(String visibilityKind) {
		this.visibilityKind = visibilityKind;
	}

	public void setElementAttribute(Element ele) {
		ele.setAttribute("id", getId());
		ele.setAttribute("name", getName());
		ele.setAttribute("displayName", getDisplayName());
		ele.setAttribute("dataTypeStyle", getTypeStyle());
		if (getType() != null) {
			getType().setElementAttribute(ele);
		}
		ele.setAttribute("defaultValue", getDefaultValue());
		ele.setAttribute("description", getDesc());
		ele.setAttribute("help", getHelp());
		ele.setAttribute(ACCESS_POWER_GROUP, getAccessPowerGroup());
		ele.setAttribute("accessStrategy", getAccessStrategy());
		ele.setAttribute("visibility", getVisibilityKind());
		ele.setAttribute("isAuthorization", isAuthorization() ? "true" : "false");
		ele.setAttribute("isDefaultDimensionAttribute", isDefaultDimensionAttribute() ? "true" : "false");
		ele.setAttribute("isFeature", getIsFeature() ? "true" : "false");
		ele.setAttribute("isDefaultMeasureAttribute", isDefaultMeasureAttribute() ? "true" : "false");
		ele.setAttribute("isGlobalization", isGlobalization() ? "true" : "false");
		ele.setAttribute("isHide", isHide() ? "true" : "false");
		ele.setAttribute("isShare", isShare() ? "true" : "false");
		ele.setAttribute("isKey", isKey() ? "true" : "false");
		ele.setAttribute("isNullable", isNullable() ? "true" : "false");
		ele.setAttribute(ACCESS_POWER, isAccessPower() ? "true" : "false");
		ele.setAttribute("isReadOnly", isReadOnly() ? "true" : "false");
		ele.setAttribute("versionType", getVersionType());
		ele.setAttribute("createIndustry", getCreateIndustry());
		ele.setAttribute("modifyIndustry", getModifyIndustry());
		ele.setAttribute("industryChanged", isIndustryChanged() ? "true" : "false");
		ele.setAttribute("isSource", isSource() ? "true" : "false");
		ele.setAttribute("fieldName", getFieldName());
		ele.setAttribute("fieldType", getFieldType());
		ele.setAttribute(FORLOCALE, isForLocale() ? "true" : "false");

		ele.setAttribute("minValue", getMinValue());
		ele.setAttribute("maxValue", getMaxValue());
		ele.setAttribute("fixedLength", isFixLen() ? "true" : "false");
		ele.setAttribute("length", getLength());
		ele.setAttribute("precise", getPrecision());
		ele.setAttribute("calculation", isCalculateAttr() ? "true" : "false");
		ele.setAttribute("isActive", isActive() ? "true" : "false");
		ele.setAttribute("refModelName", getRefModuleName());
		ele.setAttribute("resid", getResid());
		ele.setAttribute("notSerialize", getIsSequence() ? "true" : "false");
		ele.setAttribute("dynamicTable", getDynamicTable());
		ele.setAttribute("dynamic", getIsDynamicAttr() ? "true" : "false");
	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id='").append(this.getId()).append("' ");
		sb.append("name='").append(this.getName()).append("' ");
		sb.append("displayName='").append(this.getDisplayName()).append("' ");
		sb.append("dataTypeStyle='").append(this.getTypeStyle()).append("' ");
		if (getType() != null) {
			sb.append(getType().genXMLAttrString());
		}

		sb.append("defaultValue='").append(this.getDefaultValue()).append("' ");
		sb.append("description='").append(this.getDesc()).append("' ");
		sb.append("help='").append(this.getHelp()).append("' ");
		sb.append(ACCESS_POWER_GROUP + "='").append(this.getAccessPowerGroup()).append("' ");
		sb.append("accessStrategy='").append(this.getAccessStrategy()).append("' ");
		sb.append("visibility='").append(this.getVisibilityKind()).append("' ");
		sb.append("isAuthorization='").append(this.isAuthorization() ? "true" : "false").append("' ");
		sb.append("isDefaultDimensionAttribute='").append(this.isDefaultDimensionAttribute() ? "true" : "false")
				.append("' ");
		sb.append("isFeature='").append(this.getIsFeature() ? "true" : "false").append("' ");
		sb.append("isDefaultMeasureAttribute='").append(this.isDefaultMeasureAttribute() ? "true" : "false")
				.append("' ");
		sb.append("isGlobalization='").append(this.isGlobalization() ? "true" : "false").append("' ");
		sb.append("isHide='").append(this.isHide() ? "true" : "false").append("' ");
		sb.append("isShare='").append(this.isShare() ? "true" : "false").append("' ");
		sb.append("isKey='").append(this.isKey() ? "true" : "false").append("' ");
		sb.append("isNullable='").append(this.isNullable() ? "true" : "false").append("' ");
		sb.append(ACCESS_POWER + "='").append(this.isAccessPower() ? "true" : "false").append("' ");
		sb.append("isReadOnly='").append(this.isReadOnly() ? "true" : "false").append("' ");
		sb.append("versionType='").append(this.getVersionType()).append("' ");
		sb.append("createIndustry='").append(this.getCreateIndustry()).append("' ");
		sb.append("modifyIndustry='").append(this.getModifyIndustry()).append("' ");
		sb.append("industryChanged='").append(this.isIndustryChanged() ? "true" : "false").append("' ");
		sb.append("isSource='").append(this.isSource() ? "true" : "false").append("' ");
		sb.append("fieldName='").append(this.getFieldName()).append("' ");
		sb.append("fieldType='").append(this.getFieldType()).append("' ");
		sb.append(FORLOCALE).append("='").append(this.isForLocale() ? "true" : "false").append("' ");
		sb.append("minValue='").append(this.getMinValue()).append("' ");
		sb.append("maxValue='").append(this.getMaxValue()).append("' ");
		sb.append("fixedLength='").append(this.isFixLen() ? "true" : "false").append("' ");
		sb.append("length='").append(this.getLength()).append("' ");
		sb.append("precise='").append(this.getPrecision()).append("' ");
		sb.append("calculation='").append(this.isCalculateAttr() ? "true" : "false").append("' ");
		sb.append("isActive='").append(this.isActive() ? "true" : "false").append("' ");
		sb.append("refModelName='").append(this.getRefModuleName()).append("' ");
		sb.append("resid='").append(this.getResid()).append("' ");
		sb.append("notSerialize='").append(this.getIsSequence() ? "true" : "false").append("' ");
		sb.append("dynamic='").append(this.getIsDynamicAttr() ? "true" : "false").append("' ");
		sb.append("dynamicTable='").append(this.getDynamicTable()).append("' ");
		return sb.toString();

	}

	public Element createElement(Document doc, int index, String classId) {
		Element ele = doc.createElement("attribute");
		ele.setAttribute("sequence", index + "");
		ele.setAttribute("classID", classId);
		setElementAttribute(ele);
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, int index, String classId) {
		pw.print(tabStr + "<attribute ");
		pw.print(" sequence='" + index + "' ");
		pw.print(" classID='" + classId + "' ");

		pw.print(genXMLAttrString());
		pw.println("/>");
	}

	/**
	 * 解析attribute节点
	 * 
	 * @param node
	 * @return
	 */
	public static Attribute parseNode(Node node) {
		Attribute attr = null;
		String name = node.getNodeName();
		if ("attribute".equalsIgnoreCase(name)) {
			attr = new Attribute();
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				attr.setId(map.getNamedItem("id").getNodeValue());
				attr.setName(map.getNamedItem("name").getNodeValue());
				attr.setDisplayName(map.getNamedItem("displayName").getNodeValue());
				if (map.getNamedItem("dataTypeStyle") != null) {// 类型
					attr.setTypeStyle(map.getNamedItem("dataTypeStyle").getNodeValue());
				}
				Type type = Type.parseType(map);
				if (type != null)
					attr.setType(type);
				attr.setDefaultValue(map.getNamedItem("defaultValue").getNodeValue());
				attr.setClassID(map.getNamedItem("classID").getNodeValue());// 所属实体id
				attr.setDesc(map.getNamedItem("description").getNodeValue());
				attr.setHelp(map.getNamedItem("help").getNodeValue());
				if (map.getNamedItem(ACCESS_POWER_GROUP) != null) {// 使用权组
					attr.setAccessPowerGroup(map.getNamedItem(ACCESS_POWER_GROUP).getNodeValue());
				}
				attr.setAccessStrategy(map.getNamedItem("accessStrategy") // 访问策略
						.getNodeValue());
				attr.setVisibilityKind(map.getNamedItem("visibility").getNodeValue());
				attr.setAuthorization("true".equals(map.getNamedItem( // 是否授权，貌似被隐藏了
						"isAuthorization").getNodeValue()));
				attr.setDefaultDimensionAttribute("true".equals(map.getNamedItem("isDefaultDimensionAttribute")
						.getNodeValue()));
				if (map.getNamedItem("isFeature") != null) {
					attr.setIsFeature("true".equals(map.getNamedItem("isFeature").getNodeValue()));
				}

				attr.setDefaultMeasureAttribute("true".equals(map.getNamedItem("isDefaultMeasureAttribute")
						.getNodeValue()));
				attr.setGlobalization("true".equals(map.getNamedItem("isGlobalization").getNodeValue()));
				attr.setHide("true".equals(map.getNamedItem("isHide").getNodeValue()));
				attr.setShare("true".equals(map.getNamedItem("isShare").getNodeValue()));
				attr.setKey("true".equals(map.getNamedItem("isKey").getNodeValue()));
				attr.setNullable("true".equals(map.getNamedItem("isNullable").getNodeValue()));
				if (map.getNamedItem(ACCESS_POWER) != null) {
					attr.setAccessPower("true".equals(map.getNamedItem(ACCESS_POWER).getNodeValue()));
				}
				attr.setReadOnly("true".equals(map.getNamedItem("isReadOnly").getNodeValue()));
				if (map.getNamedItem("versionType") != null) {
					attr.setVersionType(map.getNamedItem("versionType").getNodeValue());
				}
				if (map.getNamedItem("createIndustry") != null) {
					attr.setCreateIndustry(map.getNamedItem("createIndustry").getNodeValue());
				}
				if (map.getNamedItem("modifyIndustry") != null) {
					attr.setModifyIndustry(map.getNamedItem("modifyIndustry").getNodeValue());
				}
				if (map.getNamedItem("industryChanged") != null) { // 增量开发中用到
					attr.setIndustryChanged("true".equals(map.getNamedItem("industryChanged").getNodeValue()));
				}
				if (map.getNamedItem("isSource") != null) { // 增量开发中用到
					attr.setSource("true".equals(map.getNamedItem("isSource").getNodeValue()));
				}

				if (map.getNamedItem("fieldName") != null) {
					attr.setFieldName(map.getNamedItem("fieldName").getNodeValue());
				}
				if (map.getNamedItem("fieldType") != null) { // 字段类型
					attr.setFieldType(map.getNamedItem("fieldType").getNodeValue());
				}
				if (map.getNamedItem(FORLOCALE) != null) {// 支持本地语言
					attr.setForLocale("true".equalsIgnoreCase(map.getNamedItem(FORLOCALE).getNodeValue()));
				}
				if (map.getNamedItem("minValue") != null) {
					attr.setMinValue(map.getNamedItem("minValue").getNodeValue());
				}
				if (map.getNamedItem("maxValue") != null) {
					attr.setMaxValue(map.getNamedItem("maxValue").getNodeValue());
				}
				if (map.getNamedItem("fixedLength") != null) {
					attr.setFixLen("true".equalsIgnoreCase(map.getNamedItem("fixedLength").getNodeValue()));
				}
				if (map.getNamedItem("length") != null) {
					attr.setLength(map.getNamedItem("length").getNodeValue());
				}
				if (map.getNamedItem("precise") != null) {
					attr.setPrecision(map.getNamedItem("precise").getNodeValue());
				}
				if (map.getNamedItem("calculation") != null) { // 计算属性
					attr.setCalculateAttr("true".equalsIgnoreCase(map.getNamedItem("calculation").getNodeValue()));
				}
				if (map.getNamedItem("isActive") != null) {
					attr.setActive("true".equalsIgnoreCase(map.getNamedItem("isActive").getNodeValue()));
				}
				if (map.getNamedItem("refModelName") != null) {
					attr.setRefModuleName(map.getNamedItem("refModelName").getNodeValue());
				}
				if (map.getNamedItem("resid") != null) {
					attr.setResid(map.getNamedItem("resid").getNodeValue());
				}
				if (map.getNamedItem("notSerialize") != null) { // 可序列化
					attr.setIsSequence("true".equalsIgnoreCase(map.getNamedItem("notSerialize").getNodeValue()));
				}
				if (map.getNamedItem("dynamic") != null) {
					attr.setIsDynamicAttr("true".equalsIgnoreCase(map.getNamedItem("dynamic").getNodeValue()));
				}
				if (map.getNamedItem("dynamicTable") != null) {
					attr.setDynamicTable(map.getNamedItem("dynamicTable").getNodeValue());
				}
			}
		}
		return attr;
	}

	public void dealBeforeSave() {
		if (!NCMDPTool.isBaseType(type.getTypeId()) && "REF".equalsIgnoreCase(getTypeStyle())) {
			if (isNullable()) {
				setFieldType("varchar");
				if (StringUtil.isEmptyWithTrim(getDefaultValue())) {
					setDefaultValue("~");
				}
			}

			if (!"20".equalsIgnoreCase(getLength()) && !"36".equalsIgnoreCase(getLength())) {
				String message = Messages.Attribute_311 + getName() + ":" + getDisplayName() + Messages.Attribute_313
						+ "";
				if (MessageDialog.openConfirm(NCMDPEditor.getActiveMDPEditor().getSite().getShell(),
						Messages.Attribute_315, message)) {
					setLength("20");
				} else if (StringUtil.isEmptyWithTrim(getLength())) {
					setLength("20");
				}
			}
		}
		if ("BS000010000100001059".equalsIgnoreCase(type.getTypeId())
				|| "BS000010000100001056".equalsIgnoreCase(type.getTypeId())) {
			setLength("101");
		}

		// 给供应链做的特殊处理
		if (Pattern.matches("vdef(([1-9])|(1\\d)|(20))", getName())) {
			setType(NCMDPTool.getBaseTypeById("BS000010000100001056"));
			setLength("101");
		}
		if (Pattern.matches("vfree(([1-9])|(10))", getName())) {
			setType(NCMDPTool.getBaseTypeById("BS000010000100001059"));
			setLength("101");
		}
	}

	public String validate(JGraph graph) {
		StringBuilder sb = new StringBuilder();
		String str = validate();
		if (!isNull(str)) {
			sb.append(str);
		}
		if (graph.isIndustryIncrease()) {
			String endTag = graph.getEndStrForIndustryElement();
			Cell cell = graph.getCellByID(getClassID());
			if (cell != null && cell.isSource()) {// 只针对原有实体，不针对行业新增实体
				if (!isSource() && !"ARRAY".equalsIgnoreCase(getTypeStyle())) {// 1)增量开发的属性
																				// 2)非子表属性
					if (StringUtil.isEmptyWithTrim(getDynamicTable()) && !TYPE_STYLES[1].equalsIgnoreCase(typeStyle)) {
						sb.append(Messages.Attribute_328 + graph.getCellByID(getClassID()).getName() + "." + getName()
								+ "\r\n");
					} else {
						if (!getDynamicTable().endsWith(endTag)) {
							sb.append(Messages.Attribute_331 + endTag + Messages.Attribute_332
									+ graph.getCellByID(getClassID()).getName() + "." + getName() + "\r\n");
						}
					}

					if (!getName().endsWith(endTag)) {
						sb.append(Messages.Attribute_335 + getName() + "::" + getDisplayName() + Messages.Attribute_337
								+ endTag + Messages.Attribute_338 + "\r\n");
					}
				}
			}
		}

		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		if (isNull(getName()))
			sb.append(Messages.Attribute_340);
		if (isNull(getDisplayName()))
			sb.append(Messages.Attribute_341);
		if (getType() == null) {
			sb.append(Messages.Attribute_342);
		} else if (isNull(getType().getTypeId())) {
			sb.append(Messages.Attribute_343);
		}
		if (getTypeStyle() == null || getTypeStyle().trim().length() == 0) {
			sb.append(Messages.Attribute_344);
		}
		if (getFieldName() == null || getFieldName().trim().length() == 0) {
			sb.append(Messages.Attribute_345);
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	public boolean isNull(String str) {
		return str == null || str.trim().length() == 0;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public boolean isIndustryChanged() {
		return industryChanged;
	}

	public void setIndustryChanged(boolean changed) {
		this.industryChanged = changed;
	}

	public boolean isSource() {
		return isSource;
	}

	public void setSource(boolean isSource) {
		this.isSource = isSource;
	}

	public String getFieldName() {
		if (fieldName == null || fieldName.trim().length() == 0) {
			return getName();
		} else {
			return fieldName;
		}
	}

	public void setFieldName(String fieldName) {
		if (fieldName != null)
			fieldName = fieldName.toLowerCase();
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		if (getType() != null && (fieldType == null || fieldType.trim().length() == 0)) {
			return NCMDPTool.getDefaultFieldType(getType().getTypeId());
		}
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		if (fieldType != null)
			fieldType = fieldType.toLowerCase();
		this.fieldType = fieldType;
	}

	public Attribute copy() {
		Attribute attr = new Attribute();
		attr.setAccessStrategy(this.getAccessStrategy());
		attr.setActive(this.isActive());
		attr.setAuthorization(this.isAuthorization());
		attr.setCalculateAttr(this.isCalculateAttr());
		attr.setDefaultDimensionAttribute(this.isDefaultDimensionAttribute());
		attr.setDefaultMeasureAttribute(this.isDefaultMeasureAttribute());
		attr.setDefaultValue(this.getDefaultValue());
		attr.setClassID(this.getClassID());
		attr.setDesc(this.getDesc());
		attr.setDisplayName(this.getDisplayName());
		attr.setFieldName(this.getFieldName());
		attr.setFieldType(this.getFieldType());
		attr.setForLocale(this.isForLocale());
		attr.setFixLen(this.isFixLen());
		attr.setGlobalization(this.isGlobalization());
		attr.setHelp(this.getHelp());
		attr.setAccessPowerGroup(this.getAccessPowerGroup());
		attr.setHide(this.isHide());
		attr.setKey(this.isKey());
		attr.setLength(this.getLength());
		attr.setMaxValue(this.getMaxValue());
		attr.setMinValue(this.getMinValue());
		attr.setName(this.getName());
		attr.setNullable(this.isNullable());
		attr.setAccessPower(this.isAccessPower());
		attr.setPrecision(this.getPrecision());
		attr.setReadOnly(this.isReadOnly());
		attr.setRefModuleName(this.getRefModuleName());
		attr.setShare(this.isShare());
		attr.setType(this.getType());
		attr.setTypeStyle(this.getTypeStyle());
		attr.setVersionType(this.getVersionType());
		attr.setCreateIndustry(this.getCreateIndustry());
		attr.setIndustryChanged(this.isIndustryChanged());
		attr.setSource(this.isSource());
		attr.setModifyIndustry(this.getModifyIndustry());
		attr.setVisibilityKind(this.getVisibilityKind());
		attr.setResid(this.getResid());
		attr.setIsSequence(this.getIsSequence());
		attr.setIsDynamicAttr(this.getIsDynamicAttr());
		attr.setIsFeature(this.getIsFeature());
		attr.setDynamicTable(this.getDynamicTable());
		return attr;
	}

	public String toString() {
		return getDisplayName();
	}

	public String getResid() {
		return resid;
	}

	public void setResid(String resid) {
		this.resid = resid;

	}

	public Boolean getIsFeature() {
		return isFeature;
	}

	public void setIsFeature(Boolean isFeature) {
		this.isFeature = isFeature;
	}

	public String getDynamicTable() {
		return dynamicTable;
	}

	public void setDynamicTable(String dynamicTable) {
		this.dynamicTable = dynamicTable;
	}

	@Override
	public String getElementType() {
		return Messages.Attribute_346;
	}

	public void dealIncDevForIndustry() {
		setSource(true);
	}

	public List<Prop> getEntityProps() {
		List<Prop> props = new ArrayList<Prop>();

		if (!this.getName().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_4, "name", this.getName()));
		}
		if (!this.getDisplayName().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_5, "displayName", this.getDisplayName()));
		}
		if (!this.getTypeStyle().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_6, "typeStyle", this.getTypeStyle()));
		}
		if (this.getType() != null) {
			props.add(new Prop(Messages.CellModifier_7, "type", this.getType()));
		}
		if (!this.getFieldName().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_8, "fieldName", this.getFieldName()));
		}
		if (!this.getFieldType().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_9, "fieldType", this.getFieldType()));
		}
		if (!this.getRefModuleName().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_10, "refModuleName", this.getRefModuleName()));
		}
		if (!this.getDesc().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_11, "desc", this.getDesc()));
		}
		props.add(new Prop(Messages.CellModifier_12, "isHide", this.isHide()));
		props.add(new Prop(Messages.CellModifier_13, "isNullable", this.isNullable()));
		props.add(new Prop(Messages.CellModifier_14, "isReadOnly", this.isReadOnly()));
		if (!getAccessStrategy().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_15, "accessStrategy", this.getAccessStrategy()));
		}
		if (!getVisibilityKind().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_16, "visibilityKind", this.getVisibilityKind()));
		}
		if (!getDefaultValue().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_17, "defaultValue", this.getDefaultValue()));
		}
		if (!getMaxValue().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_18, "maxValue", this.getMaxValue()));
		}
		if (!getMinValue().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_19, "minValue", this.getMinValue()));
		}
		if (!getLength().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_20, "length", this.getLength()));
		}
		if (!getPrecision().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_21, "precision", this.getPrecision()));
		}
		props.add(new Prop(Messages.CellModifier_22, "isFixLen", this.isFixLen()));
		props.add(new Prop(Messages.CellModifier_23, "isCalculateAttr", this.isCalculateAttr()));
		props.add(new Prop(Messages.CellModifier_24, "isActive", this.isActive()));
		props.add(new Prop(Messages.CellModifier_25, "isAuthorization", this.isAuthorization()));
		if (!getResid().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_26, "resid", this.getResid()));
		}
		props.add(new Prop(Messages.CellModifier_28, "notSerialize", this.getIsSequence()));
		props.add(new Prop(Messages.CellModifier_29, "nynamic", this.getIsDynamicAttr()));
		if (!getDynamicTable().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_30, "dynamicTable", this.getDynamicTable()));
		}
		props.add(new Prop(Messages.CellModifier_31, "accessPower", this.isAccessPower()));
		if (!getAccessPowerGroup().trim().equals("")) {
			props.add(new Prop(Messages.CellModifier_32, "accessPowerGroup", this.getAccessPowerGroup()));
		}
		props.add(new Prop(Messages.CellModifier_33, "forLocale", this.isForLocale()));

		return props;
	}
}
