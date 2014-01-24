package ncmdp.model;

import java.io.PrintWriter;
import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import ncmdp.common.MDPConstants;
import ncmdp.tool.NCMDPTool;
import ncmdp.wizard.multiwizard.util.IMultiElement;
/**
 * 枚举具体信息
 * @author wangxmn
 *
 */
public class EnumItem implements Cloneable, Serializable, Constant,
		IMultiElement {
	private static final long serialVersionUID = -8246128543032268745L;

	private String id = "";

	private String enumValue = "1";

	private String enumDisplay = "defaultDisplay";

	private String desc = "";

	private String versionType = "0";

	private String resId = "";

	private boolean hidden = false;

	private boolean industryChanged = false;
	private boolean isSource = false;
	private String modifyIndustry = MDPConstants.BASE_INDUSTRY;

	private String createIndustry = MDPConstants.BASE_INDUSTRY;

	private String classID = null;

	public EnumItem() {
		super();
		setId(NCMDPTool.generateID());
	}

	public String getModifyIndustry() {
		return modifyIndustry;
	}

	public void setModifyIndustry(String modifyIndustry) {
		this.modifyIndustry = modifyIndustry;
	}

	public String getCreateIndustry() {
		return createIndustry;
	}

	public void setCreateIndustry(String createIndustry) {
		this.createIndustry = createIndustry;
		setModifyIndustry(createIndustry);
	}

	public String getClassID() {
		return classID;
	}

	public void setClassID(String classID) {
		this.classID = classID;
	}

	public boolean isIndustryChanged() {
		return industryChanged;
	}

	public void setIndustryChanged(boolean industryChanged) {
		this.industryChanged = industryChanged;
	}

	public boolean isSource() {
		return isSource;
	}

	public void setSource(boolean isSource) {
		this.isSource = isSource;
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getEnumDisplay() {
		return enumDisplay;
	}

	public void setEnumDisplay(String enumDisplay) {
		this.enumDisplay = enumDisplay;
	}

	public String getEnumValue() {
		return enumValue;
	}

	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}

	public void setElementAttribute(Element ele) {
		ele.setAttribute("id", getId());
		ele.setAttribute("enumValue", getEnumValue());
		ele.setAttribute("enumDisplay", getEnumDisplay());
		ele.setAttribute("description", getDesc());
		ele.setAttribute("versionType", getVersionType());
		ele.setAttribute("resid", getResId());
		ele.setAttribute("createIndustry", getCreateIndustry());
		ele.setAttribute("modifyIndustry", getModifyIndustry());
		ele.setAttribute("hidden", isHidden() ? "true" : "false");
		ele.setAttribute("industryChanged", isIndustryChanged() ? "true"
				: "false");//支持扩展开发
		ele.setAttribute("isSource", isSource() ? "true" : "false");
	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id='").append(this.getId()).append("' ");
		sb.append("enumValue='").append(this.getEnumValue()).append("' ");
		sb.append("enumDisplay='").append(this.getEnumDisplay()).append("' ");
		sb.append("description='").append(this.getDesc()).append("' ");
		sb.append("versionType='").append(this.getVersionType()).append("' ");
		sb.append("resid='").append(this.getResId()).append("' ");
		sb.append("hidden='").append(this.isHidden() ? "true" : "false")
				.append("' ");
		sb.append("createIndustry='").append(this.getCreateIndustry())
				.append("' ");
		sb.append("modifyIndustry='").append(this.getModifyIndustry())
				.append("' ");
		sb.append("industryChanged='")
				.append(this.isIndustryChanged() ? "true" : "false")
				.append("' ");
		sb.append("isSource='").append(this.isSource() ? "true" : "false")
				.append("' ");

		return sb.toString();

	}

	public Element createElement(Document doc, String enumID, int index) {
		Element ele = doc.createElement("enumitem");
		ele.setAttribute("enumID", enumID);
		ele.setAttribute("sequence", index + "");
		setElementAttribute(ele);
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, String enumID,
			int index) {
		pw.print(tabStr + "<enumitem ");
		pw.print(" enumID='" + enumID + "' ");
		pw.print(" sequence='" + index + "' ");

		pw.print(genXMLAttrString());
		pw.println("/>");
	}

	public static EnumItem parseNode(Node node) {
		EnumItem enumItem = null;
		String name = node.getNodeName();
		if ("enumitem".equalsIgnoreCase(name)) {
			enumItem = new EnumItem();
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				enumItem.setId(map.getNamedItem("id").getNodeValue());
				enumItem.setEnumValue(map.getNamedItem("enumValue")
						.getNodeValue());
				enumItem.setEnumDisplay(map.getNamedItem("enumDisplay")
						.getNodeValue());
				if (map.getNamedItem("description") != null)
					enumItem.setDesc(map.getNamedItem("description")
							.getNodeValue());
				if (map.getNamedItem("versionType") != null) {
					enumItem.setVersionType(map.getNamedItem("versionType")
							.getNodeValue());
				}
				if (map.getNamedItem("enumID") != null) {
					enumItem.setClassID(map.getNamedItem("enumID")
							.getNodeValue());
				}
				if (map.getNamedItem("resid") != null) {
					enumItem.setResId(map.getNamedItem("resid").getNodeValue());
				}
				if (map.getNamedItem("hidden") != null) {
					enumItem.setHidden("true".equals(map.getNamedItem("hidden")
							.getNodeValue()));
				}
				if (map.getNamedItem("industrychanged") != null) {
					enumItem.setIndustryChanged("true".equals(map.getNamedItem(
							"industrychanged").getNodeValue()));
				}
				if (map.getNamedItem("isSource") != null) {
					enumItem.setSource("true".equals(map.getNamedItem(
							"isSource").getNodeValue()));
				}

				if (map.getNamedItem("createIndustry") != null) {
					enumItem.setCreateIndustry(map.getNamedItem(
							"createIndustry").getNodeValue());
				}
				if (map.getNamedItem("modifyIndustry") != null) {
					enumItem.setModifyIndustry(map.getNamedItem(
							"modifyIndustry").getNodeValue());
				}
			}
		}
		return enumItem;
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		if (isNull(getEnumValue()))
			sb.append("枚举项的值不能为空,\r\n");
		if (isNull(getEnumDisplay()))
			sb.append("枚举项的显示名称不能为空,\r\n");
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	public boolean isNull(String str) {
		return str == null || str.trim().length() == 0;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public EnumItem copy() {
		EnumItem item = new EnumItem();
		item.setDesc(this.getDesc());
		item.setEnumDisplay(this.getEnumDisplay());
		item.setEnumValue(this.getEnumValue());
		item.setVersionType(this.getVersionType());
		item.setResId(this.getResId());
		item.setHidden(this.isHidden());
		item.setCreateIndustry(this.getCreateIndustry());
		item.setModifyIndustry(this.getModifyIndustry());
		item.setIndustryChanged(this.isIndustryChanged());
		item.setSource(this.isSource());
		item.setClassID(this.getClassID());
		return item;
	}

	@Override
	public String getDisplayName() {
		return enumDisplay;
	}

	@Override
	public String getElementType() {
		return "        枚举项";
	}

	@Override
	public String getResid() {
		return resId;
	}

	@Override
	public void setResid(String resid) {
		this.resId = resid;
	}

	public void dealIncDevForIndustry() {
		setSource(true);
	}
}
